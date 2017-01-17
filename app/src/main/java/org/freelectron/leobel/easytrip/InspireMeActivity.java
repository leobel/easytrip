package org.freelectron.leobel.easytrip;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.adapters.PinRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.PaginateInfo;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.RecyclerViewListener;
import org.freelectron.leobel.easytrip.models.RecyclerViewManager;
import org.freelectron.leobel.easytrip.models.Response;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.widgets.DividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class InspireMeActivity extends AppCompatActivity implements RecyclerViewListener<PDKPin>{

    RecyclerViewManager<PDKPin> recyclerViewManager;

    @Inject
    public PinterestService pinterestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspire_me);

        EasyTripApp.getInstance().getComponent().inject(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pins_recycler_view);
        View emptyView = findViewById(R.id.empty_view_container);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewManager = new RecyclerViewManager<>(this, recyclerView, emptyView);
    }


    @Override
    public RecyclerViewAdapter getAdapter() {
        return new PinRecyclerViewAdapter(R.layout.fragment_pin);
    }

    @Override
    public void itemClick(PDKPin item) {
        
    }



    @Override
    public Observable<PageResponse<List<PDKPin>>> getItems(PaginateInfo<?> paginateInfo) {
        String cursor = "";
        if(paginateInfo != null) cursor = (String) paginateInfo.getIndex();
        return pinterestService.getMyPins("id,link,note,url,counts,image,metadata", cursor);
    }

    @Override
    public void onLoadingItemsError(Throwable error) {
        Timber.d("Result error: " + error.getMessage());
    }
}
