package com.ankoma88.converterlab.models;


public class Currency {

    private String mCurrencyName;
    private String mCurrencyTicker;

    public Currency() {
    }

    public Currency(String currencyTicker, String currencyName) {
        mCurrencyName = currencyName;
        mCurrencyTicker = currencyTicker;
    }

    public String getCurrencyName() {
        return mCurrencyName;
    }

    public void setCurrencyName(String currencyName) {
        mCurrencyName = currencyName;
    }

    public String getCurrencyTicker() {
        return mCurrencyTicker;
    }

    public void setCurrencyTicker(String currencyTicker) {
        mCurrencyTicker = currencyTicker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (!mCurrencyName.equals(currency.mCurrencyName)) return false;
        return mCurrencyTicker.equals(currency.mCurrencyTicker);

    }

    @Override
    public int hashCode() {
        int result = mCurrencyName.hashCode();
        result = 31 * result + mCurrencyTicker.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "mCurrencyName='" + mCurrencyName + '\'' +
                ", mCurrencyTicker='" + mCurrencyTicker + '\'' +
                '}';
    }
}
