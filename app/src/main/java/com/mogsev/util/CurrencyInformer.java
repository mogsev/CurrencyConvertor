package com.mogsev.util;

/**
 * Created by zhenya on 03.08.2015.
 */
public class CurrencyInformer extends CurrencyModel {
    private String buy;
    private String sale;
    private String buyDelda;
    private String saleDelta;

    public CurrencyInformer(String code) {
        super(code);
    }

    private CurrencyInformer(Builder builder) {
        this.setCode(builder.code);
        this.setName(builder.name);
        this.buy = builder.buy;
        this.sale = builder.sale;
        this.buyDelda = builder.buyDelda;
        this.saleDelta = builder.buyDelda;
    }

    public String getBuy() {
        return buy;
    }

    public String getSale() {
        return sale;
    }

    public String getBuyDelda() {
        return buyDelda;
    }

    public String getSaleDelta() {
        return saleDelta;
    }

    public static class Builder {
        private final String code;
        private String name;
        private String buy;
        private String sale;
        private String buyDelda;
        private String saleDelta;

        public Builder(String code) {
            this.code = code;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder buy(String buy) {
            this.buy = buy;
            return this;
        }

        public Builder sale(String sale) {
            this.sale = sale;
            return this;
        }

        public Builder buyDelta(String buyDelda) {
            this.buyDelda = buyDelda;
            return this;
        }

        public Builder saleDelta(String saleDelta) {
            this.saleDelta = saleDelta;
            return this;
        }

        public CurrencyInformer build() {
            return new CurrencyInformer(this);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Buy: " + this.buy + "\t" + "Sale: " + this.sale + "\t";
    }
}
