package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandList implements ICommand {
    @Override
    public int getArgsSize() {
        return 1;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.list";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " list'";
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
        player.sendMessage("§b" + DigitalClock.getMessagePrefix() + "§a List of all existing clocks:");
        StringBuilder list = new StringBuilder();
        int i = 0;
        if (main.getClocksL().size() != 0) {
            for (String name : main.getClocksL()) {
                Clock clock = Clock.loadClockByClockName(name);
                list.append(clock.getName());
                if (i != main.getClocksL().size() - 1) {
                    list.append(", ");
                }
                i++;
            }
        } else {
            list = new StringBuilder("§oNo clocks found!");
        }
        player.sendMessage("§a" + list);
    }
}
