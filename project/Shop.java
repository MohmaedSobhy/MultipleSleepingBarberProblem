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

    public void OpenShop() {
        frame = ShopFrame.GetInstanceGUI(); // open Frame window
        System.out.println("Salon is Open");
        // set semaphore for barbers and customers
        BarberIsReady = new Semaphore(barberCount);
        CustomerIsReady = new Semaphore(0);
        customersQueue = new LinkedBlockingDeque<>(WeatingSeats);
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= barberCount; i++) {
            threads.add(new Thread(new Barber(BarberIsReady, CustomerIsReady, customersQueue, "Barber" + i, barberCount)));
        }

        for (Thread t : threads)
            t.start();

        int numberOfCustomer = 20;
        for (int i = 1; i <= numberOfCustomer; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.AddNewCustomer("Customer"+i);
            System.out.println("Customer " + i + " Enter Shop");
            Thread thread = new Thread(new Customer("Customer"+i,i));
            thread.start();
        }
    }

    public void CustomerEnterSop(String customerName,int Id) {
        Id--;
        try {
            // check if No Seats Available enter condion and will be in weating list
            // and waite untill be Available chair from weating seats
            if (WeaitingSeatsAvailable.availablePermits() == 0) {
                frame.UpdateRowWaitingList(Id,"Now Here");
                System.out.println(customerName + " in Waeting List");
            }
            WeaitingSeatsAvailable.acquire();

            // send signal to barber to tell him Iam Here
            //frame.ChangeLabelText(customerName+" Enter Shop");
            CustomerIsReady.release();
            customersQueue.put(customerName);
            // if no chair availble i will set in weating Area
            if (BarberIsReady.availablePermits() == 0 && customersQueue.size() <= WeatingSeats) {
                System.out.println(customerName + " in waeting seats");
                frame.UpdateRowWaitingList(Id,"");
                frame.UpdateColumnWaitingSeats(Id,"Now Here");
            }
            BarberIsReady.acquire();
            WeaitingSeatsAvailable.release();
        } catch (Exception e) {
        }
    }

}
