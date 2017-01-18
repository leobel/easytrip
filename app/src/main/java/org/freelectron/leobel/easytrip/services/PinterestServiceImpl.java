package org.freelectron.leobel.easytrip.services;

import android.content.Context;
import android.util.Log;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;
import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.PaginateInfo;
import org.freelectron.leobel.easytrip.models.Response;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by leobel on 1/13/17.
 */

public class PinterestServiceImpl implements PinterestService {

    private final PDKClient pdkClient;

    public PinterestServiceImpl(PDKClient pdkClient){
        this.pdkClient = pdkClient;
    }


    @Override
    public Observable<Response<PDKUser>> login(Context context, List<String> scopes) {
        return Observable.create(new Observable.OnSubscribe<Response<PDKUser>>(){
            @Override
            public void call(Subscriber<? super Response<PDKUser>> subscriber) {
                pdkClient.login(context, scopes, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        if (!subscriber.isUnsubscribed()) {

                            subscriber.onNext(new Response<>(response.getUser(), Response.NETWORK));
                            subscriber.onCompleted();
                            Timber.d("Result login success!!!");
                        }
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        }).onErrorReturn(throwable -> new Response<>(throwable, Response.NETWORK));
    }

    @Override
    public Observable<PageResponse<List<PDKPin>>> getMyPins(String fields, String cursor){
        return Observable.create(new Observable.OnSubscribe<PageResponse<List<PDKPin>>>() {
            @Override
            public void call(Subscriber<? super PageResponse<List<PDKPin>>> subscriber) {
                pdkClient.getMyPins(fields, cursor, new PDKCallback(){
                    @Override
                    public void onSuccess(PDKResponse response) {
                        if (! subscriber.isUnsubscribed()) {
                            PaginateInfo<String> paginateInfo = null;
                            if(response.hasNext()){
                                paginateInfo = new PaginateInfo<>();
                                paginateInfo.setIndex(response.getCursor());
                            }
                            subscriber.onNext(new PageResponse<>(response.getPinList(), paginateInfo, Response.NETWORK));
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        }).onErrorReturn(throwable -> new PageResponse<>(throwable, Response.NETWORK));
    }
}
