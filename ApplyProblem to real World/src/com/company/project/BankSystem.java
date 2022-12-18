package com.company.project;

import java.util.ArrayList;
import java.util.concurrent.*;

public class BankSystem {
    private static BankSystem bank;
    private int WeatingSeats;
    private int EmployerCount;

    private Semaphore EmployerReady;
    private Semaphore ClientIsReady;
    private Semaphore WeaitingSeatsAvailable;
    private BlockingDeque<String> clientQueue;
    private BankFrame frame;

    // use singleton pattern to create only one object
    // from class case i have only one shop

    private BankSystem(int weatingSeats, int EmployerCount) {
        WeatingSeats = weatingSeats;
        this.EmployerCount = EmployerCount;
        WeaitingSeatsAvailable = new Semaphore(weatingSeats);
    }

    public static BankSystem getInstance(int weatingSeats, int employerCount) {
        if (bank == null)
            bank = new BankSystem(weatingSeats, employerCount);
        return bank;
    }

    public static BankSystem getInstance() {
        // i writeit here if user forget to set weatingSeats and barberCount in shop
        if (bank == null) {
            bank = new BankSystem(10, 1);
        }
        return bank;
    }

    public void OpenBank() {
        frame = BankFrame.GetInstanceGUI(); // open Frame window
        System.out.println("Bank is Open");
        // set semaphore for barbers and customers
        EmployerReady = new Semaphore(EmployerCount);
        ClientIsReady = new Semaphore(0);
        clientQueue = new LinkedBlockingDeque<>(WeatingSeats);


        ExecutorService openShop = Executors.newWorkStealingPool();
        for(int i=1;i<=EmployerCount;i++)
            openShop.submit(new Employer(EmployerReady, ClientIsReady, clientQueue, "Employer" + i, EmployerCount));
        /*for (int i = 1; i <= EmployerCount; i++) {
            threads.add(new Thread(new Employer(EmployerReady, ClientIsReady, clientQueue, "Employer" + i, EmployerCount)));
        }*/

        /*for (Thread t : threads)
            t.start();*/

        int numberOfCustomer = 10;
        for (int i = 1; i <= numberOfCustomer; i++) {
            try {
                long haircutTime = Math.round(1 + 2 * Math.random());
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.AddNewCustomer("Client"+i);
            System.out.println("Client " + i + " Enter Bank");
            Thread thread = new Thread(new Client("Customer"+i,i));
            thread.start();
        }
    }

    public void CustomerEnterSop(String clientName,int Id) {
        Id--;
        try {
            // check if No Seats Available enter condion and will be in weating list
            // and waite untill be Available chair from weating seats
            if (WeaitingSeatsAvailable.availablePermits() == 0) {
                frame.UpdateRowWaitingList(Id,"Now Here");
                System.out.println(clientName + " in Waeting List");
            }
            WeaitingSeatsAvailable.acquire();
            // send signal to barber to tell him Iam Here
            //frame.ChangeLabelText(clientName+" Enter Shop");
            ClientIsReady.release();
            clientQueue.put(clientName);
            // if no chair availble i will set in weating Area
            if (EmployerReady.availablePermits() == 0 && clientQueue.size() <= WeatingSeats) {
                System.out.println(clientName + " in waeting seats");
                frame.UpdateRowWaitingList(Id,"");
                frame.UpdateColumnWaitingSeats(Id,"Now Here");
            }
            EmployerReady.acquire();
            WeaitingSeatsAvailable.release();
        } catch (Exception e) {
        }
    }

}
