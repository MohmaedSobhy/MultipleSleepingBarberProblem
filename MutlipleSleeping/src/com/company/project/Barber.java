package com.company.project;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Semaphore;

public class Barber implements Runnable {
    Semaphore BarberReady;
    Semaphore CustomerAvailbel;
    Semaphore ChairAvailbel;
    private BlockingDeque<String> customersQueue;
    private String barberName;
    private ShopFrame frame = ShopFrame.GetInstanceGUI();

    public Barber(Semaphore barberReady,
                  Semaphore customerAvailbel, BlockingDeque<String> customersQueue, String barberName,
                  int BarberCount) {
        BarberReady = barberReady;
        CustomerAvailbel = customerAvailbel;
        this.customersQueue = customersQueue;
        this.barberName = barberName;
        ChairAvailbel = new Semaphore(BarberCount);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CustomerAvailbel.acquire();// customer enter shop
                ChairAvailbel.acquire(); // if chair availble
                getHaircut();
            } catch (InterruptedException e) {
            }
        }
    }

    private void getHaircut() throws InterruptedException {
        //giving 2-5 seconds for the haircut
        String customer = customersQueue.take();
        int Id = getId(customer) - 1;

        frame.UpdateColumnWaitingSeats(Id, "");
        frame.UpdateBarberChair(Id, barberName+" Chair");

        System.out.println(customer + " is getting hair cut in " + barberName);
        long haircutTime = Math.round(1 + 3 * Math.random());
        Thread.sleep(haircutTime * 1000);

        frame.UpdateBarberChair(Id, "");
        frame.UpdateColumnServedBy(Id,"Exist From Shop "+barberName+" taking time = "+haircutTime*1000+"s");

        System.out.println(customer + " exits from shop");
        ChairAvailbel.release();
        BarberReady.release();
    }

    public int getId(String customerName) {
        int Id = 0;
        for (int i = 0; i < customerName.length(); i++) {
            if (customerName.charAt(i) >= '0' && customerName.charAt(i) <= '9')
                Id = Id * 10 + Integer.parseInt(String.valueOf(customerName.charAt(i)));
        }
        return Id;
    }


}
