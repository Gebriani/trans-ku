package id.co.sumi.transaku.model;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import id.co.sumi.transaku.modelresp.BaseResp;
import id.co.sumi.transaku.modelresp.BusinessTypeResp;
import id.co.sumi.transaku.modelresp.CitiesResp;
import id.co.sumi.transaku.modelresp.InventoryListResp;
import id.co.sumi.transaku.modelresp.PenjualanBarangResp;
import id.co.sumi.transaku.modelresp.PopulateComboResp;
import id.co.sumi.transaku.modelresp.RegisterResp;
import id.co.sumi.transaku.modelresp.ReportListResp;
import id.co.sumi.transaku.modelresp.SellListResp;
import id.co.sumi.transaku.modelresp.SellerProfileResp;
import id.co.sumi.transaku.modelresp.SupplierResp;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by MuhammadAbrar on 31/1/16.
 */
public class RestAPI {

    //Swagger
    //http://ec2-54-179-155-189.ap-southeast-1.compute.amazonaws.com:8181/transaku/swagger-ui.html#!

//    https://scotch.io/bar-talk/build-a-realtime-chat-server-with-go-and-websockets

    public static final String BASE_IMAGE_LOCAL_URL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Temp/";
    public static final String URL_LOCAL = "http://192.168.88.21:8080";
    public static final String URL_DEV = "http://52.77.131.158:8181";

    public static final String URL_DEV_2 = "http://ec2-54-179-155-189.ap-southeast-1.compute.amazonaws.com:8181";

    public static final String URL_PROD = "";
    public static final String URL = URL_DEV_2;
    private static final String BASE_URL = URL + "/transaku/";

    private Api api;

    public RestAPI(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestAPI.BASE_URL)
                .client(provideOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);

    }

    private OkHttpClient provideOkHttpClient(final Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        Log.d("Response Code", response.code() + "");
                        if (response.code() == 401) {
                            Intent intent = new Intent("Logout");
                            intent.putExtra("badAuth", true);
                            context.sendBroadcast(intent);
                        }
                        return response;
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();
        return okHttpClient;
    }

    public Api getApi() {
        return api;
    }

    public interface Api {

        @GET("inventory/{sellerId}/{itemname}")
        Observable<InventoryListResp> getInventoryByCustIdAndItemName(@Header("Authorization") String authorization, @Path("sellerId") String sellerId, @Path("itemname") String itemName);

        @FormUrlEncoded
        @POST("oauth/token")
        Observable<LoginBean> login(@Header("Authorization") String authorization, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant);

        @GET("inventory/all/{id}")
        Observable<InventoryListResp> getInventoryByCustomerID(@Header("Authorization") String authorization, @Path("id") String custId);

        @GET("inventory/toSell/{id}")
        Observable<InventoryListResp> getPOSFORSellByCustomerID(@Header("Authorization") String authorization, @Path("id") String custId);

        @GET("sell/history/{sellerId}/{startDate}/{endDate}")
        Observable<SellListResp> getReportSellByDate(@Header("Authorization") String authorization, @Path("sellerId") String sellerID, @Path("startDate") String startDate, @Path("endDate") String endDate);

        @POST("sell")
        Observable<BaseResp> sendSellPOS(@Header("Authorization") String authorization, @Body SellModel sellModel);

        @GET("city/province/{provinceId}")
        Observable<CitiesResp> getCitiesByProvID(@Path("provinceId") String provinceId);

        @GET("businessType/all")
        Observable<BusinessTypeResp> getAllBusinessType();

        @POST("signUp")
        Observable<RegisterResp> userRegister(@Body UserProfileModel userProfileModel);

        @POST("signUp/resetPassword")
        Observable<BaseResp> resetPassword(@Body UserProfileModel userProfileModel);

        @POST("inventory")
        Observable<BaseResp> addNewInventory(@Header("Authorization") String authorization, @Body InventoryModel inventoryModel);

        @GET("oauth/check_token")
        Observable<BaseResp> checkToken(@Header("Authorization") String authorization,
                                        @Query("token") String token);
        @GET("apps/populateCombo")
        Observable<PopulateComboResp> getAllPopulateCombo();

        @POST("inventory/reStock")
        Observable<BaseResp> restockInventory(@Header("Authorization") String authorization,
                                              @Body InventoryModel inventoryModel);

        @PUT("signUp/verification")
        Observable<BaseResp> registrasiVerification(@Body UserProfileModel userProfileModel);

        @GET("supplier/search/inventory/{name}")
        Observable<SupplierResp> searchSupplierByNameInventory(@Header("Authorization") String authorization,
                                                               @Path("name") String inv_name);

        @GET("supplier/search/{name}")
        Observable<SupplierResp> searchSupplierByNameSupplier(@Header("Authorization") String authorization,
                                                              @Path("name") String supp_name);

        @GET("supplier/active")
        Observable<SupplierResp> getAllActiveSupplier(@Header("Authorization") String authorization);

        @GET("supplierInventory/all/{id}")
        Observable<InventoryListResp> getAllInventoryBySupplierID(@Header("Authorization") String authorization,
                                                                  @Path("id") long id);

        @GET("inventory/search/{sellerId}/{name}")
        Observable<InventoryListResp> searchInventoryByName(@Header("Authorization") String authorization,
                                                            @Path("sellerId") String sellerID,
                                                            @Path("name") String name);

        @GET("inventory/search/toSell/{sellerId}/{name}")
        Observable<InventoryListResp> searchInventoryForSellByName(@Header("Authorization") String authorization,
                                                            @Path("sellerId") String sellerID,
                                                            @Path("name") String name);

        @GET("capital/reportByDateBetween/{sellerId}/{startDate}/{endDate}")
        Observable<ReportListResp> getReportLabaRugi(@Header("Authorization") String authorization,
                                                     @Path("sellerId") String sellerID,
                                                     @Path("startDate") String startDate,
                                                     @Path("endDate") String endDate);

        @GET("supplier/searchWithLocation/inventory/{name}/{latitude}/{longitude}")
        Observable<SupplierResp> searchSupplier_ByNameInventory_OrderByDistance(@Header("Authorization") String authorization,
                                                                                @Path("name") String inv_name,
                                                                                @Path("latitude") String latitude,
                                                                                @Path("longitude") String longitude);

        @GET("supplier/searchWithLocation/{name}/{latitude}/{longitude}")
        Observable<SupplierResp> searchSupplier_ByNameSupplier_OrderByDistance(@Header("Authorization") String authorization,
                                                                               @Path("name") String supp_name,
                                                                               @Path("latitude") String latitude,
                                                                               @Path("longitude") String longitude);

        @GET("sell/itemSoldSummary/{sellerId}/{startDate}/{endDate}")
        Observable<PenjualanBarangResp> getReportPenjualanBarang(@Header("Authorization") String authorization,
                                                                 @Path("sellerId") String sellerID,
                                                                 @Path("startDate") String startDate,
                                                                 @Path("endDate") String endDate);

        @PUT("inventory")
        Observable<BaseResp> editPriceInventory(@Header("Authorization") String authorization, @Body InventoryModel inventoryModel);

        @GET("customer/profile")
        Observable<SellerProfileResp> getSellerProfile(@Header("Authorization") String authorization);

        @PUT("customer/changePassword")
        Observable<BaseResp> sellerChangePassword(@Header("Authorization") String authorization, @Body UserProfileModel userProfileModel);

        @HTTP(method = "DELETE", path = "inventory", hasBody = true)
        Observable<BaseResp> deleteInventory(@Header("Authorization") String authorization, @Body InventoryModel inventoryModel);

        @PUT("customer/profile")
        Observable<BaseResp> editSellerProfile(@Header("Authorization") String authorization, @Body UserProfileModel userProfileModel);

        @POST("supplierPO")
        Observable<BaseResp> orderSupplier(@Header("Authorization") String authorization, @Body OrderModel orderModel);


//        @Multipart
//        @POST("upload")
//        Observable<BaseResp> uploadUserPic(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    }

}
