package com.company.project;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Semaphore;

public class Employer implements Runnable {
    Semaphore EmployerReady;
    Semaphore ClientAvailable;
    Semaphore windowAvailable;
    private BlockingDeque<String> customersQueue;
    private String employerName;
    private BankFrame frame = BankFrame.GetInstanceGUI();

    public Employer(Semaphore EmployerReady,
                    Semaphore clientAvailable, BlockingDeque<String> clientQueue, String employerName,
                    int employerCount) {
        this.EmployerReady = EmployerReady;
        ClientAvailable = clientAvailable;
        this.customersQueue = clientQueue;
        this.employerName = employerName;
        windowAvailable = new Semaphore(employerCount);
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientAvailable.acquire();// customer enter shop
                windowAvailable.acquire(); // if chair availble
                servievClient();
            } catch (InterruptedException e) {
            }
        }
    }

    private void servievClient() throws InterruptedException {
        //giving 2-5 seconds for the servie
        String customer = customersQueue.take();
        int Id = getId(customer) - 1;

        frame.UpdateColumnWaitingSeats(Id, "");
        frame.UpdateBarberChair(Id, employerName +" Window");

        System.out.println(customer + " is serive  in " + employerName);
        long servie = Math.round(1 + 3 * Math.random());
        Thread.sleep(servie * 1000);

        frame.UpdateBarberChair(Id, "");
        frame.UpdateColumnServedBy(Id,"Exist From Bank "+ employerName +" taking time = "+servie*1000+"s");

        System.out.println(customer + " exits from Bank");
        windowAvailable.release();
        EmployerReady.release();
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
