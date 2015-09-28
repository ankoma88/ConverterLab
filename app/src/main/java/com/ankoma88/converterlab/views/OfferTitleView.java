package com.ankoma88.converterlab.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ankoma88.converterlab.R;

public class OfferTitleView extends LinearLayout {

    public OfferTitleView(Context context) {
        super(context);
        findViews();
    }

    public OfferTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OfferTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static OfferTitleView inflate(ViewGroup parent) {
        return (OfferTitleView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_title_view_fo, parent, false);
    }

    private void findViews() {
        inflate(getContext(), R.layout.offer_title_card_fo, this);
    }

}