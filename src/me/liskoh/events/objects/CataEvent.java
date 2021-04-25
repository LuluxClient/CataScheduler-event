package me.liskoh.events.objects;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;

import me.liskoh.events.enums.CataDay;

public class CataEvent {

	private String name;
	private String type;
	private int dayOfWeek, hourOfDay, minuteOfHour;
	private List<String> commands;
	private boolean valid;
	private Date date;

	public CataEvent(String name, String type, String dayOfWeek, int hourOfDay, int minuteOfHour,
			List<String> commands) {
		this.name = name;
		this.type = type;
		this.dayOfWeek = this.getDay(dayOfWeek);
		this.hourOfDay = hourOfDay;
		this.minuteOfHour = minuteOfHour;
		this.commands = commands;
		this.valid = canBeCreate();
	}

	public boolean canStart(int day, int hour, int minute, int second) {
		return this.dayOfWeek == day && this.hourOfDay == hour && minute == this.minuteOfHour && second == 0;
	}

	public void perform() {
		this.commands.forEach(cmd -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
		});
	}

	public int getDay(String dayName) {
		for (CataDay value : CataDay.values())
			if (value.getDayName().equalsIgnoreCase(dayName))
				return value.getId();
		return -1;
	}

	public String getType() {
		return type;
	}

	public boolean canBeCreate() {

		if (dayOfWeek == -1)
			return false;

		if (hourOfDay < 0 || hourOfDay > 60)
			return false;

		if (minuteOfHour < 0 || minuteOfHour > 60)
			return false;

		return true;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean b) {
		this.valid = b;
	}

	public String getName() {
		return name;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public int getMinuteOfHour() {
		return minuteOfHour;
	}

	public List<String> getCommands() {
		return commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CataEvent [name=" + name + ", type=" + type + ", dayOfWeek=" + dayOfWeek + ", hourOfDay=" + hourOfDay
				+ ", minuteOfHour=" + minuteOfHour + ", commands=" + commands + ", valid=" + valid + "]";
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

}
