package me.liskoh.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.liskoh.events.commands.CataEventCommand;
import me.liskoh.events.commands.CheckEventCommand;
import me.liskoh.events.objects.CataEvent;
import me.liskoh.events.taks.CataEventTask;
import me.liskoh.events.utils.YmlMaker;

public class EventsPlugin extends JavaPlugin {

	private static final char DOT = '.';
	private static final String DAY_OF_WEEK = "day";
	private static final String HOUR_OF_DAY = "hour";
	private static final String MINUTE_OF_HOUR = "minute";
	private static final String COMMAND_TO_PERFORM = "commands";
	public static EventsPlugin INSTANCE;
	private List<CataEvent> events;
	private YmlMaker defaultConfig;

	@Override
	public void onEnable() {

		INSTANCE = this;

		this.events = new ArrayList<>();

		this.defaultConfig = new YmlMaker(this, "default_config.yml");
		this.defaultConfig.saveDefaultConfig();
		this.load();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CataEventTask(), 20L, 20L);

		this.getCommand("cataevent").setExecutor(new CataEventCommand());
		this.getCommand("checkevent").setExecutor(new CheckEventCommand());

		EventExpansion eventExpansion = new EventExpansion(this);
		eventExpansion.register();
	}

	@Override
	public void onDisable() {
	}

	public String color(String str) {
		return ChatColor.translateAlternateColorCodes('@', str);
	}

	public void load() {
		this.events.clear();
		final FileConfiguration config = this.defaultConfig.getConfig();
		final ConfigurationSection section = config.getConfigurationSection("cata_event");
		section.getKeys(false).forEach(key -> {
			final String path = section.getCurrentPath() + DOT + key;

			CataEvent day = new CataEvent(config.getString(path + DOT + "name"), config.getString(path + DOT + "type"),
					config.getString(path + DOT + DAY_OF_WEEK), config.getInt(path + DOT + HOUR_OF_DAY),
					config.getInt(path + DOT + MINUTE_OF_HOUR), config.getStringList(path + DOT + COMMAND_TO_PERFORM));

			events.add(day);
		});
	}

	public List<CataEvent> getEvents() {
		return events;
	}

	public Optional<CataEvent> getNextEvent() {
		return this.getNextEvent(null);
	}

	public Optional<CataEvent> getNextEvent(String type) {

		Date date = new Date();
		int step = 0;
		Optional<CataEvent> optional = getEvent(date, type);
		if (!optional.isPresent())
			while (step < 7 && !optional.isPresent()) {

				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				date = c.getTime();

				optional = getEvent(date, type);
				step++;
			}
		return optional;

	}

	@SuppressWarnings("deprecation")
	public Optional<CataEvent> getEvent(Date date, String type) {
		Calendar calendar = this.dateToCalendar(date);

		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		List<CataEvent> cataEvents = this.events.stream().filter(event -> {
			return event.getDayOfWeek() == day && (type == null || type.equals(event.getType()));
		}).collect(Collectors.toList());

		if (cataEvents.size() == 0)
			return Optional.empty();

		Date now = new Date();
		cataEvents = cataEvents.stream().filter(event -> {
			Calendar tmpCalendar = this.dateToCalendar(now);
			int tmpDay = tmpCalendar.get(Calendar.DAY_OF_WEEK);
			if (tmpDay == event.getDayOfWeek())
				return event.getHourOfDay() >= hour;
			return true;
		}).collect(Collectors.toList());

		if (cataEvents.size() == 0)
			return Optional.empty();

		cataEvents.forEach(e -> {
			Date date1 = new Date(date.getYear(), date.getMonth(), calendar.get(Calendar.DAY_OF_MONTH),
					e.getHourOfDay(), e.getMinuteOfHour(), 0);
			e.setDate(date1);
		});

		Collections.sort(cataEvents, new Comparator<CataEvent>() {

			@Override
			public int compare(CataEvent cataEvent, CataEvent cataEvent2) {
				Date date1 = new Date(date.getYear(), date.getMonth(), date.getDay(), cataEvent.getHourOfDay(),
						cataEvent.getMinuteOfHour(), 0);
				Date date2 = new Date(date.getYear(), date.getMonth(), date.getDay(), cataEvent2.getHourOfDay(),
						cataEvent2.getMinuteOfHour(), 0);
				return date2.compareTo(date1);
			}
		}.reversed());

		return Optional.of(cataEvents.get(0));

	}

	// Convert Date to Calendar
	private Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}

	public YmlMaker getDefaultConfig() {
		return defaultConfig;
	}

	public void setDefaultConfig(YmlMaker defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
}
