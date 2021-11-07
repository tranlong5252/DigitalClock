package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CommandFill extends MaterialCommand implements ICommand {
    @Override
    public int getArgsSize() {
        return 2;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.fill";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " fill <name>'";
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
        String oldmat = clock.getFillingMaterial().name().toLowerCase().replace("_", " ");
        Material m = player.getInventory().getItemInMainHand().getType();
        if (m.isSolid() || m.isAir()) {
            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' changed filling material from " + oldmat + " to " + clock.setFillingMaterial(m.name()).name().toLowerCase().replace("_", " "));
        } else {
            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't use " + m.name().toLowerCase().replace("_", " ") + " as a material, because it is not a block (cuboid shape)!");
        }
    }
}
