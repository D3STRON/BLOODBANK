package blood.com.bloodbank;

public class Components {
    public String value, cost;

    public Components(){

    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Components(String value, String cost) {

        this.value = value;
        this.cost = cost;
    }
}