package me.liskoh.events.enums;

public enum CataDay {

    SUNDAY(1, "Dimanche"),
    MONDAY(2, "Lundi"),
    TUESDAY(3, "Mardi"),
    WEDNESDAY(4, "Mercredi"),
    THURSDAY(5, "Jeudi"),
    FRIDAY(6, "Vendredi"),
    SATURDAY(7, "Samedi");

    private int id;
    private String dayName;

    CataDay(int id, String dayName) {
        this.id = id;
        this.dayName = dayName;
    }

    public static String getDayById(int i) {
        for(CataDay value : values())
            if(i == value.getId())
                return value.getDayName();
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
