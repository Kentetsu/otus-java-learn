package ATM;

import java.util.Map;

public class ATM {

    private final CashRegister cashRegister;
    private int[] initArray = {5000, 1000, 500, 100, 50, 10};

    public ATM() {
        this.cashRegister = new CashRegister(initArray);
    }

    public int depositMoney(Map<Integer, Integer> mapOfBills) throws Exception {
        return cashRegister.addBills(mapOfBills);
    }

    public int withdrawMoney(int numberOfWithdraw) throws Exception {
        return cashRegister.removeBills(numberOfWithdraw);
    }

    public int getAmountOfMoney() {
        return cashRegister.getAmountOfMoney();
    }
}
