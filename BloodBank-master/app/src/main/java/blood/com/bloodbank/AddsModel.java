package blood.com.bloodbank;

/**
 * Created by Anurag on 15-03-2018.
 */

public class AddsModel {
    public String buyer;
    public String id;
    public String bloodgroup;
    public String componentname;
    public String maxamount;
    public String priceperunit;

    public String getBuyid() {
        return buyid;
    }

    public void setBuyid(String buyid) {
        this.buyid = buyid;
    }

    public String buyid;

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public String getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(String maxamount) {
        this.maxamount = maxamount;
    }

    public String getPriceperunit() {
        return priceperunit;
    }

    public void setPriceperunit(String priceperunit) {
        this.priceperunit = priceperunit;
    }
}
