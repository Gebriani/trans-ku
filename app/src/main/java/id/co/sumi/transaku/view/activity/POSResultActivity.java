package id.co.sumi.transaku.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.TransakuApplication;
import id.co.sumi.transaku.model.SellModel;
import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.presenter.PresenterFactory;
import id.co.sumi.transaku.presenter.SellPOSMvpView;
import id.co.sumi.transaku.presenter.SellPOSPresenter;
import id.co.sumi.transaku.utils.CommonFunction;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.view.adapters.POSResultAdapter;


public class POSResultActivity extends ParentActivity implements View.OnClickListener, SellPOSMvpView {

    private static String RESULT_TOTAL_HARGA = "result_total_harga";
    private static String RESULT_SELL_MODEL = "result_sell_model";
    private static String RESULT_INVENTORY = "result_inventory";
    private static String RESULT_TOTAL_POS = "result_total_pos";

    private Toolbar toolbar;
    private TextView toolbar_title;
    private static TextView tv_total_harga_barang;
    private TextView tv_pajak_dan_service;
    private TextView tv_potongan_harga;
    private static TextView tv_total_harga;
    private Button bt_batal;
    private Button bt_bayar;
    private SellModel sellModel;
    private RecyclerView rv_result_inventory;
    private double totalprice;
    private POSResultAdapter adapter;
    private SellPOSPresenter presenter;
    private static double total_price_end;
    private int totalPOS;
    private String tempInventory;

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        rv_result_inventory = (RecyclerView) findViewById(R.id.pos_result_recycleview);
        rv_result_inventory.setLayoutManager(new LinearLayoutManager(rv_result_inventory.getContext()));

        tv_pajak_dan_service = (TextView) findViewById(R.id.item_total_pajakdanService);
        tv_potongan_harga = (TextView) findViewById(R.id.item_total_potonganHarga);
        tv_total_harga = (TextView) findViewById(R.id.item_total_totalHarga);
        tv_total_harga_barang = (TextView) findViewById(R.id.item_total_totalHargaBarang);
//        tv_total_price = (TextView) findViewById(R.id.pos_total_price);
        bt_batal = (Button) findViewById(R.id.pos_result_bt_batal);
        bt_bayar = (Button) findViewById(R.id.pos_result_bt_bayar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
        getSupportActionBar().setTitle("");
        toolbar_title.setText("POS");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bt_batal.setOnClickListener(this);
        bt_bayar.setOnClickListener(this);

        presenter = PresenterFactory.sellPOSPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_result);
        findView();
        totalprice = getIntent().getDoubleExtra(RESULT_TOTAL_HARGA, 0);
        totalPOS = getIntent().getIntExtra(RESULT_TOTAL_POS, 0);
        tempInventory = getIntent().getStringExtra(RESULT_INVENTORY);
        total_price_end = totalprice;
        tv_total_harga_barang.setText(CommonFunction.convertCurrencyFormat(totalprice));
        tv_pajak_dan_service.setText(CommonFunction.convertCurrencyFormat(0.0));
        tv_potongan_harga.setText(CommonFunction.convertCurrencyFormat(0.0));
        tv_total_harga.setText(CommonFunction.convertCurrencyFormat(totalprice));

        sellModel = TransakuApplication.getInstance().getGson().fromJson(getIntent().getStringExtra(RESULT_SELL_MODEL), SellModel.class);
        adapter = new POSResultAdapter(this, sellModel.getSellDetails(), totalprice);
        rv_result_inventory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static void editTotalHargaBarang(Double totalprice) {
        tv_total_harga_barang.setText(CommonFunction.convertCurrencyFormat(totalprice));
        tv_total_harga.setText(CommonFunction.convertCurrencyFormat(totalprice));
        total_price_end = totalprice;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pos_result_bt_batal:
                onBackPressed();
                break;
            case R.id.pos_result_bt_bayar:
                if(adapter.getTotalResult() != 0){
                    dialogPay(total_price_end);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       POSActivity.showActivity(this, Const.page_flag_back,TransakuApplication.getInstance().getGson().toJson(sellModel),totalprice, totalPOS);
    }

    @Override
    public void showResponse(boolean isSuceess, String message) {
        if(isSuceess){
            finish();
            Toast.makeText(this, "POS berhasil", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "POS gagal "+message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showActivity(Context context, String result, String resultInv, double totalHarga, int totalPOS) {
        Intent intent = new Intent(context, POSResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_SELL_MODEL, result);
        bundle.putDouble(RESULT_TOTAL_HARGA, totalHarga);
        bundle.putString(RESULT_INVENTORY, resultInv);
        bundle.putInt(RESULT_TOTAL_POS, totalPOS);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    private void dialogPay(final double totalpay){
        LayoutInflater inflater = LayoutInflater.from(POSResultActivity.this);
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
                if(!user_pay.getText().toString().trim().isEmpty() && Double.parseDouble(user_pay.getText().toString()) >= totalpay){
                    alertDialog.dismiss();
                    double pay_return = Double.parseDouble(user_pay.getText().toString()) - totalpay;
                    Toast.makeText(POSResultActivity.this, "Jumlah kembalian : "+pay_return, Toast.LENGTH_LONG).show();
                    sellModel.setId(0);
                    sellModel.setSellDetails(adapter.getSellDetailModelList());
                    sellModel.setTotalPrice(total_price_end);
                    presenter.sendSellPOS(sellModel);
                } else if(user_pay.getText().toString().trim().isEmpty()) {
                    user_pay.setError("Tidak boleh kosong");
                } else if(Double.parseDouble(user_pay.getText().toString()) < totalpay){
                    user_pay.setError("Pembayaran lebih kecil daripada total harga barang");
                }
            }
        });

        alertDialog.show();
    }
}
