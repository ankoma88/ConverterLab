package com.ankoma88.converterlab.models;


public class Offer {

    private int mId;
    private String mTraderOrgId;
    private String mCurrency;
    private double mAsk;
    private double mBid;
    private double mBidChange;
    private double mAskChange;

    public Offer() {
    }

    public Offer(String traderOrgId, String currency, double ask, double bid) {
        mTraderOrgId = traderOrgId;
        mCurrency = currency;
        mAsk = ask;
        mBid = bid;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTraderOrgId() {
        return mTraderOrgId;
    }

    public void setTraderOrgId(String traderOrgId) {
        mTraderOrgId = traderOrgId;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public double getAsk() {
        return mAsk;
    }

    public void setAsk(double ask) {
        mAsk = ask;
    }

    public double getBid() {
        return mBid;
    }

    public void setBid(double bid) {
        mBid = bid;
    }

    public double getBidChange() {
        return mBidChange;
    }

    public void setBidChange(double bidChange) {
        mBidChange = bidChange;
    }

    public double getAskChange() {
        return mAskChange;
    }

    public void setAskChange(double askChange) {
        mAskChange = askChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (Double.compare(offer.mAsk, mAsk) != 0) return false;
        if (Double.compare(offer.mBid, mBid) != 0) return false;
        if (!mTraderOrgId.equals(offer.mTraderOrgId)) return false;
        return mCurrency.equals(offer.mCurrency);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mTraderOrgId.hashCode();
        result = 31 * result + mCurrency.hashCode();
        temp = Double.doubleToLongBits(mAsk);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mBid);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "mTraderOrgId='" + mTraderOrgId + '\'' +
                ", mCurrency='" + mCurrency + '\'' +
                ", mAsk=" + mAsk +
                ", mBid=" + mBid +
                ", mBidChange=" + mBidChange +
                ", mAskChange=" + mAskChange +
                '}';
    }
}
