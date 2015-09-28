package com.ankoma88.converterlab.rest.models;


public class Quotation {
    private double bid;
    private double ask;

    public Quotation() {
    }

    public Quotation(double bid, double ask) {
        this.bid = bid;
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quotation quotation = (Quotation) o;

        if (Double.compare(quotation.bid, bid) != 0) return false;
        return Double.compare(quotation.ask, ask) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(bid);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ask);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
