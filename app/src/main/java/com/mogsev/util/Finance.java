package com.mogsev.util;

import java.util.List;

/**
 * Created by zhenya on 06.08.2015.
 */
public class Finance {
    private String title;
    private String region;
    private String city;
    private String phone;
    private String address;
    private List<CurrencyInformer> currencies;

    private Finance(Builder builder) {
        this.title = builder.title;
        this.region = builder.region;
        this.city = builder.city;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    public String getTitle() {
        return title;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public List<CurrencyInformer> getCurrencies() {
        return currencies;
    }

    public static class Builder {
        private String title;
        private String region;
        private String city;
        private String phone;
        private String address;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Finance build() {
            return new Finance(this);
        }
    }

    @Override
    public String toString() {
        return "Title: " + this.title + "\t" + "Region: " + this.region + "\t"
                + "City: " + this.city + "\t" + "Phone: " + this.phone + "Address: " + this.address;
    }
}
