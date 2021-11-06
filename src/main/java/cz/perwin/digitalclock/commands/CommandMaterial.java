package cz.perwin.digitalclock.commands;

import cz.perwin.digitalclock.DigitalClock;
import cz.perwin.digitalclock.XMaterial;
import cz.perwin.digitalclock.core.Clock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMaterial extends MaterialCommand implements ICommand {
    @Override
    public int getArgsSize() {
        return 3;
    }

    @Override
    public String getPermissionName() {
        return "digitalclock.material";
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
        return "§4" + DigitalClock.getMessagePrefix() + "§c Correct usage: '/" + usedCmd + " material <name> <material id:data>'";
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

    @SuppressWarnings("deprecation")
    @Override
    //todo change to 1.13+ block
    public void process(DigitalClock main, Player player, String[] args) {
        Clock clock = Clock.loadClockByClockName(args[1]);
        String oldmat = clock.getMaterial().name().toLowerCase().replace("_", " ");
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (super.isValidName(hand.getType().toString())) {
            Material m = hand.getType();
            //Material.getMaterial();
            if (m.isSolid()) {
                player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' changed material from " + oldmat + " to " + clock.changeMaterial(m.name(), 0).name().toLowerCase().replace("_", " "));
            } else {
                player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't use " + m.name().toLowerCase().replace("_", " ") + " as a material, because it is not a block (cuboid shape)!");
            }
        } else {
            if (args[2].contains(":")) {
                String[] matdata = args[2].split(":");
                if (super.isValidName(matdata[1])) {
                    if (super.isValidName(matdata[0])) {
                        var optional = XMaterial.matchXMaterial(matdata[0]);
                        if (optional.isEmpty()) return;
                        Material m = optional.get().parseMaterial();
                        if (m.isSolid()) {
                            player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' changed material from " + oldmat + " to " + clock.changeMaterial(m.name(), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
                        } else {
                            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't use " + m.name().toLowerCase().replace("_", " ") + " as a material, because it is not a block (cuboid shape)!");
                        }
                    } else {
                        try {
                            var optional = XMaterial.matchXMaterial(matdata[0]);
                            if (optional.isEmpty()) return;
                            Material m = optional.get().parseMaterial();
                            if (m.isSolid()) {
                                player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' changed material from " + oldmat + " to " + clock.changeMaterial(m.name(), Integer.parseInt(matdata[1])).name().toLowerCase().replace("_", " "));
                            } else {
                                player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't use " + m.name().toLowerCase().replace("_", " ") + " as a material, because it is not a block (cuboid shape)!");
                            }
                        } catch (IllegalArgumentException e) {
                            player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Material '" + matdata[0] + "' does not exist!");
                        }
                    }
                } else {
                    player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Material data must be positive integer!");
                }
            } else {
                try {
                    var optional = XMaterial.matchXMaterial(args[2]);
                    if (optional.isEmpty()) return;
                    Material m = optional.get().parseMaterial();
                    if (m.isSolid()) {
                        player.sendMessage("§2" + DigitalClock.getMessagePrefix() + "§a Your clock '" + args[1] + "' changed material from " + oldmat + " to " + clock.changeMaterial(m.name(), 0).name().toLowerCase().replace("_", " "));
                    } else {
                        player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c You can't use " + m.name().toLowerCase().replace("_", " ") + " as a material, because it is not a block (cuboid shape)!");
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c Material '" + args[2] + "' does not exist!");
                }
            }
        }
    }
}
