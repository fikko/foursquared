/**
 * Copyright 2009 Joe LaPenna
 */

package com.joelapenna.foursquare.types;

;

/**
 * Auto-generated: 2009-08-05 21:30:26.446954
 * 
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public class Venue implements FoursquareType {

    private String mAddress;
    private String mCity;
    private String mCrossstreet;
    private String mDistance;
    private String mGeolat;
    private String mGeolong;
    private String mId;
    private String mName;
    private String mPhone;
    private String mState;
    private Stats mStats;
    private Group mTips;
    private Group mTodos;
    private String mZip;

    public Venue() {
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCrossstreet() {
        return mCrossstreet;
    }

    public void setCrossstreet(String crossstreet) {
        mCrossstreet = crossstreet;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getGeolat() {
        return mGeolat;
    }

    public void setGeolat(String geolat) {
        mGeolat = geolat;
    }

    public String getGeolong() {
        return mGeolong;
    }

    public void setGeolong(String geolong) {
        mGeolong = geolong;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Stats getStats() {
        return mStats;
    }

    public void setStats(Stats stats) {
        mStats = stats;
    }

    public Group getTips() {
        return mTips;
    }

    public void setTips(Group tips) {
        mTips = tips;
    }

    public Group getTodos() {
        return mTodos;
    }

    public void setTodos(Group todos) {
        mTodos = todos;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

}
