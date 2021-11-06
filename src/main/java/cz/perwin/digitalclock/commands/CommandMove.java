package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import org.bukkit.entity.Player;

public class CommandMove implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.move";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return main.getEnableMoveUsers().containsKey(player);
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " move <name>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        main.getEnableMoveUsers().remove(player);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Moving this clock has been rejected!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        main.getEnableMoveUsers().put(player, args[1]);
        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Moving clock '" + args[1] + "' has been enabled. Now just right click to some place to move your clock there.");
    }
}
