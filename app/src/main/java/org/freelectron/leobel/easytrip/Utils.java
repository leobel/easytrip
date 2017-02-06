package org.freelectron.leobel.easytrip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.pinterest.android.pdk.PDKOriginal;
import com.pinterest.android.pdk.PDKPin;

import org.freelectron.leobel.easytrip.widgets.PinImageView;

/**
 * Created by leobel on 2/1/17.
 */

public class Utils {

    public static void setPinImage(PDKPin item, PinImageView pinImage) {
        PDKOriginal original = item.getImage().getOriginal();
        pinImage.setImageWidth(original.getWidth());
        pinImage.setImageHeight(original.getHeight());

        GenericDraweeHierarchy hierarchy = pinImage.getHierarchy();
        if(item.getColor() != null){
            int color= Color.parseColor(item.getColor());
            hierarchy.setPlaceholderImage(new ColorDrawable(color));
        }
        else{
            hierarchy.setPlaceholderImage(R.color.divider);
        }
        pinImage.setImageURI(Uri.parse(original.getUrl()));
    }

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
