package org.freelectron.leobel.easytrip.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.pinterest.android.pdk.PDKOriginal;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKPlace;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.Utils;
import org.freelectron.leobel.easytrip.models.RecyclerViewHolder;
import org.freelectron.leobel.easytrip.widgets.PinImageView;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by leobel on 1/16/17.
 */

public class PinViewHolder extends RecyclerViewHolder<PDKPin> {

    private PinImageView pinImage;
    private TextView pinNote;

    public PinViewHolder(View itemView) {
        super(itemView);
        pinImage = (PinImageView)itemView.findViewById(R.id.pin_image);
        pinNote = (TextView)itemView.findViewById(R.id.pin_note);
    }

    @Override
    public void updateFrom(PDKPin item) {
        Utils.setPinImage(item, pinImage);
        PDKPlace place = item.getMetadata().getPlace();
        if(place != null) {
            pinNote.setText(place.getName());
        }
        else{
            pinNote.setText(R.string.unknown_place);
        }

    }
}
