package org.freelectron.leobel.easytrip.models;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.freelectron.leobel.easytrip.models.Realm.BoardRealm;

import java.util.List;

/**
 * Created by leobel on 2/7/17.
 */

public class BoardRecyclerViewManager<T> extends RecyclerViewManager<T> {
    private final List<BoardRealm> boards;
    private int index;

    public BoardRecyclerViewManager(RecyclerViewListener listener, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, View emptyView, List items, PaginateInfo<BoardIndex> paginateInfo, List<BoardRealm> boards, int index) {
        super(listener, recyclerView, swipeRefreshLayout, emptyView, items, paginateInfo);
        this.boards = boards;
        this.index = index;
    }


    @Override
    protected void refreshList() {
        index = 0;
        super.refreshList();
    }

    @Override
    protected void setHasMoreItems(PageResponse<List<T>> response) {
         PaginateInfo<BoardIndex> paginateInfo = new PaginateInfo<>();
        BoardIndex boardIndex = new BoardIndex(index, "");
        if(!response.hasMoreItems() && index < boards.size() - 1){ // go to the next board
            index +=1;
            boardIndex.setBoardIndex(index);
            paginateInfo.setIndex(boardIndex);
        }
        else if(response.hasMoreItems()){ // the current board has more items
            boardIndex.setCursor((String) response.getPaginateInfo().getIndex());
            paginateInfo.setIndex(boardIndex);
        }
        else{
            paginateInfo = null;
        }

        setCurrentPaginateInfo(paginateInfo);
    }

    public int getBoardIndex(){
        return index;
    }
}
