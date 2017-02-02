package org.freelectron.leobel.easytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.pinterest.android.pdk.PDKClient;

import org.freelectron.leobel.easytrip.services.PinterestService;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by leobel on 2/2/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Inject
    public PinterestService pinterestService;

    protected AlertDialog requireLoginDialog;
    protected AlertDialog requireConnectionDialog;
    public static ArrayList<String> scopes = new ArrayList<>();

    static {
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyTripApp.getInstance().getComponent().inject(this);

        requireLoginDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.login_dialog_message)
                .setNegativeButton(R.string.flight_cabin_passenger_positive_cancel_text, (dialogInterface, i) -> requireLoginDialog.dismiss())
                .setPositiveButton(R.string.login_dialog_login, (dialogInterface, i) -> {
                    pinterestService.login(this, scopes)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if(response.isSuccessful()){
                                    Timber.d("User login successful :" + response.getValue().getFirstName());
                                }
                                else{
                                    Timber.d("User login error: " + response.getError());
                                }
                            });
                })
                .create();

        requireConnectionDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.require_connection)
                .setNegativeButton(R.string.require_connection_ok, (dialogInterface, i) -> requireConnectionDialog.dismiss())
                .setPositiveButton(R.string.require_connection_settings, (dialogInterface, i) -> startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)))
                .create();
    }
}
