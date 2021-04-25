package me.liskoh.events.taks;

import me.liskoh.events.EventsPlugin;

import java.util.Calendar;

public class CataEventTask implements Runnable {

    @Override
    public void run() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        EventsPlugin.INSTANCE.getEvents().forEach(event -> {
            if(event.canStart(day, hour, minute, second))
                event.perform();
        });
    }
}
