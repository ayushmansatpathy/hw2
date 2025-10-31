package controller;

import java.util.Set;

public final class InputValidation {

  private static final Set<String> ALLOWED_CATEGORIES = Set.of("food", "travel", "bills", "entertainment", "other");

  private InputValidation() {
  }

  public static double requireValidAmount(String amountText) {
    if (amountText == null || amountText.trim().isEmpty()) {
      throw new IllegalArgumentException("Amount required.");
    }
    double val;
    try {
      val = Double.parseDouble(amountText.trim());
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException("Amount must be a Double.");
    }
    if (val <= 0 || val >= 1000) {
      throw new IllegalArgumentException("Amount must be > 0 and < 1000.");
    }
    return val;
  }

  public static String requireValidCategory(String categoryText) {
    if (categoryText == null) {
      throw new IllegalArgumentException("Category is required.");
    }
    String c = categoryText.trim().toLowerCase();
    if (!ALLOWED_CATEGORIES.contains(c)) {
      throw new IllegalArgumentException(
          "Category must be one of: food, travel, bills, entertainment, other.");
    }
    return c;
  }
}