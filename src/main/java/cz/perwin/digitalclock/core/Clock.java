package cz.perwin.digitalclock.core;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Clock {
    protected boolean showSeconds;
    protected boolean showHours;
    protected boolean ampm;
    private String clockName;
    private String clockCreator;
    private Material material;
    private Material fillingMaterial = Material.AIR;
    private boolean retrieveData;
    private int addMinutes = 0;
    private boolean blinking;
    private boolean blinkingChanger;
    private int countdownto;
    private ClockMode clockMode;
    private ClockArea clockArea;
    private int stopwatchtime;

    public Clock(String name, String playerName, Block block, Block playersBlock, int depth) {
        this.clockName = name;
        this.clockCreator = playerName;
        this.retrieveData = true;
        this.material = block.getType();
        this.showHours = true;
        this.clockMode = ClockMode.NORMAL;
        this.clockArea = new ClockArea(this, block, playersBlock, depth);
    }

    public static int stopTask(String clockName) {
        int task = Generator.getGenerator().getMain().getClockTasks().getByClockName(clockName);
        Generator.getGenerator().getMain().getServer().getScheduler().cancelTask(task);
        Generator.getGenerator().getMain().getClockTasks().removeByClockName(clockName);
        return task;
    }

    public static boolean isRunning(String clockName) {
        int task = Generator.getGenerator().getMain().getClockTasks().getByClockName(clockName);
        return Bukkit.getScheduler().isCurrentlyRunning(task);
    }

    public static void eraseCompletely(Clock clock) {
        if (Generator.getGenerator().getMain().getClockTasks().containsKeyByClockName(clock.getName())) {
            Clock.stopTask(clock.getName());
        }
        Generator.removeClockAndRestore(clock);
        clock.setRetrieveData(false);
        DigitalClock i = Generator.getGenerator().getMain();
        i.getClocksConf().set(clock.getName(), null);
        i.saveClocksConf();
        //Generator.getGenerator().getMain().saveConfig();
    }

    public static Clock loadClockByClockName(String clockName) {
        if (Generator.getGenerator().getMain().getClocksConf().getKeys(false).contains(clockName)) {
            Location loc = new Location(Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".x"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".y"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".z"));
            Location loc2 = new Location(Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".x2"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".y2"), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".z2"));
            return new Clock(clockName, Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".creator"), Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")).getBlockAt(loc), Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(clockName + ".world")).getBlockAt(loc2), Generator.getGenerator().getMain().getClocksConf().getInt(clockName + ".depth"));
        }
        return null;
    }

    public void writeAndGenerate() {
        if (!this.isSomethingMissing()) {
            this.write();
            Generator.getGenerator().generateOnce(this);
        } else {
            throw new NullPointerException("Missing data found when generating clock '" + this.clockName + "'!");
        }
    }

    public void write() {
        if (!this.isSomethingMissing()) {
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".creator", this.clockCreator);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".world", this.clockArea.getStartBlock().getWorld().getName());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".x", this.clockArea.getStartBlock().getX());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".y", this.clockArea.getStartBlock().getY());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".z", this.clockArea.getStartBlock().getZ());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".x2", this.clockArea.getPlayersBlock().getX());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".y2", this.clockArea.getPlayersBlock().getY());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".z2", this.clockArea.getPlayersBlock().getZ());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".depth", this.clockArea.getDepth());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".direction", this.clockArea.getDirection().name());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".material", this.material.name());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".filling", this.fillingMaterial.name());
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".add", this.addMinutes);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".seconds", this.showSeconds);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".hours", this.showHours);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".blinking", this.blinking);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".changer", this.blinkingChanger);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".ampm", this.ampm);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".cdt", this.countdownto);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".swt", this.stopwatchtime);
            Generator.getGenerator().getMain().getClocksConf().set(this.clockName + ".mode", this.clockMode.name());
            //Generator.getGenerator().getMain().saveConfig();
        } else {
            throw new NullPointerException("Missing data to write when saving the clock '" + this.clockName + "'!");
        }
    }

    public void teleportToClock(Player player) {
        player.teleport(this.getClockArea().getPlayersBlock().getLocation());
    }

    public Material getFillingMaterial() {
        this.reloadFromConfig();
        return this.fillingMaterial;
    }

    public ClockMode getClockMode() {
        this.reloadFromConfig();
        return this.clockMode;
    }

    public void enableIngameTime(boolean b) {
        Generator.removeClockAndRestore(this);
        this.clockMode = b ? ClockMode.INGAMETIME : ClockMode.NORMAL;
        this.write();
    }

    public Material setFillingMaterial(String name) {
        var optional = XMaterial.matchXMaterial(name);
        if (optional.isEmpty()) return null;
        this.fillingMaterial = optional.get().parseMaterial();
        this.write();
        return this.fillingMaterial;
    }

    public void reloadFromConfig() {
        if (Generator.getGenerator().getMain().getClocksConf().getKeys(false).contains(this.clockName) && this.retrieveData) {
            World w = Generator.getGenerator().getMain().getServer().getWorld(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".world"));
            this.clockArea = new ClockArea(this, new Location(w, Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".x"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".y"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".z")), new Location(w, Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".x2"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".y2"), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".z2")), BlockFace.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".direction")), Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".depth"));
            this.clockCreator = Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".creator");
            this.material = Material.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".material"));
            this.fillingMaterial = Material.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".filling"));
            this.addMinutes = Integer.parseInt(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".add"));
            this.showSeconds = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".seconds"));
            this.showHours = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".hours"));
            this.blinking = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".blinking"));
            this.blinkingChanger = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".changer"));
            this.ampm = Boolean.parseBoolean(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".ampm"));
            this.countdownto = Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".cdt");
            this.stopwatchtime = Generator.getGenerator().getMain().getClocksConf().getInt(this.clockName + ".swt");
            this.clockMode = ClockMode.valueOf(Generator.getGenerator().getMain().getClocksConf().getString(this.clockName + ".mode", "NORMAL"));
        }
    }

    public Material changeMaterial(String name) {
        var optional = XMaterial.matchXMaterial(name);
        if (optional.isEmpty()) return null;
        this.material = optional.get().parseMaterial();
        this.writeAndGenerate();
        return this.material;
    }

    public String getCreator() {
        this.reloadFromConfig();
        return this.clockCreator;
    }

    public void setCreator(String creator) {
        this.clockCreator = creator;
        this.write();
    }

    public void enableCountdown(boolean c) {
        this.reloadFromConfig();
        this.clockMode = c ? ClockMode.COUNTDOWN : ClockMode.NORMAL;
        this.write();
    }

    public void enableStopwatch(boolean s) {
        this.reloadFromConfig();
        this.clockMode = s ? ClockMode.STOPWATCH : ClockMode.NORMAL;
        this.write();
    }

    public int getCountdownTime() {
        this.reloadFromConfig();
        return this.countdownto;
    }

    public void setCountdownTime(int t) {
        this.reloadFromConfig();
        this.countdownto = t;
        this.write();
    }

    public int getStopwatchTime() {
        this.reloadFromConfig();
        return this.stopwatchtime;
    }

    public void setStopwatchTime(int t) {
        this.reloadFromConfig();
        this.stopwatchtime = t;
        this.write();
    }

    public void setShowingSeconds(boolean ss) {
        Generator.removeClockAndRestore(this);
        this.showSeconds = ss;
        this.write();
        ClockArea.resetDimensions(this);
    }

    public void setShowingHours(boolean hh) {
        Generator.removeClockAndRestore(this);
        this.showHours = hh;
        this.write();
        ClockArea.resetDimensions(this);
    }

    public boolean isBlinking() {
        this.reloadFromConfig();
        return this.blinking;
    }

    public void setBlinking(boolean bl) {
        this.blinking = bl;
        this.write();
    }

    public boolean getAMPM() {
        this.reloadFromConfig();
        return this.ampm;
    }

    public void setAMPM(boolean ap) {
        Generator.removeClockAndRestore(this);
        this.ampm = ap;
        this.write();
        ClockArea.resetDimensions(this);
    }

    protected void setBlinkingChanger(boolean blm) {
        this.blinkingChanger = blm;
        this.write();
    }

    protected boolean isBlinkingChangerON() {
        this.reloadFromConfig();
        return this.blinkingChanger;
    }

    public boolean shouldShowSeconds() {
        this.reloadFromConfig();
        return this.showSeconds;
    }

    public boolean shouldShowHours() {
        this.reloadFromConfig();
        return this.showHours;
    }

    public void addMinutes(int m) {
        this.reloadFromConfig();
        this.addMinutes = m;
        this.write();
    }

    public String getName() {
        this.reloadFromConfig();
        return this.clockName;
    }

    public void setName(String clockName) {
        this.clockName = clockName;
        this.write();
    }

    public Material getMaterial() {
        this.reloadFromConfig();
        return this.material;
    }

    public int getAddMinutes() {
        this.reloadFromConfig();
        return this.addMinutes;
    }

    public void updateClockArea(ClockArea ca) {
        this.clockArea = ca;
        this.write();
    }

    public ClockArea getClockArea() {
        this.reloadFromConfig();
        return this.clockArea;
    }

    protected void setRetrieveData(boolean retrieveData) {
        this.retrieveData = retrieveData;
    }

    protected boolean isSomethingMissing() {
        return this.clockCreator == null || this.clockName == null || this.clockArea == null || this.material == null || this.clockMode == null;
    }
}
