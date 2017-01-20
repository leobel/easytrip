package org.freelectron.leobel.easytrip.services;

import android.content.Context;

import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKUser;

import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.Response;

import java.util.List;

import rx.Observable;

/**
 * Created by leobel on 1/13/17.
 */

public interface PinterestService {
    Observable<Response<PDKUser>> login(Context context, List<String> scopes);
    Observable<PageResponse<List<PDKPin>>> getMyPins(String fields, String cursor);
    Observable<PageResponse<List<PDKPin>>> getBoardPins(String board, String fields, String cursor);
}
