package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.entity.Player;

public class CommandToggleblinking implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.blinking";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " blinking <name>'";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Clock '" + clockName + "' not found!";
    }

    @Override
    public void process(DigitalClock main, Player player, String[] args) {
        Clock clock = Clock.loadClockByClockName(args[1]);
        if (clock.isBlinking()) {
            clock.setBlinking(false);
            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a You have successfully stopped blinking of colon on clock '" + args[1] + "'.");
        } else {
            clock.setBlinking(true);
            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a You have successfully started blinking of colon on clock '" + args[1] + "'.");
        }
    }
}
