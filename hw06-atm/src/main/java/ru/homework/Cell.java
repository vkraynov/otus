package ru.homework;

/**
 * Cell for bills.
 *
 * @author Vadim_Kraynov
 * @since 15.11.2022
 */
public class Cell implements Comparable<Cell> {
    private final Bill bill;
    private int quantity;

    public Cell(Bill bill) {
        this.bill = bill;
    }

    public Cell load(int quantity) {
        this.quantity += quantity;
        return this;
    }

    public void load(Bill bill) {
        if (this.bill.getNominal() == bill.getNominal()) {
            quantity++;
        }
    }

    public int withdraw(int numberOfRequestedBills) {
        if (quantity - numberOfRequestedBills >= 0) {
            quantity -= numberOfRequestedBills;
        } else {
            numberOfRequestedBills = quantity;
            quantity = 0;
        }
        return numberOfRequestedBills;
    }

    public Bill getBill() {
        return bill;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int compareTo(Cell other) {
        return Integer.compare(other.getBill().getNominal().getValue(), getBill().getNominal().getValue());
    }
}
