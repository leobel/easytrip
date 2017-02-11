package org.freelectron.leobel.easytrip.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.EasyTripApp;
import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.adapters.PinRecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.BoardIndex;
import org.freelectron.leobel.easytrip.models.BoardRecyclerViewManager;
import org.freelectron.leobel.easytrip.models.PageResponse;
import org.freelectron.leobel.easytrip.models.PaginateInfo;
import org.freelectron.leobel.easytrip.models.Realm.BoardRealm;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;
import org.freelectron.leobel.easytrip.models.RecyclerViewListener;
import org.freelectron.leobel.easytrip.models.RecyclerViewManager;
import org.freelectron.leobel.easytrip.services.PinterestService;
import org.freelectron.leobel.easytrip.services.RealmService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPinListInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinListFragment extends Fragment implements RecyclerViewListener<PDKPin> {

    private static final String ARG_CATEGORY_PARAM = "ARG_CATEGORY_PARAM";
    private static final String ITEMS_LIST = "ITEMS_LIST";
    private static final String PAGINATE_INFO = "PAGINATE_INFO";
    private static final String INDEX = "INDEX";

    BoardRecyclerViewManager<PDKPin> recyclerViewManager;
    private OnPinListInteractionListener mListener;

    private Integer categoryId;
    private List<BoardRealm> boards;

    @Inject
    public PinterestService pinterestService;

    @Inject
    public RealmService realmService;

    public PinListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryId Travel categoryId.
     * @return A new instance of fragment PinListFragment.
     */
    public static PinListFragment newInstance(Integer categoryId) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_PARAM, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasyTripApp.getInstance().getComponent().inject(this);

        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_PARAM);
            boards = realmService.getBoardByCategory(categoryId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pin_list, container, false);

        setListener(getParentFragment());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pins_recycler_view);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        View emptyView = view.findViewById(R.id.empty_view_container);

        int columns = getResources().getInteger(R.integer.gallery_columns);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<PDKPin> items = null;
        PaginateInfo<BoardIndex> paginateInfo = null;
        int index = 0;
        if(savedInstanceState != null){
            items = (ArrayList<PDKPin>)savedInstanceState.getSerializable(ITEMS_LIST);
            paginateInfo = (PaginateInfo<BoardIndex>) savedInstanceState.getSerializable(PAGINATE_INFO);
            index = savedInstanceState.getInt(INDEX);
        }
        recyclerViewManager = new BoardRecyclerViewManager<>(this, recyclerView, swipeRefreshLayout, emptyView, items, paginateInfo, boards, index);

        return view;
    }


    public void setListener(Object listener) {
        if (listener instanceof OnPinListInteractionListener) {
            mListener = (OnPinListInteractionListener) listener;
        } else {
            throw new RuntimeException(listener.toString()
                    + " must implement OnPinListInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewManager.unSubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerViewManager != null){
            outState.putSerializable(ITEMS_LIST, (ArrayList<PDKPin>)recyclerViewManager.getItems());
            outState.putSerializable(PAGINATE_INFO, recyclerViewManager.getPaginateInfo());
            outState.putInt(INDEX, recyclerViewManager.getBoardIndex());
        }
    }

    @Override
    public RecyclerViewAdapter getAdapter() {
        return new PinRecyclerViewAdapter(R.layout.fragment_pin);
    }

    @Override
    public void itemClick(PDKPin pin) {
        mListener.onPinSelected(pin, pin.getBoard().getUid());

    }

    @Override
    public Observable<PageResponse<List<PDKPin>>> getItems(PaginateInfo<?> paginateInfo) {
        String cursor = "";
        int index = 0;
        if(paginateInfo != null) {
            BoardIndex boardIndex = (BoardIndex) paginateInfo.getIndex();
            index = boardIndex.getBoardIndex();
            cursor = boardIndex.getCursor();
        }
        return pinterestService.getBoardPins(boards.get(index).getId(), getString(R.string.pin_list_query), cursor);
    }

    @Override
    public void onLoadingItemsError(Throwable error) {
        mListener.onLoadingItemsError(error);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPinListInteractionListener {
        void onPinSelected(PDKPin pin, String boardId);
        void onLoadingItemsError(Throwable error);
    }
}
