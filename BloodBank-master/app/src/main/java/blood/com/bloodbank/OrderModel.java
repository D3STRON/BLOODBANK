package blood.com.bloodbank;

public class OrderModel {

    public String seller;
    public String sellid;
    public String id;
    public String bloodgroup;
    public String componentname;
    public String amount;

    public OrderModel ()
    {

    }


    public String getSellid() {
        return sellid;
    }

    public void setSellid(String sellid) {
        this.sellid = sellid;
    }

    public OrderModel(String seller, String id, String bloodgroup, String componentname, String amount, String sellid) {
        this.seller = seller;
        this.sellid = sellid;

        this.id = id;
        this.bloodgroup = bloodgroup;
        this.componentname = componentname;
        this.amount = amount;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getComponentname() {
        return componentname;
    }

    public void setComponentname(String componentname) {
        this.componentname = componentname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
