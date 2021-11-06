package cz.perwin.digitalclock.core;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import cz.perwin.digitalclock.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.DigitalClock;

public class Commands implements CommandExecutor {
	private final DigitalClock i;
	public static final HashMap<String, Class<?>> commandList = new HashMap<>();
	
	static {
		commandList.put("create", CommandCreate.class);
		commandList.put("remove", CommandRemove.class);
		commandList.put("delete", CommandRemove.class);
		commandList.put("rotate", CommandRotate.class);
		commandList.put("material", CommandMaterial.class);
		commandList.put("fill", CommandFill.class);
		commandList.put("move", CommandMove.class);
		commandList.put("addingminutes", CommandAddingminutes.class);
		commandList.put("tp", CommandTP.class);
		commandList.put("stopclock", CommandStopclock.class);
		commandList.put("runclock", CommandRunclock.class);
		commandList.put("toggleseconds", CommandToggleseconds.class);
		commandList.put("toggleingametime", CommandToggleingametime.class);
		commandList.put("toggleampm", CommandToggleampm.class);
		commandList.put("toggleblinking", CommandToggleblinking.class);
		commandList.put("setcountdown", CommandSetcountdown.class);
		commandList.put("disablecountdown", CommandDisablecountdown.class);
		commandList.put("setstopwatch", CommandSetstopwatch.class);
		commandList.put("disablestopwatch", CommandDisablestopwatch.class);
		commandList.put("depth", CommandDepth.class);
		commandList.put("list", CommandList.class);
		commandList.put("reload", CommandReload.class);
		commandList.put("settime", CommandSettime.class);
		commandList.put("update", CommandUpdate.class);
		commandList.put("off", CommandOff.class);
		commandList.put("help", CommandHelp.class);
		commandList.put("?", CommandHelp.class);
		commandList.put("hours", CommandTogglehours.class);
	}
	
	public Commands(DigitalClock i) {
		this.i = i;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("digitalclock") || command.getName().equalsIgnoreCase("dc")) {
			String usedcmd = command.getName().toLowerCase();
			if(args.length > 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					if(commandList.containsKey(args[0].toLowerCase())) {
						ICommand ic = null;
						try {
							Class<?> clazz = Commands.class.getClassLoader().loadClass(commandList.get(args[0].toLowerCase()).getName());
							ic = (ICommand) clazz.getDeclaredConstructor().newInstance();
						} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
							System.err.println("Problem occured when processing command: " + e.getMessage());
							//e.printStackTrace();
						} finally {
							if(ic != null) {
								this.processCommand(usedcmd, player, args, ic);
							}
						}
					} else {
						player.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c This argument doesn't exist. Show '/"+ usedcmd + " help' for more info.");
					}
        		} else {
        			switch(args[0].toLowerCase()) {
        			case "reload":
    					this.processCommand(usedcmd, null, args, new CommandReload());
        				break;
        			case "update":
    					this.processCommand(usedcmd, null, args, new CommandUpdate());
        				break;
        			case "off":
    					this.processCommand(usedcmd, null, args, new CommandOff());
        				break;
        			default:
        				sender.sendMessage("§4" + DigitalClock.getMessagePrefix() + "§c This command can be executed only from the game!");
        				break;
        			}
        		}
			} else {
				sender.sendMessage("§a---- DigitalClock v"+ this.i.getDescription().getVersion() +" ----\nAuthor: PerwinCZ\nRun command '/"+ usedcmd + " help' in game for commands list.");
			}
			return true;
		} 
		return false;
	}
	
	private void processCommand(String usedcmd, Player player, String[] args, ICommand ic) {
		if(args.length != ic.getArgsSize()) {
			player.sendMessage(ic.reactBadArgsSize(usedcmd));
		} else if(player != null && !player.hasPermission(ic.getPermissionName()) && !player.isOp()) {
			player.sendMessage(ic.reactNoPermissions());
		} else if(ic.checkClockExistence() && i.getClocksConf().getKeys(false).contains(args[1]) != ic.neededClockExistenceValue()) {
			player.sendMessage(ic.reactBadClockList(args[1]));
		} else if(ic.specialCondition(i, player, args)) {
			ic.specialConditionProcess(i, player, args);
		} else {
			ic.process(i, player, args);
		}
	}
}
