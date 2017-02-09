package org.freelectron.leobel.easytrip.models;

import java.io.Serializable;

/**
 * Created by leobel on 2/7/17.
 */

public class BoardIndex implements Serializable {

    private String cursor;
    private int boardIndex;

    public BoardIndex(int boardIndex, String cursor){
        this.boardIndex = boardIndex;
        this.cursor = cursor;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }
}
