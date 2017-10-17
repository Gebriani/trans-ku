package id.co.sumi.transaku.presenter;

/**
 * Created by alodokter on 15/05/17.
 */

public class PresenterFactory {

    public static AddInventoryPresenter addInventoryPresenter() {
        return new AddInventoryPresenter();
    }

    public static EditSellerProfilePresenter editSellerProfilePresenter() {
        return new EditSellerProfilePresenter();
    }

    public static GeneralPresenter generalPresenter() {
        return new GeneralPresenter();
    }

    public static InventoryPresenter inventoryPresenter() {
        return new InventoryPresenter();
    }

    public static LoginPresenter loginPresenter() {
        return new LoginPresenter();
    }

    public static POSPresenter posPresenter() {
        return new POSPresenter();
    }

    public static RegisterPresenter registerPresenter() {
        return new RegisterPresenter();
    }

    public static ReportSellPresenter reportSellPresenter() {
        return new ReportSellPresenter();
    }

    public static SearchSupplierPresenter searchSupplierPresenter() {
        return new SearchSupplierPresenter();
    }

    public static SellerProfilePresenter sellerProfilePresenter() {
        return new SellerProfilePresenter();
    }

    public static BookingPresenter bookingPresenter(){
        return new BookingPresenter();
    }

    public static SellPOSPresenter sellPOSPresenter() {
        return new SellPOSPresenter();
    }

    public static SupplierInventoryPresenter supplierInventoryPresenter() {
        return new SupplierInventoryPresenter();
    }

}