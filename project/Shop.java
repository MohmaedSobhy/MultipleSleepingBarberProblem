package com.company.project;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class Shop {
    private static Shop shop;
    private int WeatingSeats;
    private int barberCount;

    private Semaphore BarberIsReady;
    private Semaphore CustomerIsReady;
    private Semaphore WeaitingSeatsAvailable;
    private BlockingDeque<String> customersQueue;

    private ShopFrame frame;

    // use singleton pattern to create only one object
    // from class case i have only one shop
    private Shop(int weatingSeats, int barberCount) {
        WeatingSeats = weatingSeats;
        this.barberCount = barberCount;
        WeaitingSeatsAvailable = new Semaphore(weatingSeats);
    }

    public static Shop getInstance(int weatingSeats, int barberCount) {
        if (shop == null)
            shop = new Shop(weatingSeats, barberCount);

        return shop;
    }

    public static Shop getInstance() {
        // i writeit here if user forget to set weatingSeats and barberCount in shop
        if (shop == null) {
            shop = new Shop(10, 1);
        }
        return shop;
    }

    

}
