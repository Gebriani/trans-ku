package id.co.sumi.transaku.model;

import java.io.Serializable;

public class SellDetailModel implements Serializable {
    private InventoryModel inventory;
    private long id;
    private double qty;
    private double price;
    private double subTotal;

    public SellDetailModel(InventoryModel inventory, long id, double qty, double price, double subTotal) {
        this.inventory = inventory;
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.subTotal = subTotal;
    }

    public SellDetailModel(InventoryModel inventory, double qty, double price, double subTotal) {
        this.inventory = inventory;
        this.qty = qty;
        this.price = price;
        this.subTotal = subTotal;
    }

    public InventoryModel getInventory() {
        return inventory;
    }

    public void setInventory(InventoryModel inventory) {
        this.inventory = inventory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
