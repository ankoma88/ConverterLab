package com.ankoma88.converterlab.rest.models;


import java.util.Map;

public class Organization {

    private String id;
    private int oldId;
    private int orgType;
    private String title;
    private String regionId;
    private String cityId;
    private String phone;
    private String address;
    private String link;
    private Map<String, Quotation> currencies;

    public Organization() {
    }

    public Organization(String id, int oldId, int orgType, String title,
                        String regionId, String cityId, String phone, String address,
                        String link, Map<String, Quotation> currencies) {
        this.id = id;
        this.oldId = oldId;
        this.orgType = orgType;
        this.title = title;
        this.regionId = regionId;
        this.cityId = cityId;
        this.phone = phone;
        this.address = address;
        this.link = link;
        this.currencies = currencies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Map<String, Quotation> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, Quotation> currencies) {
        this.currencies = currencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (oldId != that.oldId) return false;
        if (!id.equals(that.id)) return false;
        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + oldId;
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", oldId=" + oldId +
                ", orgType=" + orgType +
                ", title='" + title + '\'' +
                ", regionId='" + regionId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
