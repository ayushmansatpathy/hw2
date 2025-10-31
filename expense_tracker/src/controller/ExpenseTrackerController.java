package controller;

import javax.swing.*;
import model.Transaction;
import view.ExpenseTrackerView;
import java.util.List;

public class ExpenseTrackerController {

  private final ExpenseTrackerView view;

  public ExpenseTrackerController(ExpenseTrackerView view) {
    this.view = view;

    this.view.setOnAddTransaction(e -> {
      try {
        double amount = InputValidation.requireValidAmount(view.getAmountText());
        String category = InputValidation.requireValidCategory(view.getCategoryText());
        Transaction t = new Transaction(amount, category);
        view.addTransaction(t);
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(view, ex.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
      }
    });

    this.view.setOnApplyFilter(e -> applyFilter());
  }

  public void applyFilter() {
    String selection = view.getSelectedFilter();
    List<Transaction> all = view.getAllTransactions();

    try {
      List<Transaction> filtered;
      switch (selection) {
        case "Amount":
          filtered = new AmountFilter(view.getFilterValueText()).filter(all);
          break;
        case "Category":
          filtered = new CategoryFilter(view.getFilterValueText()).filter(all);
          break;
        case "None":
        default:
          filtered = all;
          break;
      }
      view.showTransactions(filtered);
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(view, ex.getMessage(), "Invalid filter input", JOptionPane.ERROR_MESSAGE);
    }
  }
}
