package org.freelectron.leobel.easytrip;

import android.support.multidex.MultiDexApplication;

import com.pinterest.android.pdk.PDKClient;
import com.squareup.leakcanary.LeakCanary;

import org.freelectron.leobel.easytrip.modules.AppModule;
import org.freelectron.leobel.easytrip.modules.ServicesModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by leobel on 1/4/17.
 */

public class EasyTripApp extends MultiDexApplication {

    public ApplicationComponent component;

    private static EasyTripApp instance;

    public EasyTripApp(){instance = this;}

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm
        // The Realm file will be located in Context.getFilesDir() with name "default.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // Initialize Pinterest
        PDKClient.configureInstance(this, getString(R.string.pinterest_api));

        // Initialize Leak Canary
        LeakCanary.install(this);

        // Initialize Timber only if Debug
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Initialize Dagger component
        component = DaggerApplicationComponent
                .builder()
                .appModule(new AppModule(this))
                .servicesModule(new ServicesModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static EasyTripApp getInstance(){
        return instance;
    }
}
