package com.company.project;

public class Client implements Runnable{

   private String clientName;
   private int Id;
    public Client(String customerName, int Id) {
        this.clientName = customerName;
        this.Id=Id;
    }

    @Override
    public  void run() {
        BankSystem.getInstance().CustomerEnterSop(clientName,Id);
    }


}
