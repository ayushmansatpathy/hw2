package controller;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;

public class CategoryFilter implements TransactionFilter {

    private final String category;

    public CategoryFilter(String categoryText) {
        this.category = InputValidation.requireValidCategory(categoryText);
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        List<Transaction> out = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getCategory().equalsIgnoreCase(category)) {
                out.add(t);
            }
        }
        return out;
    }
}
