package com.ankoma88.converterlab.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ankoma88.converterlab.models.Trader;

import java.util.ArrayList;
import java.util.List;

public class TraderDAO extends EntityDBDAO {

    public static final String TAG = TraderDAO.class.getSimpleName();
    private static final String WHERE_ID_EQUALS = DBHelper.ID_COLUMN + " = ?";

    public TraderDAO(Context context) {
        super(context);
    }

    public void saveAll(List<Trader> traders) {

        deleteAll();
        for (Trader trader : traders) {
            save(trader);
        }
        Log.d(TAG, "Traders updated");
    }

    public void deleteAll() {
        String sql = "DELETE FROM " + DBHelper.TRADER_TABLE;
        database.execSQL(sql);
    }

    public List<Trader> getAll() {

        List<Trader> traders = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TRADER_TABLE,
                new String[]{DBHelper.ID_COLUMN,
                        DBHelper.TRADER_ORG_ID_COLUMN,
                        DBHelper.TRADER_NAME_COLUMN,
                        DBHelper.TRADER_TYPE_COLUMN,
                        DBHelper.TRADER_REGION_COLUMN,
                        DBHelper.TRADER_CITY_COLUMN,
                        DBHelper.TRADER_ADDRESS_COLUMN,
                        DBHelper.TRADER_PHONE_COLUMN,
                        DBHelper.TRADER_LINK_COLUMN},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Trader trader = new Trader();
            trader.setId(cursor.getInt(0));
            trader.setOrgId(cursor.getString(1));
            trader.setName(cursor.getString(2));
            trader.setType(cursor.getString(3));
            trader.setRegion(cursor.getString(4));
            trader.setCity(cursor.getString(5));
            trader.setAddress(cursor.getString(6));
            trader.setPhone(cursor.getString(7));
            trader.setLink(cursor.getString(8));
            traders.add(trader);
        }

        return traders;
    }

    public long save(Trader trader) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.TRADER_ORG_ID_COLUMN, trader.getOrgId());
        values.put(DBHelper.TRADER_NAME_COLUMN, trader.getName());
        values.put(DBHelper.TRADER_TYPE_COLUMN, trader.getType());
        values.put(DBHelper.TRADER_REGION_COLUMN, trader.getRegion());
        values.put(DBHelper.TRADER_CITY_COLUMN, trader.getCity());
        values.put(DBHelper.TRADER_ADDRESS_COLUMN, trader.getAddress());
        values.put(DBHelper.TRADER_PHONE_COLUMN, trader.getPhone());
        values.put(DBHelper.TRADER_LINK_COLUMN, trader.getLink());

        return database.insert(DBHelper.TRADER_TABLE, null, values);
    }

    public Trader get(int id) {

        Trader trader = null;
        String sql = "SELECT * FROM " + DBHelper.TRADER_TABLE + " WHERE " + WHERE_ID_EQUALS;
        Cursor cursor = database.rawQuery(sql, new String[]{id + ""});

        if (cursor.moveToNext()) {
            trader = new Trader();
            trader.setId(cursor.getInt(0));
            trader.setOrgId(cursor.getString(1));
            trader.setName(cursor.getString(2));
            trader.setType(cursor.getString(3));
            trader.setRegion(cursor.getString(4));
            trader.setCity(cursor.getString(5));
            trader.setAddress(cursor.getString(6));
            trader.setPhone(cursor.getString(7));
            trader.setLink(cursor.getString(8));
        }

        return trader;
    }

    public int delete(Trader trader) {
        return database.delete(DBHelper.TRADER_TABLE, WHERE_ID_EQUALS,
                new String[]{trader.getId() + ""});
    }


}
