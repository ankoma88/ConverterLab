package com.ankoma88.converterlab.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.models.Offer;
import com.ankoma88.converterlab.util.Converter;

public class OfferView extends LinearLayout {
    private TextView mTvCurrencyName;
    private TextView mTvAsk;
    private TextView mTvBid;
    private ImageView mIvAskChange;
    private ImageView mIvBidChange;

    public OfferView(Context context) {
        super(context);
        findViews();
    }

    public OfferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViews();
    }

    public OfferView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViews();
    }

    public static OfferView inflate(ViewGroup parent) {
        return (OfferView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_view_fo, parent, false);
    }

    private void findViews() {
        inflate(getContext(), R.layout.offer_card_fo, this);
        mTvCurrencyName = (TextView) findViewById(R.id.tvCurrencyName_FO);
        mTvAsk = (TextView) findViewById(R.id.tvAsk_FO);
        mTvBid = (TextView) findViewById(R.id.tvBid_FO);
        mIvAskChange = (ImageView) findViewById(R.id.ivAskChange_FO);
        mIvBidChange = (ImageView) findViewById(R.id.ivBidChange_FO);
    }

    public void setView(Offer offer) {
        mTvCurrencyName.setText(offer.getCurrency());
        mTvAsk.setText(Converter.formatDouble(offer.getAsk()));
        mTvBid.setText(Converter.formatDouble(offer.getBid()));
        if (offer.getAskChange() >= 0) {
            mTvAsk.setTextColor(getResources().getColor(R.color.green));
            mIvAskChange.setImageResource(R.drawable.ic_green_arrow_up);
        } else {
            mTvAsk.setTextColor(getResources().getColor(R.color.red));
            mIvAskChange.setImageResource(R.drawable.ic_green_arrow_up);
        }

        if (offer.getBidChange() >= 0) {
            mTvBid.setTextColor(getResources().getColor(R.color.green));
            mIvBidChange.setImageResource(R.drawable.ic_green_arrow_up);
        } else {
            mTvBid.setTextColor(getResources().getColor(R.color.red));
            mIvBidChange.setImageResource(R.drawable.ic_green_arrow_up);
        }
    }

}