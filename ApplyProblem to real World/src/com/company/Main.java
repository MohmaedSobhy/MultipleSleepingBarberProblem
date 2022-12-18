package com.company;

import com.company.project.BankSystem;

public class Main {
    public static void main(String[] args) {
        BankSystem bank= BankSystem.getInstance(3,2);
        bank.OpenBank();
    }
}
