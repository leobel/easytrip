package org.freelectron.leobel.easytrip;

import android.content.Context;
import android.content.Intent;

/**
 * Created by leobel on 2/1/17.
 */

public class Utils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int min(int[] array){
        return min(array, Integer.MIN_VALUE);
    }

    public static int min(int[] array, int threshold){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++){
            if(array[i] >= threshold && array[i] < min){
                min = array[i];
            }
        }
        return min;
    }
}
