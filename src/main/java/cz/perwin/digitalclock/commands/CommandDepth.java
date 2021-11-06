package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandDepth implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.depth";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        try {
            int i = Integer.parseInt(args[2]);
            return i <= 0;
        } catch (Exception e) {
            return true;
        }
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " depth <name> <depth>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Depth must be positive integer bigger than 0!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        Clock clock = Clock.loadClockByClockName(args[1]);
        int de = Integer.parseInt(args[2]);
        clock.getClockArea().setDepth(de);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock §e'" + args[1] + "'§a changed depth to " + de + " blocks.");
    }
}
