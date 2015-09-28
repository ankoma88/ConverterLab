package com.ankoma88.converterlab.activities;

import android.support.v4.app.Fragment;

import com.ankoma88.converterlab.fragments.OffersFragment;

/**Activity launches OffersFragment*/
public class OffersActivity extends BaseActivity {
    @Override
    protected Fragment createFragment() {
        int traderId = getIntent().getIntExtra(TradersActivity.EXTRA_TRADER_ID, -1);
        if (traderId != -1) {
            return OffersFragment.newInstance(traderId);
        } else return new OffersFragment();
    }
}
