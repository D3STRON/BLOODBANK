package blood.com.bloodbank;

/**
 * Created by shaik_000 on 16-03-2018.
 */

public class EventModel {

    public String bloddBankName, eventName, eventAddress, eventDescription, eventDate,eventId;

    public EventModel(){

    }

    public EventModel(String bloddBankName, String eventName, String eventAddress, String eventDescription, String eventDate, String eventId) {
        this.bloddBankName = bloddBankName;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventId = eventId;
    }

    public String getBloddBankName() {
        return bloddBankName;
    }

    public void setBloddBankName(String bloddBankName) {
        this.bloddBankName = bloddBankName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
