package com.ankoma88.converterlab.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.db.CurrencyDAO;
import com.ankoma88.converterlab.db.OfferDAO;
import com.ankoma88.converterlab.db.TraderDAO;
import com.ankoma88.converterlab.fragments.TradersFragment;
import com.ankoma88.converterlab.models.Currency;
import com.ankoma88.converterlab.models.Offer;
import com.ankoma88.converterlab.models.Trader;
import com.ankoma88.converterlab.receivers.AlarmReceiver;
import com.ankoma88.converterlab.rest.RestClient;
import com.ankoma88.converterlab.rest.models.BaseModel;
import com.ankoma88.converterlab.rest.models.Organization;
import com.ankoma88.converterlab.rest.models.Quotation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This IntentService loads data from server and updates database.
 * AlarmReceiver holds a partial wake lock for this service while the service does its work.
 * When the service is finished, it calls to release the wake lock.
 */
public class UpdateService extends IntentService {
    public static final String TAG = UpdateService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 1;
    public static final String ACTION_UPDATE_RESULT = "com.ankoma88.converterlab.action.UPD_RESULT";
    public static final String EXTRA_STATUS = "UpdateStatus";
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED_UPDATED = 1;
    public static final int STATUS_FINISHED_NO_NEED_FOR_UPDATE = 2;
    public static final int STATUS_ERROR = 3;
    private static final String PREF_DATA_DATE = "dataDateTime";
    private boolean mIsNeedNotification;

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Update intent received");
        mIsNeedNotification = intent.getBooleanExtra(TradersFragment.EXTRA_NEED_NOTIFICATION, false);
        loadDataFromNetwork();
        AlarmReceiver.completeWakefulIntent(intent);
    }

    /**
     * Loads data from server
     */
    private void loadDataFromNetwork() {
        new RestClient().getApiService().getBaseModel(new Callback<BaseModel>() {
            @Override
            public void success(final BaseModel baseModel, Response response) {
                Log.i(TAG, "Base model loaded");
                if (baseModel.getDate().getTime() > getDataTime()) {
                    Log.i(TAG, "Received data is new: start update");
                    sendUpdateResultBroadcast(STATUS_RUNNING);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateDataFromBaseModel(baseModel);
                        }
                    }).start();

                } else {
                    Log.i(TAG, "Data in db is relevant: update not needed");
                    showNotificationIfNeeded(100, 100);
                    sendUpdateResultBroadcast(STATUS_FINISHED_NO_NEED_FOR_UPDATE);
                }

                removeNotificationIfNeeded();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error : " + error.getMessage());
                sendUpdateResultBroadcast(STATUS_ERROR);
            }
        });
    }

    /**
     * Notifies application about update status
     */
    private void sendUpdateResultBroadcast(int status) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_UPDATE_RESULT);
        broadcastIntent.putExtra(EXTRA_STATUS, status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.i(TAG, "Update complete broadcast sent with status: " + status);
    }

    /**
     * Converts base model to domain models and saves them to database
     */
    private void updateDataFromBaseModel(BaseModel baseModel) {
        final List<Trader> traders = new ArrayList<>();
        final List<Offer> offers = new ArrayList<>();
        final List<Currency> currencies = new ArrayList<>();
        parseDataFromBaseModel(baseModel, traders, offers, currencies);
        saveData(traders, offers, currencies);
        saveDataTime(baseModel.getDate());
        sendUpdateResultBroadcast(STATUS_FINISHED_UPDATED);
        Log.i(TAG, "Data updated");
    }

    /**
     * Parses data from base model
     */
    private void parseDataFromBaseModel(BaseModel baseModel,
                                        List<Trader> traders,
                                        List<Offer> offers,
                                        List<Currency> currencies) {

        final Map<String, String> currencyTickerNameMap = baseModel.getCurrencies();

        for (Map.Entry<String, String> entry : currencyTickerNameMap.entrySet()) {
            Currency currency = createCurrencyModel(entry.getKey(), entry.getValue());
            currencies.add(currency);
        }


        final List<Organization> organizations = baseModel.getOrganizations();

        //Needed for progress update
        float total = organizations.size();
        float current = 1;

        for (Organization organization : organizations) {
            Trader trader = createTraderModel(baseModel, organization);
            traders.add(trader);

            final Map<String, Quotation> currencyRateMap = organization.getCurrencies();
            for (Map.Entry<String, Quotation> exchangeRate : currencyRateMap.entrySet()) {
                Offer offer = createOfferModel(organization, exchangeRate);
                offers.add(offer);
            }

            current++;
            showNotificationIfNeeded(total, current);
        }

    }

    /**
     * Creates currency trader model
     */
    @NonNull
    private Trader createTraderModel(BaseModel baseModel, Organization organization) {
        Trader trader = new Trader();
        trader.setOrgId(organization.getId());
        trader.setName(organization.getTitle());
        trader.setType(organization.getOrgType() == 1 ?
                getString(R.string.bank) :
                getString(R.string.exchange_office));
        trader.setAddress(organization.getAddress());
        trader.setPhone(organization.getPhone());
        trader.setRegion(baseModel.getRegions().get(organization.getRegionId()));
        trader.setCity(baseModel.getCities().get(organization.getCityId()));
        trader.setLink(organization.getLink());
        return trader;
    }

    /**
     * Creates currency exchange market offer model
     */
    @NonNull
    private Offer createOfferModel(Organization organization,
                                   Map.Entry<String, Quotation> exchangeRate) {
        Offer offer = new Offer();
        offer.setTraderOrgId(organization.getId());
        offer.setCurrency(exchangeRate.getKey());
        offer.setAsk(exchangeRate.getValue().getAsk());
        offer.setBid(exchangeRate.getValue().getBid());
        return offer;
    }

    /**
     * Creates currency model
     */
    @NonNull
    private Currency createCurrencyModel(String currencyTicker, String currencyName) {
        return new Currency(currencyTicker, currencyName);
    }

    /**
     * Saves models to database
     */
    private void saveData(List<Trader> traders, List<Offer> offers, List<Currency> currencies) {
        new TraderDAO(this).saveAll(traders);
        new OfferDAO(this).saveAll(offers);
        new CurrencyDAO(this).saveAll(currencies);
    }

    /**
     * Stores time of latest data change on server
     */
    private void saveDataTime(Date date) {
        SharedPreferences preferences = getSharedPreferences(PREF_DATA_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit()
                .putLong(PREF_DATA_DATE, date.getTime());
        editor.apply();
    }

    /**
     * Gets time of latest server data change
     */
    private long getDataTime() {
        SharedPreferences preferences = getSharedPreferences(PREF_DATA_DATE, Context.MODE_PRIVATE);
        return preferences.getLong(PREF_DATA_DATE, 0);
    }

    /**
     * Shows update progress notification if application is running
     */
    private void showNotificationIfNeeded(float total, float current) {
        if (mIsNeedNotification) {
            int progressPercent = (int) ((current * 100) / total);
            showNotification(progressPercent);
        }
    }

    /**
     * Shows update progress percentage in notification
     */
    private void showNotification(int percent) {
        sleep(10);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.update_message))
                .setContentText(getString(R.string.updating_data))
                .setTicker(getString(R.string.notification_ticker))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setProgress(100, percent, false)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(percent + "%"))
                .setOngoing(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Cancels ongoing notification after some time
     */
    private void removeNotificationIfNeeded() {
        if (mIsNeedNotification) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(5000);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(NOTIFICATION_ID);
                }
            }).start();
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
