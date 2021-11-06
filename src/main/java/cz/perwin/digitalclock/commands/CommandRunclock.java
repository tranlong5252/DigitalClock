package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import org.bukkit.entity.Player;

public class CommandRunclock implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.runclock";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return main.getClockTasks().containsKeyByClockName(args[1]);
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " runclock <name>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + args[1] + "' is already running!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        main.run(args[1]);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Clock '" + args[1] + "' is now running under task number " + main.getClockTasks().getByClockName(args[1]) + ".");
    }
}
