package org.freelectron.leobel.easytrip.models;

import java.io.Serializable;

/**
 * Created by leobel on 1/16/17.
 */

public class PaginateInfo<T> implements Serializable {
    private T index;
    private int size;

    public void setIndex(T index) {
        this.index = index;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public T getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }
}
