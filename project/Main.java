package com.company;

import com.company.project.Shop;

public class Main {
    public static void main(String[] args) {
        Shop shop=Shop.getInstance(3,2);
        shop.OpenShop();
    }
}
