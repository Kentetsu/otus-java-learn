package ru.eon;

import java.util.TreeSet;

public class CashRegister {
    private TreeSet<Cell> cellBlock;
    public CashRegister(int[] arrayOfDenominations){
        for(int denomination: arrayOfDenominations){
            cellBlock.add(new Cell(denomination));
        }
    }

    public int getAmountOfMoney() {
        var totalValue = 0;
        for(Cell cell: cellBlock){
            var count = cell.getCount();
            var denomination = cell.getDenomination();
            totalValue += count * denomination;
        }
        return totalValue;
    }

    public int addBills(int count, int denomination) {

    }

    public int removeBills(int count, int denomination) {

    }
}
