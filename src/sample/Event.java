package sample;

public class Event {
    private String EventID;
    private String Description;
    private String EventDate;
    private String Theme;
    private String LocationID;
    private boolean Daytime;

    public Event() {}

    public Event(String eventID, String description, String eventDate, String theme, String locationID, boolean daytime) {
        this.EventID = eventID;
        this.Description = description;
        this.EventDate = eventDate;
        this.Daytime = daytime;
        this.Theme = theme;
        this.LocationID = locationID;
    }

    public String getEventID() {
        return EventID;
    }

    public String getDescription() {
        return Description;
    }

    public String getEventDate() {
        return EventDate;
    }

    public boolean isDaytime() {
        return Daytime;
    }

    public String getTheme() {
        return Theme;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public void setDaytime(boolean daytime) {
        Daytime = daytime;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    @Override
    public String toString() {
        return "Event{" +
                "EventID='" + EventID + '\'' +
                ", Description='" + Description + '\'' +
                ", EventDate='" + EventDate + '\'' +
                ", Daytime=" + Daytime +
                ", Theme='" + Theme + '\'' +
                ", LocationID='" + LocationID + '\'' +
                '}';
    }
}
