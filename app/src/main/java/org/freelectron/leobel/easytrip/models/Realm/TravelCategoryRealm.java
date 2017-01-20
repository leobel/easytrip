package org.freelectron.leobel.easytrip.models.Realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by leobel on 1/19/17.
 */

public class TravelCategoryRealm extends RealmObject {

    @PrimaryKey
    private Integer id;

    private String name;

    private RealmList<BoardRealm> boardRealms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<BoardRealm> getBoardRealms() {
        return boardRealms;
    }

    public void setBoardRealms(RealmList<BoardRealm> boardRealms) {
        this.boardRealms = boardRealms;
    }
}
