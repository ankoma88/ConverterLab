package com.ankoma88.converterlab.models;


public class Trader {

    private int mId;
    private String mOrgId;
    private String mName;
    private String mType;
    private String mRegion;
    private String mCity;
    private String mAddress;
    private String mPhone;
    private String mLink;

    public Trader() {
    }

    public Trader(String orgId, String name, String type, String region,
                  String city, String address, String phone, String link) {
        mOrgId = orgId;
        mName = name;
        mType = type;
        mRegion = region;
        mCity = city;
        mAddress = address;
        mPhone = phone;
        mLink = link;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOrgId() {
        return mOrgId;
    }

    public void setOrgId(String orgId) {
        mOrgId = orgId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trader trader = (Trader) o;

        if (!mOrgId.equals(trader.mOrgId)) return false;
        if (!mName.equals(trader.mName)) return false;
        return mType.equals(trader.mType);

    }

    @Override
    public int hashCode() {
        int result = mOrgId.hashCode();
        result = 31 * result + mName.hashCode();
        result = 31 * result + mType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Trader{" +
                "mOrgId='" + mOrgId + '\'' +
                ", mName='" + mName + '\'' +
                ", mType='" + mType + '\'' +
                ", mRegion='" + mRegion + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mLink='" + mLink + '\'' +
                '}';
    }
}
