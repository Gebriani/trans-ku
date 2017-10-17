package id.co.sumi.transaku;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.sumi.transaku.model.PopulateCombo;
import id.co.sumi.transaku.model.RestAPI;
import id.co.sumi.transaku.utils.Const;
import id.co.sumi.transaku.utils.RxBus;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

/**
 * Created by MuhammadAbrarArief on 17/10/16.
 */
public class TransakuApplication extends Application {

    private static final String TAG = "TransakuApplication";

    private static TransakuApplication instance;
    private RestAPI restAPI;
    private RxBus rxBus;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private FirebaseAnalytics firebaseAnalytics;

    public static TransakuApplication getInstance() {
        return instance;
    }

    public TransakuApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        restAPI = new RestAPI(this);
        rxBus = new RxBus();
        sharedPreferences = getSharedPreferences(TransakuApplication.class.getSimpleName(),
                Context.MODE_PRIVATE);
        setupRxJavaErrorHandler();

        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Facebook.class, new FacebookDeserializer());
        gson = gsonBuilder.create();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public PopulateCombo getPopulateComboJson(){
        return this.getGson().fromJson(Const.POPULATE_COMBO_DATA, PopulateCombo.class);
    }

    private void setupRxJavaErrorHandler() {
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                if (e.getLocalizedMessage() != null) {
                    Log.w("RxJava Error", e.getLocalizedMessage());
                }
            }
        });
    }

    public Gson getGson() {
        return gson;
    }

    public RxBus getRxBus() {
        return rxBus;
    }

    public RestAPI getRestAPI() {
        return restAPI;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public FirebaseAnalytics getFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}
