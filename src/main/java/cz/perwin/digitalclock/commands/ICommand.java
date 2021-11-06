package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import org.bukkit.entity.Player;

public interface ICommand {
    int getArgsSize();

    String getPermissionName();

    boolean specialCondition(DigitalClock main, Player player, String[] args);

    boolean checkClockExistence();

    boolean neededClockExistenceValue();

    String reactBadArgsSize(String usedCmd);

    String reactNoPermissions();

    String reactBadClockList(String clockName);

    void process(DigitalClock main, Player player, String[] args);

    void specialConditionProcess(DigitalClock main, Player player, String[] args);
}
