package ru.homework;

/**
 * Bill.
 *
 * @author Vadim_Kraynov
 * @since 15.11.2022
 */
public class Bill {
    private final Nominal nominal;

    public Bill(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bill bill = (Bill) o;

        return nominal == bill.nominal;
    }

    @Override
    public int hashCode() {
        return nominal.hashCode();
    }
}
