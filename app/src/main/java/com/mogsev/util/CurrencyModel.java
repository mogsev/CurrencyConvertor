package com.mogsev.util;

/**
 * Created by zhenya on 30.07.2015.
 */
public class CurrencyModel {
    private String code;
    private String name;
    private String nameEnglish;

    public CurrencyModel() {
        code = "";
        name = "";
        nameEnglish = "";
    }

    public CurrencyModel(String code) {
        this();
        this.code = code;
    }

    public CurrencyModel(String code, String name) {
        this(code);
        this.name = name;
    }

    public CurrencyModel(String code, String name, String nameEnglish) {
        this.code = code;
        this.name = name;
        this.nameEnglish = nameEnglish;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    @Override
    public String toString() {
        return "Code: " + code + "\t" + "Name: " + name + "\t" + "Name english: " + nameEnglish;
    }
}
