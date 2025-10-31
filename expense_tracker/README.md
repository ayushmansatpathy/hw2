# hw2

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTrackerApp
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## Updates to Expense Tracker — CS 520 HW2 (Design Patterns & Testing)

This repo upgrades the HW1 Expense Tracker to satisfy HW2 requirements:
- Extensibility: Strategy pattern for filtering (Amount / Category / None)
- MVC compliance: `ExpenseTrackerView` (Swing UI), `ExpenseTrackerController` (event wiring), `Transaction`/helpers (Model-side)
- Testability: 5 JUnit tests validating add/validation/filter behaviors
- Understandability: Updated README, Javadoc, incremental commits
- Usability (Design Only): Export to CSV design (`export.txt`)

---

## Features

### Filtering (Strategy)
- `TransactionFilter` (interface)
- `AmountFilter` — exact amount matches (validated with HW1 rules)
- `CategoryFilter` — case-insensitive category matches (validated with HW1 rules)
- UI: Filter type combo (None / Amount / Category), filter value field, Apply Filter button
- Controller: `applyFilter()` builds the proper strategy and refreshes the table

### Add Transactions
- Uses HW1 input validation for amount and category
- Table always shows a Total row (sum of amounts), preserved after filtering

---

## Tests

All required tests are implemented in test/ExpenseTrackerHW2Tests.java:

1. Add Valid Transaction – verifies a valid entry updates the table and total.
2. Invalid Amount – ensures invalid amounts don’t alter data or total.
3. Invalid Category – ensures invalid categories don’t alter data or total.
4. Filter by Amount – checks filtering by amount returns correct rows and total.
5. Filter by Category – checks filtering by category returns correct rows and total.
