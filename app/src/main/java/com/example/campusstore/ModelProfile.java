package com.example.campusstore;

public class ModelProfile
{
    String name,email,phone,item_url,key,shop;

    public ModelProfile()
    {
    }

    public ModelProfile(String key,String name, String email, String phone, String item_url,String shop) {
        this.key=key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.item_url = item_url;
        this.shop=shop;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

}
