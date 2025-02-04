package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import org.bukkit.entity.Player;

public class CommandReload implements ICommand {
    @Override
    public int getArgsSize() {
        return 1;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.reload";
    }

    @Override
    public boolean specialCondition(DigitalClock main, Player player, String[] args) {
        return false;
    }

    @Override
    public boolean checkClockExistence() {
        return false;
    }

    @Override
    public boolean neededClockExistenceValue() {
        return false;
    }

    @Override
    public String reactBadArgsSize(String usedCmd) {
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " reload'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
        return;
    }

    @Override
    public String reactBadClockList(String clockName) {
        return null;
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        main.reloadConf();
        main.reloadClocksConf();
        String s = "§2" + DigitalClock.getMessagePrefix() + "§a File config.yml has been reloaded!";
        if (player == null) {
            System.out.println(s);
        } else {
            player.sendMessage(s);
        }
    }
}
