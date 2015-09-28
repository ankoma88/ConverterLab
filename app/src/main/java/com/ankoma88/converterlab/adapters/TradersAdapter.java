package com.ankoma88.converterlab.adapters;


import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.interfaces.ActionCallbacks;
import com.ankoma88.converterlab.models.Trader;

import java.util.ArrayList;
import java.util.List;

public class TradersAdapter extends RecyclerView.Adapter<TradersAdapter.TraderViewHolder> {

    private final List<Trader> mTraders;
    private final ActionCallbacks mCallbacks;

    public TradersAdapter(List<Trader> traders, ActionCallbacks callbacks) {
        mCallbacks = callbacks;
        mTraders = new ArrayList<>(traders);
    }

    @Override
    public int getItemCount() {
        return mTraders.size();
    }


    @Override
    public void onBindViewHolder(TraderViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public TraderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trader_card_ft, parent, false);

        return new TraderViewHolder(itemView);
    }


    protected class TraderViewHolder extends RecyclerView.ViewHolder
                                     implements TabLayout.OnTabSelectedListener {

        private Trader mTrader;
        private final TextView mTvName;
        private final TextView mTvRegion;
        private final TextView mTvCity;
        private final TextView mTvPhoneNumber;
        private final TextView mTvAddress;

        private final TabLayout mTabLayout;

        public TraderViewHolder(View itemView) {
            super(itemView);

            mTvName = (TextView) itemView.findViewById(R.id.tvTraderName_AT);
            mTvRegion = (TextView) itemView.findViewById(R.id.tvRegion_AT);
            mTvCity = (TextView) itemView.findViewById(R.id.tvCity_AT);
            mTvAddress = (TextView) itemView.findViewById(R.id.tvAddress_AT);
            mTvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber_AT);

            mTabLayout = (TabLayout) itemView.findViewById(R.id.tabs_FT);
            mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_link));
            mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_map));
            mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_phone));
            mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_next));
            mTabLayout.setOnTabSelectedListener(TraderViewHolder.this);
        }

        public void onBind(int position) {
            mTrader = mTraders.get(position);
            setupValues();
        }

        private void setupValues() {
            ensureAddressAndPhoneNotEmpty();
            mTvName.setText(mTrader.getName());
            mTvRegion.setText(mTrader.getRegion());
            mTvCity.setText(mTrader.getCity());
            mTvAddress.setText(itemView.getContext().getString(R.string.address_label)
                    + mTrader.getAddress());
            mTvPhoneNumber.setText(itemView.getContext().getString(R.string.tel_label)
                    + mTrader.getPhone());
        }

        private void ensureAddressAndPhoneNotEmpty() {
            String address = mTrader.getAddress();
            String phone = mTrader.getPhone();
            if (address == null || "".equals(address)) {
                mTrader.setAddress(itemView.getContext().getString(R.string.unknown));
            }
            if (phone == null || "".equals(phone)) {
                mTrader.setPhone(itemView.getContext().getString(R.string.unknown));
            }
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            handleTabClick(tab);
        }


        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            handleTabClick(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //do nothing
        }

        private void handleTabClick(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    mCallbacks.onLinkClicked(mTrader.getLink());
                    break;
                case 1:
                    mCallbacks.onMapClicked(mTrader.getId());
                    break;
                case 2:
                    mCallbacks.onPhoneClicked(mTrader.getPhone());
                    break;
                case 3:
                    mCallbacks.onDetailClicked(mTrader.getId());
                    break;
            }
        }
    }

    public void animateTo(List<Trader> models) {
        setupAnimatedRemovals(models);
        setupAnimatedAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void setupAnimatedRemovals(List<Trader> newModels) {
        for (int i = mTraders.size() - 1; i >= 0; i--) {
            final Trader model = mTraders.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void setupAnimatedAdditions(List<Trader> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Trader model = newModels.get(i);
            if (!mTraders.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Trader> newTraders) {
        for (int toPosition = newTraders.size() - 1; toPosition >= 0; toPosition--) {
            final Trader trader = newTraders.get(toPosition);
            final int fromPosition = mTraders.indexOf(trader);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Trader removeItem(int position) {
        final Trader trader = mTraders.remove(position);
        notifyItemRemoved(position);
        return trader;
    }

    public void addItem(int position, Trader trader) {
        mTraders.add(position, trader);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Trader trader = mTraders.remove(fromPosition);
        mTraders.add(toPosition, trader);
        notifyItemMoved(fromPosition, toPosition);
    }

}
