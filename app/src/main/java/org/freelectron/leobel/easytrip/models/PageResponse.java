package org.freelectron.leobel.easytrip.models;

/**
 * Created by leobel on 1/16/17.
 */

public class PageResponse<T> extends Response<T> {

    private boolean hasMoreItems;
    private PaginateInfo<?> paginateInfo;


    public PageResponse(T value, PaginateInfo paginateInfo) {
        this(value, paginateInfo, Response.CACHE);
    }

    public PageResponse(T value, PaginateInfo<?> paginateInfo, int source) {
        super(value, source);
        this.paginateInfo = paginateInfo;
        if(paginateInfo != null){
            hasMoreItems = true;
        }
    }

    public PageResponse(Throwable error, int source) {
        super(error, source);
        hasMoreItems = false;
    }

    public boolean hasMoreItems(){
        return hasMoreItems;
    };

    public PaginateInfo<?> getPaginateInfo(){
        return paginateInfo;
    }

    public void setPaginateInfo(PaginateInfo paginateInfo){
        this.paginateInfo = paginateInfo;
    }
}
