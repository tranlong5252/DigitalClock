package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import org.bukkit.entity.Player;

public class CommandCreate implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.create";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return main.getEnableBuildUsers().containsKey(player);
    }

    @Override
    public boolean checkClockExistence() {
        return true;
    }

    @Override
    public boolean neededClockExistenceValue() {
        return false;
    }

    @Override
    public String reactBadArgsSize(String usedCmd) {
        return "§4" + DigitalClock.getMessagePrefix() + " §cCorrect usage: §e'/" + usedCmd + " create <name>'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + " §cYou aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You are just creating another clock. You can't create more clocks in the same time!");
    }

    @Override
    public String reactBadClockList(String clockName) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock with this name already exists!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        int count = 0;
        if (main.getUsersClock().get(player.getName()) != null) {
            count = main.getUsersClock().get(player.getName());
        }
        boolean limitReached = player.hasPermission("digitalclock.limit." + count) && count != 0 && !player.isOp() && !player.hasPermission("digitalclock.limit.*");
        if (!limitReached) {
            main.getEnableBuildUsers().put(player, args[1]);
            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + " §aNow you can create your " + (count + 1) + ". clock. Place any block anywhere to set start block. Click with empty hand to end creating.");
        } else {
            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't create next clock. You have reached the limit of " + count + " clocks.");
        }
    }
}
