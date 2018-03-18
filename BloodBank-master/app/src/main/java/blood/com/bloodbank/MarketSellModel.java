package blood.com.bloodbank;

/**
 * Created by Anurag on 15-03-2018.
 */

public class MarketSellModel implements  java.io.Serializable{
    public String buyer;
    public String id;
    public String name;
    public String location;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String priceperunit;
    public String maxamount;
    public String buyid;
    public String latitude;
    public String longitude;
    public String distance;

    public String getBuyid() {
        return buyid;
    }

    public void setBuyid(String buyid) {
        this.buyid = buyid;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPriceperunit() {
        return priceperunit;
    }

    public void setPriceperunit(String priceperunit) {
        this.priceperunit = priceperunit;
    }

    public String getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(String maxamount) {
        this.maxamount = maxamount;
    }
}
