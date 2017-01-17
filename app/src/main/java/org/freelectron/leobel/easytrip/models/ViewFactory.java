package org.freelectron.leobel.easytrip.models;

import android.view.View;

/**
 * Created by leobel on 1/16/17.
 */

public interface ViewFactory<T> {
    T create(View view);
}
