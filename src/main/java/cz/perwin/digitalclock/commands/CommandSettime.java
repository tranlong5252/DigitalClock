package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandSettime implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.settime";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " settime <name> <HH:MM>'";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        String input = args[2];
        if (input.length() == 5
                && Character.isDigit(input.charAt(0))
                && Character.isDigit(input.charAt(1))
                && Character.isDigit(input.charAt(3))
                && Character.isDigit(input.charAt(4))
                && input.charAt(2) == ':') {
            int hours = Integer.parseInt(Character.toString(input.charAt(0))) * 10 + Integer.parseInt(Character.toString(input.charAt(1)));
            int mins = Integer.parseInt(Character.toString(input.charAt(3))) * 10 + Integer.parseInt(Character.toString(input.charAt(4)));
            if (hours >= 0 && hours < 24 && mins >= 0 && mins < 60) {
                Clock clock = Clock.loadClockByClockName(args[1]);
                int currentTimeInMinutes = Integer.parseInt(main.getGenerator().getRealNumbers(0, null)[0]) * 60 + Integer.parseInt(main.getGenerator().getRealNumbers(0, null)[1]);
                int newAddingMinutes = (currentTimeInMinutes - hours * 60 - mins) * -1;
                clock.addMinutes(newAddingMinutes);
                player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Time on clock '" + args[1] + " is now " + input + ". To set the time back to real time just use command 'addingminutes " + args[1] + " 0'.");
            } else {
                player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c HH must be integer from 00 to 23 and MM must be integer from 00 to 59!");
            }
        } else {
            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Time has incorrect format, it has to be HH:MM!");
        }
    }
}
