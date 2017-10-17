package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.InventoryModel;
import id.co.sumi.transaku.model.OrderModel;
import id.co.sumi.transaku.model.SellDetailModel;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.model.SupplierModel;
import id.co.sumi.transaku.model.UserModel;
import id.co.sumi.transaku.presenter.BookingMvpView;
import id.co.sumi.transaku.presenter.BookingPresenter;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SellPOSMvpView;
import id.co.sumi.transaku.presenter.SellPOSPresenter;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.PrefHelper;
import id.co.sumi.transaku.view.adapters.POSResultAdapter;

/**
 * Created by gebriani on 7/4/17.
 */

public class BookingConfirmationActivity extends ParentActivity implements View.OnClickListener, BookingMvpView {

    private static String RESULT_INVENTORY = "result_inventory";
    private static String RESULT_ALL_INVENTORY = "result_all_inventory";
    private static String RESULT_TOTAL_HARGA = "result_total_harga";
    private static String RESULT_TOTAL_INVENTORY = "result_total_inventory";
    private static String RESULT_SUPPLIER = "result_supplier";

    private static String PAGE_FROM = "page_from";

    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView rvResult;
    private Button btOrder;
    private static TextView tvTotalHargaBarang;
    private TextView tvPajakdanService;
    private TextView tvPotonganHarga;
    private static TextView tvTotalHarga;
    private List<InventoryModel> listInventoryResult;
    private List<SellDetailModel> listSellDetail = new ArrayList<>();
    private double totalHarga;
    private int totalInventory;
    private POSResultAdapter adapter;
    private SellModel sellModel;
    private OrderModel orderModel;
    private static double total_price_end;
    private BookingPresenter bookingPresenter;
    private int pageFrom;

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        rvResult = (RecyclerView) findViewById(R.id.pos_result_recycleview);
        rvResult.setLayoutManager(new LinearLayoutManager(rvResult.getContext()));
        btOrder = (Button) findViewById(R.id.pos_result_bt_bayar);
        findViewById(R.id.pos_result_bt_batal).setOnClickListener(this);
        btOrder.setOnClickListener(this);
        tvTotalHargaBarang = (TextView) findViewById(R.id.item_total_totalHargaBarang);
        tvPajakdanService = (TextView) findViewById(R.id.item_total_pajakdanService);
        tvPotonganHarga = (TextView) findViewById(R.id.item_total_potonganHarga);
        tvTotalHarga = (TextView) findViewById(R.id.item_total_totalHarga);

        pageFrom = getIntent().getIntExtra(PAGE_FROM, 0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("Konfirmasi Pemesanan");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bookingPresenter = PresenterFactory.bookingPresenter();
        bookingPresenter.attachView(this);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_result);
        findView();
        listInventoryResult = Arrays.asList(TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(RESULT_INVENTORY), InventoryModel[].class));
        totalHarga = getIntent().getDoubleExtra(RESULT_TOTAL_HARGA, 0);
        totalInventory = getIntent().getIntExtra(RESULT_TOTAL_INVENTORY, 0);

        for (InventoryModel temp : listInventoryResult) {
            double subTotal = temp.getTemp_total() * temp.getPrice();
            SellDetailModel selltemp = new SellDetailModel(temp, temp.getTemp_total(), temp.getPrice(), subTotal);
            listSellDetail.add(selltemp);
        }

        adapter = new POSResultAdapter(this, listSellDetail, totalHarga);
        rvResult.setAdapter(adapter);
        UserModel userModel = new UserModel(Long.parseLong(PrefHelper.getString(Const.ID)));
        total_price_end = totalHarga;

        if(pageFrom == 0){
            btOrder.setText(getResources().getString(R.string.bayar_label));
            sellModel = new SellModel(listSellDetail, userModel, totalHarga);
        } else {
            btOrder.setText(getResources().getString(R.string.pesan_label));
            SupplierModel supplierModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(RESULT_SUPPLIER), SupplierModel.class);
            orderModel = new OrderModel(listSellDetail, userModel,supplierModel, totalHarga);
        }
        tvTotalHargaBarang.setText(CommonFunction.convertCurrencyFormat(totalHarga));
        tvPajakdanService.setText(CommonFunction.convertCurrencyFormat(0.0));
        tvPotonganHarga.setText(CommonFunction.convertCurrencyFormat(0.0));
        tvTotalHarga.setText(CommonFunction.convertCurrencyFormat(totalHarga));
    }


    public static void showActivity(Context context, int pageFrom, String invResult, String allInvent, double totalHarga, int totalInventory, String supplier) {
        Intent intent = new Intent(context, BookingConfirmationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_FROM, pageFrom);
        bundle.putString(RESULT_INVENTORY, invResult);
        bundle.putString(RESULT_ALL_INVENTORY, allInvent);
        bundle.putDouble(RESULT_TOTAL_HARGA, totalHarga);
        bundle.putInt(RESULT_TOTAL_INVENTORY, totalInventory);
        bundle.putString(RESULT_SUPPLIER, supplier);
        intent.putExtras(bundle);
        context.startActivity(intent);
//        context.overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pos_result_bt_batal:
                onBackPressed();
                break;
            case R.id.pos_result_bt_bayar:
                if (adapter.getTotalResult() != 0) {
                    switch (pageFrom){
                        case 0:
                            dialogPay(total_price_end);
                            break;
                        case 1:
                            dialogOrder();
                            break;
                    }
                }
                break;
        }
    }

    public static void editTotalHargaBarang(Double totalprice) {
        tvTotalHargaBarang.setText(CommonFunction.convertCurrencyFormat(totalprice));
        tvTotalHarga.setText(CommonFunction.convertCurrencyFormat(totalprice));
        total_price_end = totalprice;
    }

    private void dialogOrder(){
        AlertDialog alertDialog = new AlertDialog.Builder(BookingConfirmationActivity.this).create();
        alertDialog.setMessage("Apakah anda yakin untuk melakukan pemesanan ?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderModel.setId(0);
                orderModel.setOrderDetails(adapter.getSellDetailModelList());
                orderModel.setTotalPrice(total_price_end);
                bookingPresenter.sendOrderPOS(orderModel);
                Log.d("ORDERMODEL", TransakuApplication.getInstance().getGson().toJson(orderModel) + " ");
            }
        });
        alertDialog.show();
    }

    private void dialogPay(final double totalpay){
        LayoutInflater inflater = LayoutInflater.from(BookingConfirmationActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_pay, null);
        final EditText user_pay = (EditText) dialogView.findViewById(R.id.dialog_pay_et_user_money);
        Button cancel_button = (Button) dialogView.findViewById(R.id.dialog_pay_cancel_button);
        Button submit_button = (Button) dialogView.findViewById(R.id.dialog_pay_submit_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_pay.getText().toString().trim().isEmpty() && Double.parseDouble(user_pay.getText().toString()) >= totalpay) {
                    alertDialog.dismiss();
//                    double pay_return = Double.parseDouble(user_pay.getText().toString()) - totalpay;
//                    Toast.makeText(DetailPOSResultActivity.this, "Jumlah kembalian : " + pay_return, Toast.LENGTH_LONG).show();
                    double pay_return = Double.parseDouble(user_pay.getText().toString()) - total_price_end;
                    Toast.makeText(BookingConfirmationActivity.this, "Jumlah kembalian : "+CommonFunction.convertCurrencyFormat(pay_return), Toast.LENGTH_LONG).show();
                    sellModel.setId(0);
                    sellModel.setSellDetails(adapter.getSellDetailModelList());
                    sellModel.setTotalPrice(total_price_end);
                    bookingPresenter.sendSellPOS(sellModel);
                    Log.d("SELLMODEL", TransakuApplication.getInstance().getGson().toJson(adapter.getSellDetailModelList()) + " ");
                    Log.d("SELLMODEL", total_price_end + " ");

                } else if (user_pay.getText().toString().trim().isEmpty()) {
                    user_pay.setError("Tidak boleh kosong");
                } else if (Double.parseDouble(user_pay.getText().toString()) < totalpay) {
                    user_pay.setError("Pembayaran lebih kecil daripada total harga barang");
                }
            }
        });

        alertDialog.show();
    }

    @Override
    public void responseOrder(boolean isSuceess, String message) {
        if (isSuceess) {
            Toast.makeText(this, "Konfirmasi Pemesanan Berhasil", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Konfirmasi Pemesanan gagal " + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void responseBuy(boolean isSuceess, String message) {
        if (isSuceess) {
            Toast.makeText(this, "Konfirmasi Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Konfirmasi Perbayaran gagal " + message, Toast.LENGTH_SHORT).show();
        }
    }
}
