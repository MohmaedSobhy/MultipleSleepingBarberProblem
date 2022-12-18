package com.company.project;

public class Customer implements Runnable{

   private String customerName;
   private int Id;
    public  Customer(String customerName,int Id) {
        this.customerName = customerName;
        this.Id=Id;
    }

    @Override
    public  void run() {
        Shop.getInstance().CustomerEnterSop(customerName,Id);
    }


}
