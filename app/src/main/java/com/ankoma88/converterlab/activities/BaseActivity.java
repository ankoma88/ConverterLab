package com.ankoma88.converterlab.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.interfaces.ActionCallbacks;
import com.ankoma88.converterlab.util.Converter;

/**Parent activity for TradersActivity and OffersActivity which implements menu actions*/
public abstract class BaseActivity extends AppCompatActivity implements ActionCallbacks {
    public static final String EXTRA_TRADER_ID = "com.ankoma88.converterlab.trader_id";

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        findToolBar();

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private void findToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_AB);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            if (NavUtils.getParentActivityName(this) != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onLinkClicked(String link) {
        openLink(link);
    }

    @Override
    public void onMapClicked(int traderId) {
        openMap(traderId);
    }

    @Override
    public void onPhoneClicked(String phone) {
        openPhone(phone);
    }

    @Override
    public void onDetailClicked(int traderId) {
        openDetailInfo(traderId);
    }




    protected void openLink(String link) {
        link = Converter.prepareUrl(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    protected void openPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    protected void openDetailInfo(int traderId) {
        Intent intent = new Intent(this, OffersActivity.class);
        intent.putExtra(EXTRA_TRADER_ID, traderId);
        startActivity(intent);
    }

    protected void openMap(int traderId) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_TRADER_ID, traderId);
        startActivity(intent);
    }



}