package ru.eon;


public class ATM {
    private final CashRegister cashRegister;
    public ATM(){
        this.cashRegister = new CashRegister(new int[] {5000, 1000, 500, 100, 50, 10});
    }

    private int depositMoney(int count, int denomination){

        return cashRegister.addBills(count, denomination);
    }

    private int withdrawMoney(int count, int denomination){

        return cashRegister.removeBills(count, denomination);
    }
}
