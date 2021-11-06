package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;
import cz.perwin.digitalclock.core.StopwatchEndEvent;
import org.bukkit.entity.Player;

public class CommandDisablestopwatch implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.disablestopwatch";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return Clock.loadClockByClockName(args[1]).getClockMode() != ClockMode.STOPWATCH;
    }

    @Override
    public boolean checkClockExistence() {
        return true;
    }

    @Override
    public boolean neededClockExistenceValue() {
        return true;
    }

    @Override
    public String reactBadArgsSize(String usedCmd) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " disablestopwatch <name>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c This clock hasn't enabled stopwatch mode!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        Clock clock = Clock.loadClockByClockName(args[1]);
        clock.enableStopwatch(false);
        main.getServer().getPluginManager().callEvent(new StopwatchEndEvent(clock, clock.getStopwatchTime()));
        if (main.getClockTasks().containsKeyByClockName(args[1])) {
            main.getServer().getScheduler().cancelTask(main.getClockTasks().getByClockName(args[1]));
            main.getClockTasks().removeByClockName(args[1]);
        }
        String hours = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[0];
        String minutes = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[1];
        String seconds = main.getGenerator().getRealNumbers(clock.getAddMinutes(), null)[2];
        main.getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a You have successfully disabled stopwatch mode on clock '" + args[1] + "'. This clock is now stopped, run it by command 'runclock <name>'.");
    }
}
