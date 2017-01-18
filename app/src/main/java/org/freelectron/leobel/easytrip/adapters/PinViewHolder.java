package org.freelectron.leobel.easytrip.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pinterest.android.pdk.PDKOriginal;
import com.pinterest.android.pdk.PDKPin;
import com.pinterest.android.pdk.PDKPlace;

import org.freelectron.leobel.easytrip.R;
import org.freelectron.leobel.easytrip.models.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by leobel on 1/16/17.
 */

public class PinViewHolder extends RecyclerViewHolder<PDKPin> {

    private SimpleDraweeView pinImage;
    private TextView pinNote;

    public PinViewHolder(View itemView) {
        super(itemView);

        pinImage = (SimpleDraweeView)itemView.findViewById(R.id.pin_image);
        pinNote = (TextView)itemView.findViewById(R.id.pin_note);
    }

    @Override
    public void updateFrom(PDKPin item) {
        PDKOriginal original = item.getImage().getOriginal();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = original.getHeight();
//        layoutParams.width = original.getWidth();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        pinImage.setLayoutParams(layoutParams);

        ArrayList<String> a = new ArrayList();
        pinImage.setImageURI(Uri.parse(original.getUrl()));
        PDKPlace place = item.getMetadata().getPlace();
        if(place!= null) {
            pinNote.setText(place.getName());
        }

    }
}
