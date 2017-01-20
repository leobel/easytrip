package org.freelectron.leobel.easytrip.services;

import org.freelectron.leobel.easytrip.models.Realm.BoardRealm;
import org.freelectron.leobel.easytrip.models.Realm.TravelCategoryRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by leobel on 1/19/17.
 */

public class RealmServiceImpl implements RealmService {

    @Override
    public List<BoardRealm> getBoardByCategory(Integer id) {
        Realm realm = Realm.getDefaultInstance();
        TravelCategoryRealm categoryRealm = realm.where(TravelCategoryRealm.class).equalTo("id", id).findFirst();
        return categoryRealm.getBoardRealms();
    }
}
