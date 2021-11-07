package cz.perwin.digitalclock.commands;

import org.bukkit.entity.Player;

public class MaterialCommand {

    protected boolean isValidName(String s) {
        return s.matches("[a-z]|[A-Z]|[_]?");
    }

	protected boolean isPermitted(Player player, String[] args) {
		String command = args[0];
		String arg = args[2].contains(":") ? args[2].substring(0, args[2].indexOf(':')) : args[2];
        return player.hasPermission("digitalclock." + command + ".*") || player.hasPermission("digitalclock." + command + "." + arg) || player.isOp();

    }

    //useless because we will use item on main hand
}
