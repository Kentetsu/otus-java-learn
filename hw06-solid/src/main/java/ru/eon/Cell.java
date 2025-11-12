package ru.eon;

public class Cell {
    private int denomination;
    private int countOfBanknotes;
//    private int maxCapacity;

    public Cell(int denomination){
        this.denomination = denomination;
        this.countOfBanknotes = 0;
//        this.maxCapacity = 1000;
    }

    private void addBanknotes(int countOfBanknotes){
        this.countOfBanknotes += countOfBanknotes;
    }

    private void removeBanknotes(int countOfBanknotes) throws Exception {
        if (this.countOfBanknotes < countOfBanknotes){
            throw new Exception("В ячейке недостаточно купюр");
        }
        this.countOfBanknotes -= countOfBanknotes;
    }

    public int getCount() {
        return this.countOfBanknotes;
    }

    public int getDenomination() {
        return this.denomination;
    }


//    private void setMaxCapacity(int maxCapacity){
//        this.maxCapacity = maxCapacity;
//    }


}
