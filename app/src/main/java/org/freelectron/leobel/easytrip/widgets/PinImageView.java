package org.freelectron.leobel.easytrip.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by leobel on 1/31/17.
 */

public class PinImageView extends SimpleDraweeView {


    private int imageWidth = 1;
    private int imageHeight = 1;

    public PinImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public PinImageView(Context context) {
        super(context);
    }

    public PinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PinImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setImageWidth(int width){
        this.imageWidth = width;
    }

    public void setImageHeight(int height){
        this.imageHeight = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, imageHeight * width / imageWidth);
    }
}
