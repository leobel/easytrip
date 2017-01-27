package org.freelectron.leobel.easytrip.adapters;

import com.pinterest.android.pdk.PDKPin;
import org.freelectron.leobel.easytrip.models.RecyclerViewAdapter;

/**
 * Created by leobel on 1/16/17.
 */
public class PinRecyclerViewAdapter extends RecyclerViewAdapter<PDKPin, PinViewHolder> {

    public PinRecyclerViewAdapter(int viewHolderId) {
        super(viewHolderId, view -> new PinViewHolder(view));
    }

}
