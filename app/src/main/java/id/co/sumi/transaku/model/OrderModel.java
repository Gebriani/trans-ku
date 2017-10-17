package id.co.sumi.transaku.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gebriani on 7/23/17.
 */

public class OrderModel implements Serializable {
    private String code;
    private UserModel customer;
    private long id;
    private List<SellDetailModel> orderDetails;
    private SupplierModel supplier;
    private double totalPrice;
    private int status;
    private String createdDate;
    private String updatedDate;

    public OrderModel() {
    }

    public OrderModel(List<SellDetailModel> orderDetails, UserModel customer, SupplierModel supplier, double totalPrice) {
        this.customer = customer;
        this.orderDetails = orderDetails;
        this.supplier = supplier;
        this.totalPrice = totalPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserModel customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<SellDetailModel> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<SellDetailModel> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public SupplierModel getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierModel supplier) {
        this.supplier = supplier;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
