package id.co.sumi.transaku.model;

import java.io.Serializable;

public class InventoryModel implements Serializable {

    private InventoryCategoryModel category;
    private String code;
    private long customerId;
    private long id;
    private String description;
    private String itemCondition;
    private int isItemToSell;
    private String name;
    private String note;
    private double price;
    private double qty;
    private double weight;
    private QtyUnitModel qtyUnit;
    private int temp_total = 0;
    private double buyPrice;
    private String pictureBase64;
    private String picturePath;

    public InventoryModel(InventoryCategoryModel category, String code, long customerId, long id, String description, String itemCondition, int isItemToSell, String name, String note, double price, double qty, double weight, QtyUnitModel qtyUnit, double buyPrice) {
        this.category = category;
        this.code = code;
        this.customerId = customerId;
        this.id = id;
        this.description = description;
        this.itemCondition = itemCondition;
        this.isItemToSell = isItemToSell;
        this.name = name;
        this.note = note;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.qtyUnit = qtyUnit;
        this.buyPrice = buyPrice;
    }

    public InventoryModel(InventoryCategoryModel category, String code, long customerId, String description, String itemCondition, int isItemToSell, String name, String note, double price, double qty, double weight, QtyUnitModel qtyUnit, double buyPrice) {
        this.category = category;
        this.code = code;
        this.customerId = customerId;
        this.description = description;
        this.itemCondition = itemCondition;
        this.isItemToSell = isItemToSell;
        this.name = name;
        this.note = note;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.qtyUnit = qtyUnit;
        this.buyPrice = buyPrice;
    }


    public InventoryModel(InventoryCategoryModel category, String code, long customerId, long id, String description, String itemCondition, int isItemToSell, String name, String note, double price, double qty, double weight, QtyUnitModel qtyUnit) {
        this.category = category;
        this.code = code;
        this.customerId = customerId;
        this.id = id;
        this.description = description;
        this.itemCondition = itemCondition;
        this.isItemToSell = isItemToSell;
        this.name = name;
        this.note = note;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.qtyUnit = qtyUnit;
    }

    public InventoryModel(InventoryCategoryModel category, String code, long customerId, String description, String itemCondition, int isItemToSell, String name, String note, double price, double qty, double weight, QtyUnitModel qtyUnit) {
        this.category = category;
        this.code = code;
        this.customerId = customerId;
        this.description = description;
        this.itemCondition = itemCondition;
        this.isItemToSell = isItemToSell;
        this.name = name;
        this.note = note;
        this.price = price;
        this.qty = qty;
        this.weight = weight;
        this.qtyUnit = qtyUnit;
    }

    public int getTemp_total() {
        return temp_total;
    }

    public void setTemp_total(int temp_total) {
        this.temp_total = temp_total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InventoryCategoryModel getCategory() {
        return category;
    }

    public void setCategory(InventoryCategoryModel category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getIsItemToSell() {
        return isItemToSell;
    }

    public void setIsItemToSell(int isItemToSell) {
        this.isItemToSell = isItemToSell;
    }

    public QtyUnitModel getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(QtyUnitModel qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getPictureBase64() {
        return pictureBase64;
    }

    public void setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}