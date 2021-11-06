package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Commands;
import org.bukkit.entity.Player;

public class CommandHelp implements ICommand {
    @Override
    public int getArgsSize() {
        return 1;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.help";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " help'";
    }

    @Override
    public String reactNoPermissions() {
        return "§4" + DigitalClock.getMessagePrefix() + "§c You aren't allowed to use this command!";
    }

    @Override
    public void specialConditionProcess(DigitalClock main, Player player, String[] args) {
    }

    @Override
    public String reactBadClockList(String clockName) {
        return null;
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        StringBuilder s = new StringBuilder("§2" + DigitalClock.getMessagePrefix() + "§a All possible arguments:\n");
        s.append("§a/dc ");
        int i = 0;
        for (String cmd : Commands.commandList.keySet()) {
            if (i == Commands.commandList.size() - 2) {
                s.append("§2").append(cmd).append("§a and ");
            } else if (i == Commands.commandList.size() - 1) {
                s.append("§2").append(cmd).append("§a.\n");
            } else {
                s.append("§2").append(cmd).append("§a, ");
            }
            i++;
        }
        s.append("You can find more information on\n§b http://dev.bukkit.org/server-mods/digitalclock");
        if (player == null) {
            System.out.println(s);
        } else {
            player.sendMessage(s.toString());
        }
    }
}
