package blood.com.bloodbank;

public class BankInfo {

    public String name, contact, email, location;

    public BankInfo(String name, String contact, String email, String location) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {

        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }
}