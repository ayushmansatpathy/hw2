package controller;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;

public class AmountFilter implements TransactionFilter {

    private final double target;

    public AmountFilter(String amountText) {
        this.target = InputValidation.requireValidAmount(amountText);
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> out = new ArrayList<>();
        for (Transaction t : transactions) {
            if (Math.abs(t.getAmount() - target) < 1e-9) {
                out.add(t);
            }
        }
        return out;
    }
}