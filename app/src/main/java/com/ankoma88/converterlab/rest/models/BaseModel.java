package com.ankoma88.converterlab.rest.models;


import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseModel {

    private String sourceId;
    private Date date;
    private Map<Integer, String> orgTypes;
    private Map<String, String> currencies;
    private Map<String, String> regions;
    private Map<String, String> cities;
    private List<Organization> organizations;

    public BaseModel() {
    }

    public BaseModel(String sourceId, Date date,
                     Map<Integer, String> orgTypes, Map<String, String> currencies,
                     Map<String, String> regions, Map<String, String> cities,
                     List<Organization> organizations) {
        this.sourceId = sourceId;
        this.date = date;
        this.organizations = organizations;
        this.orgTypes = orgTypes;
        this.currencies = currencies;
        this.regions = regions;
        this.cities = cities;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public Map<Integer, String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(Map<Integer, String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        if (sourceId != null ? !sourceId.equals(baseModel.sourceId) : baseModel.sourceId != null)
            return false;
        return !(date != null ? !date.equals(baseModel.date) : baseModel.date != null);

    }

    @Override
    public int hashCode() {
        int result = sourceId != null ? sourceId.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "sourceId='" + sourceId + '\'' +
                ", date=" + date +
                ", orgTypes=" + orgTypes +
                ", currencies=" + currencies +
                ", regions=" + regions +
                ", cities=" + cities +
                '}';
    }
}
