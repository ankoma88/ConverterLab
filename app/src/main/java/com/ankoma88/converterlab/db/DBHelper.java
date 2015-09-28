package com.ankoma88.converterlab.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "curExDB";
    private static final int DB_VERSION = 1;

    public static final String TRADER_TABLE = "traderTable";
    public static final String OFFER_TABLE = "offerTable";
    public static final String CURRENCY_TABLE = "currencyTable";

    public static final String ID_COLUMN = "_id";

    public static final String TRADER_ORG_ID_COLUMN = "org_id";
    public static final String TRADER_NAME_COLUMN = "name";
    public static final String TRADER_TYPE_COLUMN = "type";
    public static final String TRADER_REGION_COLUMN = "region";
    public static final String TRADER_CITY_COLUMN = "city";
    public static final String TRADER_ADDRESS_COLUMN = "address";
    public static final String TRADER_PHONE_COLUMN = "phone";
    public static final String TRADER_LINK_COLUMN = "link";

    public static final String OFFER_TRADER_ORG_ID_COLUMN = "trader_org_id";
    public static final String OFFER_CURRENCY_COLUMN = "currency";
    public static final String OFFER_ASK_COLUMN = "ask";
    public static final String OFFER_BID_COLUMN = "bid";
    public static final String OFFER_ASK_CHANGE_COLUMN = "ask_change";
    public static final String OFFER_BID_CHANGE_COLUMN = "bid_change";

    public static final String CURRENCY_TICKER_COLUMN = "currency_ticker";
    public static final String CURRENCY_NAME_COLUMN = "currency_name";

    public static final String CREATE_TRADER_TABLE = "CREATE TABLE "
            + TRADER_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + TRADER_ORG_ID_COLUMN + " TEXT, "
            + TRADER_NAME_COLUMN + " TEXT, "
            + TRADER_TYPE_COLUMN + " TEXT, "
            + TRADER_REGION_COLUMN + " TEXT, "
            + TRADER_CITY_COLUMN + " TEXT, "
            + TRADER_ADDRESS_COLUMN + " TEXT, "
            + TRADER_PHONE_COLUMN + " TEXT, "
            + TRADER_LINK_COLUMN + " TEXT);";

    public static final String CREATE_OFFER_TABLE = "CREATE TABLE "
            + OFFER_TABLE + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY, "
            + OFFER_TRADER_ORG_ID_COLUMN + " TEXT, "
            + OFFER_CURRENCY_COLUMN + " TEXT, "
            + OFFER_ASK_COLUMN + " REAL, "
            + OFFER_BID_COLUMN + " REAL, "
            + OFFER_ASK_CHANGE_COLUMN + " REAL, "
            + OFFER_BID_CHANGE_COLUMN + " REAL, "
            + "FOREIGN KEY(" + OFFER_TRADER_ORG_ID_COLUMN
            + ") REFERENCES " + TRADER_TABLE + "(" + TRADER_ORG_ID_COLUMN + "));";

    public static final String CREATE_CURRENCY_TABLE = "CREATE TABLE "
            + CURRENCY_TABLE + "("
            + CURRENCY_TICKER_COLUMN + " TEXT PRIMARY KEY, "
            + CURRENCY_NAME_COLUMN + " TEXT);";

    private static DBHelper instance;

    public static synchronized DBHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRADER_TABLE);
        db.execSQL(CREATE_OFFER_TABLE);
        db.execSQL(CREATE_CURRENCY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
