package com.ankoma88.converterlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ankoma88.converterlab.models.Offer;

import java.util.ArrayList;
import java.util.List;

public class OfferDAO extends EntityDBDAO {

    public static final String TAG = OfferDAO.class.getSimpleName();
    private static final String WHERE_ID_EQUALS = DBHelper.ID_COLUMN + " = ?";

    public OfferDAO(Context context) {
        super(context);
    }

    public void saveAll(List<Offer> offers) {

        for (Offer offer : offers) {
            Offer oldOffer = getByTraderAndCurrency(
                    offer.getTraderOrgId(), offer.getCurrency());

            if (oldOffer != null) {
                double askChange = offer.getAsk() - oldOffer.getAsk();
                double bidChange = offer.getBid() - oldOffer.getBid();
                offer.setAskChange(askChange);
                offer.setBidChange(bidChange);

                delete(oldOffer);
            }

            save(offer);
        }
        Log.d(TAG, "Offers updated");
    }

    public List<Offer> getAllByTraderOrgId(String traderOrgId) {

        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM " + DBHelper.OFFER_TABLE
                + " WHERE " + DBHelper.OFFER_TRADER_ORG_ID_COLUMN + "= ?";
        Cursor cursor = database.rawQuery(sql, new String[]{traderOrgId});

        while (cursor.moveToNext()) {
            Offer offer = new Offer();
            offer.setId(cursor.getInt(0));
            offer.setTraderOrgId(cursor.getString(1));
            offer.setCurrency(cursor.getString(2));
            offer.setAsk(cursor.getDouble(3));
            offer.setBid(cursor.getDouble(4));
            offer.setAskChange(cursor.getDouble(5));
            offer.setBidChange(cursor.getDouble(6));

            offers.add(offer);
        }

        return offers;
    }

    public long save(Offer offer) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.OFFER_TRADER_ORG_ID_COLUMN, offer.getTraderOrgId());
        values.put(DBHelper.OFFER_CURRENCY_COLUMN, offer.getCurrency());
        values.put(DBHelper.OFFER_ASK_COLUMN, offer.getAsk());
        values.put(DBHelper.OFFER_BID_COLUMN, offer.getBid());
        values.put(DBHelper.OFFER_ASK_CHANGE_COLUMN, offer.getAskChange());
        values.put(DBHelper.OFFER_BID_CHANGE_COLUMN, offer.getBidChange());

        return database.insert(DBHelper.OFFER_TABLE, null, values);
    }

    public int delete(Offer offer) {
        return database.delete(DBHelper.OFFER_TABLE, WHERE_ID_EQUALS,
                new String[]{offer.getId() + ""});
    }

    public Offer get(int id) {

        Offer offer = null;
        String sql = "SELECT * FROM " + DBHelper.OFFER_TABLE +
                " WHERE " + WHERE_ID_EQUALS;
        Cursor cursor = database.rawQuery(sql, new String[]{id + ""});

        if (cursor.moveToNext()) {
            offer = new Offer();
            offer.setId(cursor.getInt(0));
            offer.setTraderOrgId(cursor.getString(1));
            offer.setCurrency(cursor.getString(2));
            offer.setAsk(cursor.getDouble(3));
            offer.setBid(cursor.getDouble(4));
            offer.setAskChange(cursor.getDouble(5));
            offer.setBidChange(cursor.getDouble(6));
        }

        return offer;
    }

    public Offer getByTraderAndCurrency(String traderOrgId, String currency) {

        Offer offer = null;
        String sql = "SELECT * FROM " + DBHelper.OFFER_TABLE
                    + " WHERE " + DBHelper.OFFER_TRADER_ORG_ID_COLUMN + "= ?"
                    + " AND " + DBHelper.OFFER_CURRENCY_COLUMN + "= ?";
        Cursor cursor = database.rawQuery(sql, new String[]{traderOrgId, currency});

        if (cursor.moveToNext()) {
            offer = new Offer();
            offer.setId(cursor.getInt(0));
            offer.setTraderOrgId(cursor.getString(1));
            offer.setCurrency(cursor.getString(2));
            offer.setAsk(cursor.getDouble(3));
            offer.setBid(cursor.getDouble(4));
            offer.setAskChange(cursor.getDouble(5));
            offer.setBidChange(cursor.getDouble(6));
        }
        return offer;
    }

    public void deleteAll() {
        String sql = "DELETE FROM " + DBHelper.OFFER_TABLE;
        database.execSQL(sql);
    }

}
