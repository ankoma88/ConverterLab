package com.ankoma88.converterlab.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.models.Trader;

public class TraderInfoView extends LinearLayout {
    private TextView mTvTraderName;
    private TextView mTvLink;
    private TextView mTvType;
    private TextView mTvAddress;
    private TextView mTvCity;
    private TextView mTvRegion;
    private TextView mTvPhone;

    public TraderInfoView(Context context) {
        super(context);
        findViews();
    }

    public TraderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TraderInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static TraderInfoView inflate(ViewGroup parent) {
        return (TraderInfoView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trader_info_view_fo, parent, false);
    }

    private void findViews() {
        inflate(getContext(), R.layout.trader_info_card_fo, this);
        mTvTraderName = (TextView) findViewById(R.id.tvTraderName_FO);
        mTvLink = (TextView) findViewById(R.id.tvLink_FO);
        mTvType = (TextView) findViewById(R.id.tvType_FO);
        mTvAddress = (TextView) findViewById(R.id.tvAddress_FO);
        mTvCity = (TextView) findViewById(R.id.tvCity_FO);
        mTvRegion = (TextView) findViewById(R.id.tvRegion_FO);
        mTvPhone = (TextView) findViewById(R.id.tvPhone_FO);
    }

    public void setView(Trader trader) {
        mTvTraderName.setText(trader.getName());
        mTvLink.setText(trader.getLink());
        mTvType.setText(trader.getType());
        mTvAddress.setText(trader.getAddress());
        mTvCity.setText(trader.getCity());
        mTvRegion.setText(trader.getRegion());
        mTvPhone.setText(trader.getPhone());
    }

}