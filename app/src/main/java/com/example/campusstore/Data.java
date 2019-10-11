package com.example.campusstore;

public class Data
{
    private String item_name;
    private String item_price;
    private String item_shop;
    private String item_url;
    private String item_owner;

    public Data()
    {
    }

    public String getItem_owner() {
        return item_owner;
    }

    public void setItem_owner(String item_owner) {
        this.item_owner = item_owner;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public void setItem_shop(String item_shop) {
        this.item_shop = item_shop;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public String getItem_shop() {
        return item_shop;
    }

    public String getItem_url() {
        return item_url;
    }

    public Data(String name, String price, String shop, String owner ,String url)
    {
        item_name=name;
        item_price=price;
        item_shop=shop;
        item_url=url;
        item_owner=owner;
    }
}
