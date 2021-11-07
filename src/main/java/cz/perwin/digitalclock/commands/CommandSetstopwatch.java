package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import cz.perwin.digitalclock.core.ClockMode;
import org.bukkit.entity.Player;

public class CommandSetstopwatch implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.setstopwatch";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        ClockMode cm = Clock.loadClockByClockName(args[1]).getClockMode();
        return (cm == ClockMode.COUNTDOWN || cm == ClockMode.STOPWATCH) && Clock.isRunning(args[1]);
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " stopwatch <name> <seconds>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + args[1] + "' has already running! Stop it with command 'stopclock <name>'.");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        int secs = Integer.parseInt(args[2]);
        if (secs < 0) {
            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Seconds value mustn't be less than 0!");
        }
        Clock clock = Clock.loadClockByClockName(args[1]);
        clock.setStopwatchTime(secs);
        clock.enableStopwatch(true);
        if (main.getClockTasks().containsKeyByClockName(args[1])) {
            main.getServer().getScheduler().cancelTask(main.getClockTasks().getByClockName(args[1]));
            main.getClockTasks().removeByClockName(args[1]);
        }
        String[] num = main.getGenerator().getNumbersFromSeconds(clock.getStopwatchTime());
        String hours = num[0];
        String minutes = num[1];
        String seconds = num[2];
        main.getGenerator().generatingSequence(clock, hours, minutes, seconds, null);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a You have successfully set stopwatch time on clock '" + args[1] + "'. This clock is now stopped, run it by command 'runclock <name>'.");
    }
}
