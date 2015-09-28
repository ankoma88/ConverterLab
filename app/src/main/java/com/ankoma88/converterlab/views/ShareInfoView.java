package com.ankoma88.converterlab.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.models.Trader;

public class ShareInfoView extends LinearLayout {
    private TextView mTvTraderName;
    private TextView mTvRegion;
    private TextView mTvCity;

    public ShareInfoView(Context context) {
        super(context);
        findViews();
    }

    public ShareInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static ShareInfoView inflate(ViewGroup parent) {
        return (ShareInfoView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_info_view_fs, parent, false);
    }

    private void findViews() {
        inflate(getContext(), R.layout.share_info_card_fs, this);
        mTvTraderName = (TextView) findViewById(R.id.tvTraderName_FS);
        mTvCity = (TextView) findViewById(R.id.tvCity_FS);
        mTvRegion = (TextView) findViewById(R.id.tvRegion_FS);
    }

    public void setView(Trader trader) {
        mTvTraderName.setText(trader.getName());
        mTvCity.setText(trader.getCity());
        mTvRegion.setText(trader.getRegion());
    }

}