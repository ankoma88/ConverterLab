package com.ankoma88.converterlab.activities;

import android.support.v4.app.Fragment;

import com.ankoma88.converterlab.fragments.TradersFragment;

/**Activity launches TradersFragment*/
public class TradersActivity extends BaseActivity {
    @Override
    protected Fragment createFragment() {
        return new TradersFragment();
    }
}
