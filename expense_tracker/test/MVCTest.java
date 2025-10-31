// package test;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import controller.ExpenseTrackerController;
import model.Transaction;
import view.ExpenseTrackerView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MVCTest {

    private static <T> T getPrivateField(Object target, String fieldName, Class<T> type) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return type.cast(f.get(target));
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
        }
    }

    private static void setText(JTextField tf, String text) {
        tf.setText(text);
    }

    private static void click(JButton btn) {
        btn.doClick();
    }

    @Test
    public void testAddValidTransaction_updatesTableAndTotal() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ExpenseTrackerView view = new ExpenseTrackerView();
            new ExpenseTrackerController(view);

            DefaultTableModel model = view.getTableModel();
            assertEquals(1, model.getRowCount());
            assertEquals("Total", model.getValueAt(0, 0));
            assertEquals(0.0, ((Number) model.getValueAt(0, 3)).doubleValue(), 1e-9);

            JTextField amountField = getPrivateField(view, "amountField", JTextField.class);
            JTextField categoryField = getPrivateField(view, "categoryField", JTextField.class);
            setText(amountField, "50.00");
            setText(categoryField, "food");

            JButton addBtn = getPrivateField(view, "addTransactionBtn", JButton.class);
            click(addBtn);

            assertEquals(2, model.getRowCount());
            assertEquals(1, ((Number) model.getValueAt(0, 0)).intValue());
            assertEquals(50.00, ((Number) model.getValueAt(0, 1)).doubleValue(), 1e-9);
            assertEquals("food", model.getValueAt(0, 2));

            int last = model.getRowCount() - 1;
            assertEquals("Total", model.getValueAt(last, 0));
            assertEquals(50.00, ((Number) model.getValueAt(last, 3)).doubleValue(), 1e-9);
        });
    }

    @Test
    public void testInvalidAmount_noChangeAndTotalUnchanged() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ExpenseTrackerView view = new ExpenseTrackerView();
            new ExpenseTrackerController(view);

            DefaultTableModel model = view.getTableModel();
            assertEquals(1, model.getRowCount());
            assertEquals("Total", model.getValueAt(0, 0));
            assertEquals(0.0, ((Number) model.getValueAt(0, 3)).doubleValue(), 1e-9);

            JTextField amountField = getPrivateField(view, "amountField", JTextField.class);
            JTextField categoryField = getPrivateField(view, "categoryField", JTextField.class);
            setText(amountField, "abc");
            setText(categoryField, "food");

            JButton addBtn = getPrivateField(view, "addTransactionBtn", JButton.class);
            click(addBtn);

            assertEquals(1, model.getRowCount());
            assertEquals("Total", model.getValueAt(0, 0));
            assertEquals(0.0, ((Number) model.getValueAt(0, 3)).doubleValue(), 1e-9);
        });
    }

    @Test
    public void testInvalidCategory_noChangeAndTotalUnchanged() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ExpenseTrackerView view = new ExpenseTrackerView();
            new ExpenseTrackerController(view);

            DefaultTableModel model = view.getTableModel();
            assertEquals(1, model.getRowCount());
            assertEquals("Total", model.getValueAt(0, 0));
            assertEquals(0.0, ((Number) model.getValueAt(0, 3)).doubleValue(), 1e-9);

            JTextField amountField = getPrivateField(view, "amountField", JTextField.class);
            JTextField categoryField = getPrivateField(view, "categoryField", JTextField.class);
            setText(amountField, "10.0");
            setText(categoryField, "booze");

            JButton addBtn = getPrivateField(view, "addTransactionBtn", JButton.class);
            click(addBtn);

            assertEquals(1, model.getRowCount());
            assertEquals("Total", model.getValueAt(0, 0));
            assertEquals(0.0, ((Number) model.getValueAt(0, 3)).doubleValue(), 1e-9);
        });
    }

    @Test
    public void testFilterByAmount_onlyMatchingShown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ExpenseTrackerView view = new ExpenseTrackerView();
            ExpenseTrackerController controller = new ExpenseTrackerController(view);

            view.addTransaction(new Transaction(10.0, "food"));
            view.addTransaction(new Transaction(20.0, "travel"));
            view.addTransaction(new Transaction(10.0, "bills"));

            JComboBox<?> filterTypeBox = getPrivateField(view, "filterTypeBox", JComboBox.class);
            JTextField filterValueField = getPrivateField(view, "filterValueField", JTextField.class);
            filterTypeBox.setSelectedItem("Amount");
            setText(filterValueField, "10.0");

            JButton applyFilterBtn = getPrivateField(view, "applyFilterBtn", JButton.class);
            click(applyFilterBtn);

            DefaultTableModel model = view.getTableModel();

            assertEquals(3, model.getRowCount());
            assertEquals(1, ((Number) model.getValueAt(0, 0)).intValue());
            assertEquals(10.0, ((Number) model.getValueAt(0, 1)).doubleValue(), 1e-9);
            assertEquals(2, ((Number) model.getValueAt(1, 0)).intValue());
            assertEquals(10.0, ((Number) model.getValueAt(1, 1)).doubleValue(), 1e-9);

            int last = model.getRowCount() - 1;
            assertEquals("Total", model.getValueAt(last, 0));
            assertEquals(20.0, ((Number) model.getValueAt(last, 3)).doubleValue(), 1e-9);
        });
    }

    @Test
    public void testFilterByCategory_onlyMatchingShown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            ExpenseTrackerView view = new ExpenseTrackerView();
            ExpenseTrackerController controller = new ExpenseTrackerController(view);

            view.addTransaction(new Transaction(12.5, "food"));
            view.addTransaction(new Transaction(30.0, "travel"));
            view.addTransaction(new Transaction(8.0, "food"));

            JComboBox<?> filterTypeBox = getPrivateField(view, "filterTypeBox", JComboBox.class);
            JTextField filterValueField = getPrivateField(view, "filterValueField", JTextField.class);
            filterTypeBox.setSelectedItem("Category");
            setText(filterValueField, "food");

            JButton applyFilterBtn = getPrivateField(view, "applyFilterBtn", JButton.class);
            click(applyFilterBtn);

            DefaultTableModel model = view.getTableModel();
            assertEquals(3, model.getRowCount());
            assertEquals("food", model.getValueAt(0, 2));
            assertEquals("food", model.getValueAt(1, 2));

            int last = model.getRowCount() - 1;
            assertEquals("Total", model.getValueAt(last, 0));
            assertEquals(20.5, ((Number) model.getValueAt(last, 3)).doubleValue(), 1e-9);
        });
    }
}
