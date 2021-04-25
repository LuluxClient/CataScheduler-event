package me.liskoh.events.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.liskoh.events.EventsPlugin;

public class CataEventCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

		if (!sender.hasPermission("cataevent.admin")) {
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exéctuer cette commande.");
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "La commande est /cataevent reload");
			return false;
		}

		if (!args[0].equalsIgnoreCase("reload")) {
			sender.sendMessage(ChatColor.RED + "La commande est /cataevent reload");
			return false;
		}

		EventsPlugin.INSTANCE.getDefaultConfig().reloadConfig();
		EventsPlugin.INSTANCE.load();
		sender.sendMessage(ChatColor.YELLOW + "La configuration a été reload.");

		return true;
	}
}
