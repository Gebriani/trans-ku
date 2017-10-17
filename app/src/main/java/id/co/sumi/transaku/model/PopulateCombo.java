package id.co.sumi.transaku.model;

import java.io.Serializable;
import java.util.List;

/**
 * Transaku
 * Author Gebriani
 * on 12, March 2017.
 */

public class PopulateCombo implements Serializable {
    private List<ProvinceModel> provinces;
    private List<BusinessTypeModel> businessTypes;
    private List<InventoryCategoryModel> inventoryCategories;
    private List<QtyUnitModel> qtyUnits;

    public PopulateCombo(List<ProvinceModel> provinces, List<BusinessTypeModel> businessTypes, List<InventoryCategoryModel> inventoryCategories, List<QtyUnitModel> qtyUnits) {
        this.provinces = provinces;
        this.businessTypes = businessTypes;
        this.inventoryCategories = inventoryCategories;
        this.qtyUnits = qtyUnits;
    }

    public List<ProvinceModel> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceModel> provinces) {
        this.provinces = provinces;
    }

    public List<BusinessTypeModel> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<BusinessTypeModel> businessTypes) {
        this.businessTypes = businessTypes;
    }

    public List<QtyUnitModel> getQtyUnits() {
        return qtyUnits;
    }

    public void setQtyUnits(List<QtyUnitModel> qtyUnits) {
        this.qtyUnits = qtyUnits;
    }

    public List<InventoryCategoryModel> getInventoryCategories() {
        return inventoryCategories;
    }

    public void setInventoryCategories(List<InventoryCategoryModel> inventoryCategories) {
        this.inventoryCategories = inventoryCategories;
    }
}
