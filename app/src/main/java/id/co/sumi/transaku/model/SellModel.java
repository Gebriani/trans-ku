package id.co.sumi.transaku.model;

import java.io.Serializable;
import java.util.List;

public class SellModel implements Serializable {
    private List<SellDetailModel> sellDetails;
    private long id;
    private String code;
    private String createdDate;
    private String updatedDate;
    private UserModel seller;
    private String buyer;
    private double totalPrice;

//    private int qty;
//    private double price;
//    private int subTotal;


    public SellModel(List<SellDetailModel> sellDetailBeanList, UserModel seller, double totalPrice) {
        this.sellDetails = sellDetailBeanList;
        this.seller = seller;
        this.totalPrice = totalPrice;
    }

    public SellModel(List<SellDetailModel> sellDetailBeanList, long id, String code, String createdDate, String updatedDate, UserModel seller, String buyer, int totalPrice) {
        this.sellDetails = sellDetailBeanList;
        this.id = id;
        this.code = code;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.seller = seller;
        this.buyer = buyer;
        this.totalPrice = totalPrice;
    }

    public List<SellDetailModel> getSellDetails() {
        return sellDetails;
    }

    public void setSellDetails(List<SellDetailModel> sellDetails) {
        this.sellDetails = sellDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public UserModel getSeller() {
        return seller;
    }

    public void setSeller(UserModel seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
