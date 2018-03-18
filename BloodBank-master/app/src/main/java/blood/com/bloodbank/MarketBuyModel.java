package blood.com.bloodbank;


public class MarketBuyModel {

    public String id;
    public String amount;
    public String location;
    public String latitude;
    public String longitude;
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

    public String distance;
    public String name;
    public String seller;

    public String sellid;

    public String getSellid() {
        return sellid;
    }

    public void setSellid(String sellid) {
        this.sellid = sellid;
    }

    public MarketBuyModel(String id, String amount, String location, String name, String seller, String sellid) {
        this.id = id;
        this.amount = amount;
        this.location = location;
        this.name = name;
        this.seller = seller;
        this.sellid = sellid;
    }

    public MarketBuyModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
