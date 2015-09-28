package com.ankoma88.converterlab.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.models.Offer;
import com.ankoma88.converterlab.util.Converter;

public class ShareOfferView extends LinearLayout {
    private TextView mTvCurrencyTicker;
    private TextView mTvAsk;
    private TextView mTvBid;

    public ShareOfferView(Context context) {
        super(context);
        findViews();
    }

    public ShareOfferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViews();
    }

    public ShareOfferView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViews();
    }

    public static ShareOfferView inflate(ViewGroup parent) {
        return (ShareOfferView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_offer_view_fs, parent, false);
    }

    private void findViews() {
        inflate(getContext(), R.layout.share_offer_card_fs, this);
        mTvCurrencyTicker = (TextView) findViewById(R.id.tvCurrencyName_FS);
        mTvAsk = (TextView) findViewById(R.id.tvAsk_FS);
        mTvBid = (TextView) findViewById(R.id.tvBid_FS);
    }

    public void setView(Offer offer) {
        mTvCurrencyTicker.setText(offer.getCurrency());
        mTvAsk.setText(Converter.formatDouble(offer.getAsk()));
        mTvBid.setText(Converter.formatDouble(offer.getBid()));
    }

}