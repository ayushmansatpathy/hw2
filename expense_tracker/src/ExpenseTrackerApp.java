import javax.swing.SwingUtilities;

import controller.ExpenseTrackerController;
import view.ExpenseTrackerView;

/**
 * Entry point. Creates the View and Controller (MVC).
 */
public class ExpenseTrackerApp {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ExpenseTrackerView view = new ExpenseTrackerView();
      view.setVisible(true);
      // Wire controller after view creation
      new ExpenseTrackerController(view);
    });
  }
}
