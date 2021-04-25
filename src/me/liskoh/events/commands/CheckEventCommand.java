package me.liskoh.events.commands;

import me.liskoh.events.EventsPlugin;
import me.liskoh.events.enums.CataDay;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckEventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(! sender.hasPermission("cataevent.admin")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'exéctuer cette commande.");
            return false;
        }

        sender.sendMessage(EventsPlugin.INSTANCE.color("@7@m--------------------------------"));
        EventsPlugin.INSTANCE.getEvents().forEach(event -> {
            sender.sendMessage(EventsPlugin.INSTANCE.color("@eNom@7: @c" + event.getName() + " @eJour@7: @c" + CataDay.getDayById(event.getDayOfWeek())
                    + " @eHeure@7: @c" + event.getHourOfDay() + " @eMinute@7: @c" + event.getMinuteOfHour() + " @7(@c" + event.getCommands().toString() + "@7)"));
        });
        sender.sendMessage(EventsPlugin.INSTANCE.color("@7@m--------------------------------"));
        return true;
    }
}
