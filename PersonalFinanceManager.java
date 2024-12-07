// Import necessary libraries
import javax.swing.*; // For GUI components
import java.awt.*; // For layout management
import java.awt.event.ActionEvent; // For handling button actions
import java.awt.event.ActionListener; // For action listeners
import java.util.ArrayList; // For ArrayList data structure
import java.util.List; // For List interface

// Main GUI class for the Personal Finance Manager application
class SimpleFinanceManagerGUI extends JFrame {
    private double monthlyBudget; // Stores the user's monthly budget
    private List<Expense> expenses; // List to store all expenses added by the user
    private JTextField budgetField, expenseNameField, expenseAmountField, expenseplacefield; // Input fields
    private JTextArea reportArea; // Text area to display the expense report
    private JLabel remainingBudgetLabel; // Label to show remaining budget

    // Constructor for the GUI
    public SimpleFinanceManagerGUI() {
        expenses = new ArrayList<>(); // Initialize the expenses list
        
        // Set up the frame (window) properties
        setTitle("Personal Finance Manager");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for setting the budget
        JPanel budgetPanel = new JPanel();
        budgetPanel.setLayout(new FlowLayout()); // Arranges components in a row
        budgetPanel.add(new JLabel("Monthly Budget:")); // Label for budget input
        budgetField = new JTextField(10); // Input field for budget
        JButton setBudgetButton = new JButton("Set Budget"); // Button to set budget
        setBudgetButton.addActionListener(new SetBudgetListener()); // Adds action when button is clicked
        budgetPanel.add(budgetField);
        budgetPanel.add(setBudgetButton);

        // Panel for adding expenses
        JPanel expensePanel = new JPanel();
        expensePanel.setLayout(new FlowLayout());
        expensePanel.add(new JLabel("Expense Name:")); // Label for expense name
        expenseNameField = new JTextField(8); // Input field for expense name
        expensePanel.add(expenseNameField);

        expensePanel.add(new JLabel("Amount:")); // Label for expense amount
        expenseAmountField = new JTextField(6); // Input field for expense amount
        expensePanel.add(expenseAmountField);

        expensePanel.add(new JLabel("expense place"));
        expenseplacefield = new JTextField(8);
        expensePanel.add(expenseplacefield);

        // Dropdown menu for expense type selection
        JComboBox<String> expenseTypeBox = new JComboBox<>(new String[]{"Fixed", "Variable", "Savings"});
        expensePanel.add(expenseTypeBox);

        // Button to add expense
        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = expenseNameField.getText(); // Get expense name
                double amount;
                try {
                    amount = Double.parseDouble(expenseAmountField.getText()); // Get expense amount
                    String type = (String) expenseTypeBox.getSelectedItem(); // Get expense type

                    // Create appropriate Expense object based on type
                    Expense expense;
                    if (type.equals("Fixed")) {
                        expense = new FixedExpense(name, amount);
                    } else if (type.equals("Variable")) {
                        expense = new VariableExpense(name, amount);
                    } else {
                        expense = new Savings(name, amount);
                    }
                    expenses.add(expense); // Add expense to the list
                    updateRemainingBudget(); // Update remaining budget
                    reportArea.append(expense + "\n"); // Display expense in report area
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SimpleFinanceManagerGUI.this, "Enter a valid amount.");
                }
            }
        });
        expensePanel.add(addExpenseButton);

        // Text area to display expense reports
        reportArea = new JTextArea(10, 30);
        reportArea.setEditable(false); // Make it read-only

        // Label to display remaining budget
        remainingBudgetLabel = new JLabel("Remaining Budget: $0.0");

        // Button to generate a monthly report
        JButton generateReportButton = new JButton("Generate Monthly Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        // Adding panels and components to the frame
        add(budgetPanel, BorderLayout.NORTH); // Adds budget panel at the top
        add(expensePanel, BorderLayout.CENTER); // Adds expense panel in the center
        add(new JScrollPane(reportArea), BorderLayout.SOUTH); // Adds report area at the bottom
        add(remainingBudgetLabel, BorderLayout.WEST); // Adds remaining budget label on the left
        add(generateReportButton, BorderLayout.EAST); // Adds report button on the right

        setVisible(true); // Makes the frame visible
    }

    // Inner class to handle setting the monthly budget
    private class SetBudgetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                monthlyBudget = Double.parseDouble(budgetField.getText()); // Parse and set monthly budget
                updateRemainingBudget(); // Update remaining budget display
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SimpleFinanceManagerGUI.this, "Please enter a valid budget amount.");
            }
        }
    }

    // Method to update and display the remaining budget
    private void updateRemainingBudget() {
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum(); // Calculate total expenses
        double remainingBudget = monthlyBudget - totalExpenses; // Calculate remaining budget
        remainingBudgetLabel.setText("Remaining Budget: $" + remainingBudget); // Display remaining budget
    }

    // Method to generate and display a report of all expenses
    private void generateReport() {
        reportArea.append("\n--- Monthly Financial Report ---\n"); // Header for the report
        double totalExpenses = 0;
        for (Expense expense : expenses) {
            reportArea.append(expense + "\n"); // Display each expense
            totalExpenses += expense.getAmount(); // Sum up expenses
        }
        reportArea.append("Total Monthly Expenses: $" + totalExpenses + "\n"); // Display total expenses
        updateRemainingBudget(); // Update the remaining budget
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleFinanceManagerGUI()); // Launch GUI
    }
}

// Abstract class representing a general expense
abstract class Expense {
    private String name; // Name of the expense
    private double amount; // Amount of the expense

    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    // Abstract method to be implemented by subclasses
    public abstract String toString();
}

// Class representing a fixed expense
class FixedExpense extends Expense {
    public FixedExpense(String name, double amount) {
        super(name, amount);
    }

    @Override
    public String toString() {
        return "Fixed Expense - " + getName() + ": $" + getAmount(); // Format for displaying
    }
}

// Class representing a variable expense
class VariableExpense extends Expense {
    public VariableExpense(String name, double amount) {
        super(name, amount);
    }

    @Override
    public String toString() {
        return "Variable Expense - " + getName() + ": $" + getAmount(); // Format for displaying
    }
}

// Class representing savings
class Savings extends Expense {
    public Savings(String name, double amount) {
        super(name, amount);
    }

    @Override
    public String toString() {
        return "Savings - " + getName() + ": $" + getAmount(); // Format for displaying
    }
}