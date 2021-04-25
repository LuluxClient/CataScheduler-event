package me.liskoh.events;

import java.util.Date;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.liskoh.events.objects.CataEvent;
import me.liskoh.events.utils.TimerBuilder;

public class EventExpansion extends PlaceholderExpansion {

	private final Plugin plugin;

	/**
	 * @param plugin
	 */
	public EventExpansion(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public String getAuthor() {
		return "Maxlego08";
	}

	@Override
	public String getIdentifier() {
		return "nextevent";
	}

	@Override
	public String getVersion() {
		return this.plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String params) {

		EventsPlugin eventsPlugin = EventsPlugin.INSTANCE;

		Optional<CataEvent> optional = eventsPlugin.getNextEvent();

		if (params.equals("name"))
			return optional.isPresent() ? optional.get().getName() : "Impossible de trouver le prochain event";
		else if (params.equals("type"))
			return optional.isPresent() ? optional.get().getType() : "Impossible de trouver le prochain event";
		else if (params.equals("time")) {
			if (optional.isPresent()) {

				Date date = new Date();
				Date eventDate = optional.get().getDate();
				long diff = eventDate.getTime() - date.getTime();
				return TimerBuilder.getStringTime(diff / 1000);

			}
			return "Impossible de trouver le prochain event";
		} else {

			if (params.startsWith("name_")) {

				String type = params.replace("name_", "");
				optional = eventsPlugin.getNextEvent(type);
				return optional.isPresent() ? optional.get().getName() : "Impossible de trouver le prochain event";

			} else if (params.startsWith("time_")) {

				String type = params.replace("time_", "");
				optional = eventsPlugin.getNextEvent(type);
				if (optional.isPresent()) {
					Date date = new Date();
					Date eventDate = optional.get().getDate();
					
					long diff = eventDate.getTime() - date.getTime();
					return TimerBuilder.getStringTime(diff / 1000);
				}
				return "Impossible de trouver le prochain event";

			}

		}

		return null;
	}

}
