package com.ankoma88.converterlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ankoma88.converterlab.models.Currency;
import com.ankoma88.converterlab.models.Offer;

import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO extends EntityDBDAO {

    public static final String TAG = CurrencyDAO.class.getSimpleName();
    private static final String WHERE_TICKER_EQUALS = DBHelper.CURRENCY_TICKER_COLUMN + " = ?";

    public CurrencyDAO(Context context) {
        super(context);
    }

    public void saveAll(List<Currency> currencies) {

        deleteAll();
        for (Currency currency : currencies) {
            save(currency);
        }
        Log.d(TAG, "Traders updated");
    }

    public void deleteAll() {
        String sql = "DELETE FROM " + DBHelper.CURRENCY_TABLE;
        database.execSQL(sql);
    }

    public List<Currency> getAll() {

        List<Currency> currencies = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.CURRENCY_TABLE,
                new String[]{DBHelper.CURRENCY_TICKER_COLUMN, DBHelper.CURRENCY_NAME_COLUMN},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Currency currency = new Currency();
            currency.setCurrencyTicker(cursor.getString(0));
            currency.setCurrencyName(cursor.getString(1));
            currencies.add(currency);
        }

        return currencies;
    }

    public long save(Currency currency) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.CURRENCY_TICKER_COLUMN, currency.getCurrencyTicker());
        values.put(DBHelper.CURRENCY_NAME_COLUMN, currency.getCurrencyName());

        return database.insert(DBHelper.CURRENCY_TABLE, null, values);
    }

    public Currency get(String ticker) {
        Currency currency = null;
        String sql = "SELECT * FROM " + DBHelper.CURRENCY_TABLE + " WHERE " + WHERE_TICKER_EQUALS;
        Cursor cursor = database.rawQuery(sql, new String[]{ticker});

        if (cursor.moveToNext()) {
            currency = new Currency();
            currency.setCurrencyTicker(cursor.getString(0));
            currency.setCurrencyName(cursor.getString(1));
        }

        return currency;
    }

    public String findCurrencyName(Offer offer) {
        String currencyTicker = offer.getCurrency();
        Currency currency = get(currencyTicker);
        return currency.getCurrencyName();
    }


}
