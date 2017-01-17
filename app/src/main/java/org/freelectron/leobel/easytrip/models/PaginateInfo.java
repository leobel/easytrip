package org.freelectron.leobel.easytrip.models;

/**
 * Created by leobel on 1/16/17.
 */

public class PaginateInfo<T> {
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
