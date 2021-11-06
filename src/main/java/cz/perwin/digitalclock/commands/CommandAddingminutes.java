package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandAddingminutes implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.addingminutes";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return false;
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
        return "§4" + DigitalClock.getMessagePrefix() + " §cCorrect usage: §e'/" + usedCmd + " addingminutes <name> <minutes>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + " §cYou aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + " §cClock §e'" + clockName + "'§c not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        int mins = Integer.parseInt(args[2]);
        if (mins >= 0 && mins < 1440) {
            Clock clock = Clock.loadClockByClockName(args[1]);
            clock.addMinutes(mins);
            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + " §aClock §e'" + args[1] + "'§a now will add§e " + args[2] + " minutes.");
        } else {
            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + " §cMinutes can be only integer between 0 and 1439, not§e " + args[2] + "§c!");
        }
    }
}
