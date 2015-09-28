package com.ankoma88.converterlab.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.db.CurrencyDAO;
import com.ankoma88.converterlab.db.OfferDAO;
import com.ankoma88.converterlab.db.TraderDAO;
import com.ankoma88.converterlab.interfaces.ActionCallbacks;
import com.ankoma88.converterlab.models.Offer;
import com.ankoma88.converterlab.models.Trader;
import com.ankoma88.converterlab.views.OfferTitleView;
import com.ankoma88.converterlab.views.OfferView;
import com.ankoma88.converterlab.views.TraderInfoView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

/**Fragment with detailed info and offers of selected trader*/
public class OffersFragment extends Fragment implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {

    private static final String ARG_TRADER_ID = "TRADER_ID";
    private ActionCallbacks mCallbacks;
    private SwipeRefreshLayout mSRLayout;
    private FloatingActionButton mBtnMap, mBtnLink, mBtnCall;
    private LinearLayout mLlContent;
    private TraderInfoView mTraderInfoHeader;
    private OfferTitleView mOfferTitleHeader;
    private CurrencyDAO mCurrencyDAO;
    private List<Offer> mOffers;
    private Trader mTrader;
    private FrameLayout mFlTransparency;
    private FloatingActionsMenu mFabMenu;

    public static OffersFragment newInstance(int traderId) {
        Bundle args = new Bundle();
        args.putInt(ARG_TRADER_ID, traderId);
        OffersFragment offersFragment = new OffersFragment();
        offersFragment.setArguments(args);
        return offersFragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, parent, false);
        findViews(view);
        init();
        setupListeners();
        checkMenuExpanded();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupContent();
    }

    private void findViews(final View view) {
        mSRLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_FO);
        mSRLayout.setColorSchemeColors(R.color.primary_light, R.color.primary_dark);
        mBtnMap = (FloatingActionButton) view.findViewById(R.id.action_map_FO);
        mBtnLink = (FloatingActionButton) view.findViewById(R.id.action_link_FO);
        mBtnCall = (FloatingActionButton) view.findViewById(R.id.action_phone_FO);
        mLlContent = (LinearLayout) view.findViewById(R.id.linLayout_content_FO);
        mFlTransparency = (FrameLayout)view.findViewById(R.id.rootFl_FO);
        mFabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu_FO);
    }

    private void init() {
        final TraderDAO traderDAO = new TraderDAO(getActivity());
        final int traderId = getArguments().getInt(ARG_TRADER_ID);
        mTrader = traderDAO.get(traderId);
        mTraderInfoHeader = new TraderInfoView(getActivity());
        mOfferTitleHeader = new OfferTitleView(getActivity());
        mCurrencyDAO = new CurrencyDAO(getActivity());
    }

    private void setupListeners() {
        mSRLayout.setOnRefreshListener(OffersFragment.this);
        mBtnMap.setOnClickListener(OffersFragment.this);
        mBtnLink.setOnClickListener(OffersFragment.this);
        mBtnCall.setOnClickListener(OffersFragment.this);
        mFabMenu.setOnFloatingActionsMenuUpdateListener(OffersFragment.this);
    }

    private void setupContent() {
        mOffers = getOffersData(mTrader.getOrgId());
        addViewsToContent();
        mSRLayout.setRefreshing(false);
    }

    public List<Offer> getOffersData(String traderOrgId) {
        final OfferDAO offerDAO = new OfferDAO(getActivity());
        return offerDAO.getAllByTraderOrgId(traderOrgId);
    }

    private void addViewsToContent() {
        mLlContent.removeAllViews();
        mTraderInfoHeader.setView(mTrader);
        mLlContent.addView(mTraderInfoHeader);
        mLlContent.addView(mOfferTitleHeader);
        List<Offer> formattedOffers = new ArrayList<>(mOffers);
        for (Offer offer : formattedOffers) {
            offer.setCurrency(mCurrencyDAO.findCurrencyName(offer));
            OfferView offerView = new OfferView(getActivity());
            offerView.setView(offer);
            mLlContent.addView(offerView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateHome();
                return true;
            case R.id.action_share_AI:
                showShareDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showShareDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putInt("traderId", mTrader.getId());
        ShareDialogFragment shareDialog = new ShareDialogFragment();
        shareDialog.setArguments(args);
        shareDialog.show(fm, "dialog_share");
    }

    private void navigateHome() {
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
    }

    private void checkMenuExpanded() {
        mFlTransparency.setVisibility(getArguments().getBoolean("isMenuExpanded") ?
                View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.action_map_FO:
                mCallbacks.onMapClicked(mTrader.getId());
                break;
            case R.id.action_link_FO:
                mCallbacks.onLinkClicked(mTrader.getLink());
                break;
            case R.id.action_phone_FO:
                mCallbacks.onPhoneClicked(mTrader.getPhone());
                break;
        }
    }

    @Override
    public void onRefresh() {
        setupContent();
    }

    @Override
    public void onMenuExpanded() {
        mFlTransparency.setVisibility(View.VISIBLE);
        getArguments().putBoolean("isMenuExpanded", true);
    }

    @Override
    public void onMenuCollapsed() {
        mFlTransparency.setVisibility(View.INVISIBLE);
        getArguments().putBoolean("isMenuExpanded", false);
    }


}













