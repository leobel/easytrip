package org.freelectron.leobel.easytrip.services;

import org.freelectron.leobel.easytrip.models.Realm.BoardRealm;

import java.util.List;

/**
 * Created by leobel on 1/19/17.
 */

public interface RealmService {
    List<BoardRealm> getBoardByCategory(Integer id);
}
