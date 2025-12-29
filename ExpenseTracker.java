import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ExpenseTracker extends JFrame {
    private ArrayList<Expense> expenses;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField descField, amountField, categoryField;
    private JLabel totalLabel;

    public ExpenseTracker() {
        super("Expense Tracker");
        createGUI();
    }

    private void createGUI() {
        expenses = new ArrayList<>();
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));

        inputPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        inputPanel.add(descField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new AddExpenseListener());
        inputPanel.add(addButton);

        JButton clearButton = new JButton("Clear Inputs");
        clearButton.addActionListener(e -> clearInputs());
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"Description", "Amount", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total Expenses: ₹0.00");
        totalPanel.add(totalLabel);
        add(totalPanel, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void clearInputs() {
        descField.setText("");
        amountField.setText("");
        categoryField.setText("");
    }

    private void updateTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        totalLabel.setText(String.format("Total Expenses: ₹%.2f", total));
    }

    private class AddExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String desc = descField.getText().trim();
            String amountStr = amountField.getText().trim();
            String category = categoryField.getText().trim();

            if (desc.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(ExpenseTracker.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Expense expense = new Expense(desc, amount, category);
                expenses.add(expense);

                tableModel.addRow(new Object[]{expense.getDescription(), expense.getAmount(), expense.getCategory()});
                updateTotal();
                clearInputs();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ExpenseTracker.this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTracker::new);
    }
}
