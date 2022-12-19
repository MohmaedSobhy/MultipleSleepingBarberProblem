package com.company.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ShopFrame extends JFrame {
      private JTable table;
      private  DefaultTableModel tableModel;
      private static ShopFrame frame;

    private ShopFrame(){
        String column[]={"Name","WaitingList","WaitingSeats","ServedBy"};
          tableModel = new DefaultTableModel();
         table = new JTable(tableModel);
         tableModel.addColumn("Name");
         tableModel.addColumn("WaitingList");
         tableModel.addColumn("WaitingSeats");
         tableModel.addColumn("BarberChair");
         tableModel.addColumn("ServedBy");
         JScrollPane scrollPane=new JScrollPane(table);
         this.setTitle("Barber Shop");
         this.add(scrollPane);
         this.setSize(500,500);
         this.setVisible(true);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static ShopFrame GetInstanceGUI(){
        if(frame==null)
            frame=new ShopFrame();

        return frame;
    }

    public  void AddNewCustomer(String name){
      frame.tableModel.addRow(new Object[]{name,"", "","","",""});
    }

    public  void UpdateRowWaitingList(int Id, String value){
        int indexColumnWaitingList = 1;
        frame.tableModel.setValueAt(value,Id, indexColumnWaitingList);
    }

    public  void UpdateColumnWaitingSeats(int Id,String value){
        int indexColumnWaitingSeats = 2;
        frame.tableModel.setValueAt(value,Id, indexColumnWaitingSeats);
    }

    public  void UpdateColumnServedBy(int Id,String value){
        int indexColumnServedBy = 4;
        frame.tableModel.setValueAt(value,Id, indexColumnServedBy);
    }

    public void UpdateBarberChair(int Id,String value){
        int indexColumnChair=3;
        frame.tableModel.setValueAt(value,Id,indexColumnChair);
    }

}
