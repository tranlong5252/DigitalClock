package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandRotate implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.rotate";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return !args[2].equals("north") && !args[2].equals("south") && !args[2].equals("east") && !args[2].equals("west");
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " rotate <name> <direction>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Direction can be only north, south, east or west!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        Clock clock = Clock.loadClockByClockName(args[1]);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' rotated successfully from " + clock.getClockArea().getDirection().name().toLowerCase() + " to " + clock.getClockArea().rotate(args[2]).name().toLowerCase());
    }
}
