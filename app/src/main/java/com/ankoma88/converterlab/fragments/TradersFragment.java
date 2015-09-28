package com.ankoma88.converterlab.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.adapters.TradersAdapter;
import com.ankoma88.converterlab.db.TraderDAO;
import com.ankoma88.converterlab.interfaces.ActionCallbacks;
import com.ankoma88.converterlab.models.Trader;
import com.ankoma88.converterlab.receivers.AlarmReceiver;
import com.ankoma88.converterlab.services.UpdateService;

import java.util.ArrayList;
import java.util.List;

/**Fragment with list of currency traders*/
public class TradersFragment extends Fragment implements SearchView.OnQueryTextListener,
                                                         SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TradersFragment.class.getSimpleName();
    public static final String EXTRA_NEED_NOTIFICATION = "notificationRequired";
    private UpdateReceiver mUpdateReceiver;
    private ActionCallbacks mCallbacks;
    private SwipeRefreshLayout mSRLayout;
    private RecyclerView mTradersRv;
    private TradersAdapter mAdapter;
    private List<Trader> mTraders;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        startUpdateService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traders, parent, false);
        init();
        findViews(view);
        setupAdapter();
        setupListeners();
        return view;
    }

    private void init() {
        mUpdateReceiver = new UpdateReceiver();
    }

    private void findViews(View view) {
        mSRLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_FO);
        mSRLayout.setColorSchemeColors(R.color.primary_light, R.color.primary_dark);
        mTradersRv = (RecyclerView) view.findViewById(R.id.rvBankList_FO);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mTradersRv.setLayoutManager(layoutManager);

    }

    private void setupAdapter() {
        mTraders = getTradersData();
        mAdapter = new TradersAdapter(mTraders, mCallbacks);
        mTradersRv.setAdapter(mAdapter);
    }

    private void setupListeners() {
        mSRLayout.setOnRefreshListener(TradersFragment.this);
    }

    private void startUpdateService() {
        Intent updateIntent = new Intent(getActivity(), UpdateService.class);
        updateIntent.putExtra(EXTRA_NEED_NOTIFICATION, true);
        getActivity().startService(updateIntent);
    }

    private List<Trader> getTradersData() {
        final TraderDAO traderDAO = new TraderDAO(getActivity());
        return traderDAO.getAll();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        final MenuItem item = menu.findItem(R.id.action_search_AH);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Trader> filteredModelList = filter(mTraders, query);
        mAdapter.animateTo(filteredModelList);
        mTradersRv.scrollToPosition(0);
        return true;
    }

    private List<Trader> filter(List<Trader> traders, String query) {
        query = query.toLowerCase();

        final List<Trader> filteredModelList = new ArrayList<>();
        for (Trader trader : traders) {
            final String name = trader.getName().toLowerCase();
            final String region = trader.getRegion().toLowerCase();
            final String city = trader.getCity().toLowerCase();
            if (name.contains(query) || region.contains(query) || city.contains(query)) {
                filteredModelList.add(trader);
            }
        }
        return filteredModelList;
    }

    private void setAlarm() {
        new AlarmReceiver().setAlarm(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (ActionCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateService.ACTION_UPDATE_RESULT);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mUpdateReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mUpdateReceiver);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        startUpdateService();
    }

    private class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i(TAG, "Received broadcast intent: " + action);

            if (action.equals(UpdateService.ACTION_UPDATE_RESULT)) {
                switch (intent.getIntExtra(UpdateService.EXTRA_STATUS, -1)) {

                    case UpdateService.STATUS_RUNNING:
                        mSRLayout.setRefreshing(true);
                        break;

                    case UpdateService.STATUS_FINISHED_UPDATED:
                        setupAdapter();
                        setAlarm();
                        mSRLayout.setRefreshing(false);
                        break;

                    case UpdateService.STATUS_FINISHED_NO_NEED_FOR_UPDATE:
                        if (mTraders == null) setupAdapter();
                        mSRLayout.setRefreshing(false);
                        break;

                    case UpdateService.STATUS_ERROR:
                        Toast.makeText(getActivity(), getString(R.string.update_failed),
                                Toast.LENGTH_LONG).show();
                        setupAdapter();
                        mSRLayout.setRefreshing(false);
                        break;
                }
            }
        }
    }

}