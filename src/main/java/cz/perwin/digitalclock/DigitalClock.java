package cz.perwin.digitalclock;

import com.google.common.base.Charsets;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.Commands;
import cz.perwin.digitalclock.core.Events;
import cz.perwin.digitalclock.core.Generator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
//import cz.perwin.digitalclock.utils.Metrics;

public class DigitalClock extends JavaPlugin {
    static {
        System.out.println("[DigitalClock] Preparing DigitalClock for loading...");
        File pluginDir = new File("plugins/DigitalClock");
        if (!pluginDir.exists()) {
            pluginDir.mkdir();
        }
        final File table = new File("plugins/DigitalClock/GeoLiteCity.dat");
        if (!table.exists()) {
            Thread thread = new Thread(() -> {
                Thread.currentThread().setName(Thread.currentThread().getName() + " - DigitalClock GeoLocation download");
                System.out.println("[DigitalClock] Downloading file " + table.getName() + ".");
                try {
                    URL link = new URL("http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz");
                    ReadableByteChannel rbc = Channels.newChannel(new GZIPInputStream(link.openStream()));
                    FileOutputStream fos = new FileOutputStream(table);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    System.out.println("[DigitalClock] File " + table.getName() + " has been downloaded.");
                } catch (IOException e) {
                    System.err.println("[DigitalClock] Error when downloading file " + table.getName() + ": " + e);
                }
            });
            thread.start();
        }
    }

    private final Logger console = this.getLogger();
    private Map<Player, String> enableBuildUsers = new HashMap<>();
    private Map<Player, String> enableMoveUsers = new HashMap<>();
    private Map<String, Integer> usersClock = new HashMap<>();
    private ArrayList<String> clocks = new ArrayList<>();
    private ClockMap clockTasks = new ClockMap();
    private FileConfiguration clocksConf = null;
    private File clocksFile = null;
    private int settings_width = 0;
    private boolean separately;
    private boolean shouldRun;
    private boolean runStart;
    private boolean versionWarning;
    private boolean protectClocks;
    private long generatorAccuracy = 0;
    private Generator generator;

    public static String getMessagePrefix() {
        return "[DigitalClock]";
    }

    public void onEnable() {
        this.console.info("[DigitalClock] Plugin has been enabled!");
        // PREPARING SERVER
        this.saveDefaultConfig();
        this.reloadConf();
        this.saveDefaultClocksConf();
        this.reloadClocksConf();
        this.generator = new Generator(this);

        // CHECK VERSION
        if (this.getConfig().getBoolean("enableVersionOnStartChecking", true)) {
            Version.check(this.getDescription().getVersion());
        }

        // LOADING CLASSES
        this.getServer().getPluginManager().registerEvents(new Events(this), this);
        Commands cmdExecutor = new Commands(this);
        this.getServer().getPluginCommand("digitalclock").setExecutor(cmdExecutor);

        // LOADING CLOCKS AND GEOIPTABLES
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new AfterDone(this), 0L);

        // METRICS
		/*try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
			this.console.severe(e + "");
		}*/
    }

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        this.console.info("[DigitalClock] Plugin has been disabled!");
        this.saveClocksConf();
    }

    protected void runTasks() {
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this,
                DigitalClock.this::saveClocksConf, 20L, (15 * 60 * 20));

        for (final String name : getClocksL()) {
            this.run(name);
        }
    }

    public void run(final String name) {
        if (!this.getClockTasks().containsKeyByClockName(name)) {
            final Clock clock = Clock.loadClockByClockName(name);
            clock.reloadFromConfig();
            int task = this.getServer().getScheduler().scheduleSyncRepeatingTask(Generator.getGenerator().getMain(), () -> {
                if (DigitalClock.this.getClocksConf().getKeys(false).contains(clock.getName())) {
                    Generator.getGenerator().generateOnce(clock);
                }
            }, 0L, 20L);
            this.getClockTasks().put(clock, task);
        }
    }

    public void reloadConf() {
        this.reloadConfig();
        DigitalClock.this.runStart = this.getConfig().getBoolean("runStart", false);
        DigitalClock.this.settings_width = this.getConfig().getInt("width", 3);
        DigitalClock.this.separately = this.getConfig().getBoolean("generateForEachPlayerSeparately", false);
        DigitalClock.this.shouldRun = this.getConfig().getBoolean("runAfterCreation", false);
        DigitalClock.this.versionWarning = this.getConfig().getBoolean("enableNewerVersionWarning", true);
        DigitalClock.this.protectClocks = this.getConfig().getBoolean("protectClocks", false);
        DigitalClock.this.generatorAccuracy = this.getConfig().getLong("generatorAccuracy", 2000L);
    }

    public void reloadClocksConf() {
        setClocksConf(YamlConfiguration.loadConfiguration(clocksFile));
        InputStream defConfigStream = this.getResource("clocks.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
            getClocksConf().setDefaults(defConfig);
        }
    }

    protected void saveDefaultClocksConf() {
        if (clocksFile == null) {
            clocksFile = new File(getDataFolder(), "clocks.yml");
        }
        if (!clocksFile.exists()) {
            this.saveResource("clocks.yml", false);
        }
    }

    public void saveClocksConf() {
        if (getClocksConf() == null || clocksFile == null) {
            return;
        }
        try {
            DigitalClock.this.getClocksConf().save(clocksFile);
        } catch (IOException ex) {
            this.console.severe(ex + "");
        }
    }

    public void getClocks() {
        this.getClocksL().clear();
        this.getUsersClock().clear();
        for (String name : DigitalClock.this.getClocksConf().getKeys(false)) {
            Clock clock = Clock.loadClockByClockName(name);
            if (this.getUsersClock().containsKey(clock.getCreator())) {
                int n = this.getUsersClock().get(clock.getCreator());
                this.getUsersClock().remove(clock.getCreator());
                this.getUsersClock().put(clock.getCreator(), (n + 1));
            } else {
                this.getUsersClock().put(clock.getCreator(), 1);
            }
            this.getClocksL().add(name);
        }
    }

    public Map<String, Integer> getUsersClock() {
        return usersClock;
    }

    public void setUsersClock(Map<String, Integer> usersClock) {
        this.usersClock = usersClock;
    }

    public Map<Player, String> getEnableBuildUsers() {
        return enableBuildUsers;
    }

    public void setEnableBuildUsers(Map<Player, String> enableBuildUsers) {
        this.enableBuildUsers = enableBuildUsers;
    }

    public FileConfiguration getClocksConf() {
        return clocksConf;
    }

    public void setClocksConf(FileConfiguration clocksConf) {
        this.clocksConf = clocksConf;
    }

    public Map<Player, String> getEnableMoveUsers() {
        return enableMoveUsers;
    }

    public void setEnableMoveUsers(Map<Player, String> enableMoveUsers) {
        this.enableMoveUsers = enableMoveUsers;
    }

    public ClockMap getClockTasks() {
        return clockTasks;
    }

    public void setClockTasks(ClockMap clockTasks) {
        this.clockTasks = clockTasks;
    }

    public Generator getGenerator() {
        return this.generator;
    }

    public ArrayList<String> getClocksL() {
        return clocks;
    }

    public void setClocksL(ArrayList<String> clocks) {
        this.clocks = clocks;
    }

    public Logger getConsole() {
        return this.console;
    }

    public int getSettingsWidth() {
        return this.settings_width;
    }

    public boolean shouldRun() {
        return this.shouldRun;
    }

    public boolean versionWarning() {
        return this.versionWarning;
    }

    public boolean protectClocks() {
        return this.protectClocks;
    }

    public boolean shouldGenerateSeparately() {
        return this.separately;
    }

    public long getGeneratorAccuracy() {
        return this.generatorAccuracy;
    }

    public boolean runStart() {
        return runStart;
    }
}
