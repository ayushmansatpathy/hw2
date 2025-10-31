package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Transaction;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ExpenseTrackerView is the Swing UI (MVC View).
 * It stores a master list of transactions, and can display either the full list
 * or a filtered list.
 */
public class ExpenseTrackerView extends JFrame {

  // ---- Model displayed in table ----
  private final DefaultTableModel tableModel = new DefaultTableModel();

  // ---- UI controls for adding transactions ----
  private final JTextField amountField = new JTextField(10);
  private final JTextField categoryField = new JTextField(10);
  private final JButton addTransactionBtn = new JButton("Add");

  // ---- UI controls for filtering ----
  private final JComboBox<String> filterTypeBox = new JComboBox<>(new String[] { "None", "Amount", "Category" });
  private final JTextField filterValueField = new JTextField(10);
  private final JButton applyFilterBtn = new JButton("Apply Filter");

  // ---- Table ----
  private final JTable table = new JTable(tableModel);

  // ---- Data (master list) ----
  private final List<Transaction> allTransactions = new ArrayList<>();

  public ExpenseTrackerView() {
    super("Expense Tracker");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(700, 500);
    setLocationRelativeTo(null);

    // Table columns
    tableModel.addColumn("Serial");
    tableModel.addColumn("Amount");
    tableModel.addColumn("Category");
    tableModel.addColumn("Date");

    // Layout
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    inputPanel.add(new JLabel("Amount:"));
    inputPanel.add(amountField);
    inputPanel.add(new JLabel("Category:"));
    inputPanel.add(categoryField);
    inputPanel.add(addTransactionBtn);

    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filterPanel.add(new JLabel("Filter:"));
    filterPanel.add(filterTypeBox);
    filterPanel.add(new JLabel("Value:"));
    filterPanel.add(filterValueField);
    filterPanel.add(applyFilterBtn);

    JPanel north = new JPanel(new GridLayout(2, 1));
    north.add(inputPanel);
    north.add(filterPanel);

    add(north, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);

    // initial empty table with "Total" row
    showTransactions(allTransactions);
  }

  // =========================
  // Public API used by Controller and Tests
  // =========================

  public String getAmountText() {
    return amountField.getText();
  }

  public String getCategoryText() {
    return categoryField.getText();
  }

  public void setOnAddTransaction(ActionListener l) {
    addTransactionBtn.addActionListener(l);
  }

  public void setOnApplyFilter(ActionListener l) {
    applyFilterBtn.addActionListener(l);
  }

  public String getSelectedFilter() {
    return (String) filterTypeBox.getSelectedItem();
  }

  public String getFilterValueText() {
    return filterValueField.getText();
  }

  /**
   * Add a transaction to the master list and refresh table (current filter is not
   * re-applied here).
   * Controller can call applyFilter again if needed.
   */
  public void addTransaction(Transaction t) {
    allTransactions.add(t);
    showTransactions(allTransactions);
  }

  /**
   * Return an unmodifiable view of the master list (for filtering strategies).
   */
  public List<Transaction> getAllTransactions() {
    return Collections.unmodifiableList(allTransactions);
  }

  /**
   * Replace the table contents with the provided list, and append the "Total"
   * row.
   */
  public void showTransactions(List<Transaction> toDisplay) {
    // Clear model
    int rows = tableModel.getRowCount();
    for (int r = rows - 1; r >= 0; r--) {
      tableModel.removeRow(r);
    }

    // Add rows
    double total = 0.0;
    int serial = 1;
    for (Transaction t : toDisplay) {
      tableModel.addRow(new Object[] {
          serial++,
          t.getAmount(),
          t.getCategory(),
          t.getTimestamp()
      });
      total += t.getAmount();
    }

    // Add total row
    tableModel.addRow(new Object[] { "Total", "", "", total });
  }

  public DefaultTableModel getTableModel() {
    return tableModel;
  }

  // ======= Test helpers kept for compatibility with HW1 tests (optional) =======

  /** For legacy tests that directly set the amount field widget. */
  public void setAmountField(JTextField custom) {
    // swap visually
    Container parent = amountField.getParent();
    if (parent != null) {
      parent.remove(amountField);
      parent.add(custom, 1); // right after "Amount:" label
      parent.revalidate();
      parent.repaint();
    }
    // transfer text value
    amountField.setText(custom.getText());
  }

  /** Legacy baseline parsing behavior: empty -> 0.0; else Double.parseDouble. */
  public double getAmountField() {
    String txt = amountField.getText();
    if (txt == null || txt.trim().isEmpty())
      return 0.0;
    try {
      return Double.parseDouble(txt.trim());
    } catch (NumberFormatException nfe) {
      return 0.0;
    }
  }
}
