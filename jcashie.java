package jg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class Transaction {
    String type;
    String recipient;
    double amount;

    public Transaction(String type, String recipient, double amount) {
        this.type = type;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String toString() {
        return type + " → " + recipient + " | ₱" +
                String.format("%.2f", amount);
    }
}

public class jcashie extends JFrame {

    private double balance = 2269.21;
    private JLabel balanceLabel;
    private JTextField recipientField;
    private JTextField amountField;
    private DefaultListModel<String> historyModel;

    public jcashie() {

        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10,15,10,15));

        JLabel helloLabel = new JLabel("Hello!");
        helloLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton helpBtn = new JButton("HELP");

        header.add(helloLabel, BorderLayout.WEST);
        header.add(helpBtn, BorderLayout.EAST);

      //balance card
        JPanel balanceCard = new JPanel(new BorderLayout());
        balanceCard.setBackground(new Color(0,120,215));
        balanceCard.setBorder(new EmptyBorder(20,20,20,20));

        JLabel title = new JLabel("AVAILABLE BALANCE");
        title.setForeground(Color.WHITE);

        balanceLabel = new JLabel("₱ " + balance);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JButton cashInTop = new JButton("+ Cash In");
        cashInTop.addActionListener(e -> handleTransaction("Cash In"));

        balanceCard.add(title, BorderLayout.NORTH);
        balanceCard.add(balanceLabel, BorderLayout.CENTER);
        balanceCard.add(cashInTop, BorderLayout.EAST);

       //wallet action butt
        JPanel actionsPanel = new JPanel(new GridLayout(2,4,10,10));
        actionsPanel.setBorder(new EmptyBorder(15,15,15,15));
        actionsPanel.setBackground(Color.WHITE);

        JButton sendBtn = new JButton("Send");
        JButton loadBtn = new JButton("Load");
        JButton transferBtn = new JButton("Transfer");
        JButton billsBtn = new JButton("Bills");

        JButton borrowBtn = new JButton("Borrow");
        JButton saveBtn = new JButton("GSave");
        JButton investBtn = new JButton("GInvest");
        JButton moreBtn = new JButton("View All");

        actionsPanel.add(sendBtn);
        actionsPanel.add(loadBtn);
        actionsPanel.add(transferBtn);
        actionsPanel.add(billsBtn);
        actionsPanel.add(borrowBtn);
        actionsPanel.add(saveBtn);
        actionsPanel.add(investBtn);
        actionsPanel.add(moreBtn);

       // transaction input are
        JPanel inputPanel = new JPanel(new GridLayout(4,1,8,8));
        inputPanel.setBorder(new EmptyBorder(15,15,15,15));
        inputPanel.setBackground(Color.WHITE);

        recipientField = new JTextField();
        recipientField.setBorder(
                BorderFactory.createTitledBorder("Send to")
        );

        amountField = new JTextField();
        amountField.setBorder(
                BorderFactory.createTitledBorder("Amount")
        );

        JButton sendMoneyBtn = new JButton("Send Money");

        inputPanel.add(recipientField);
        inputPanel.add(amountField);
        inputPanel.add(sendMoneyBtn);

      //transation history
        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Transactions")
        );

        sendBtn.addActionListener(e -> handleTransaction("Send Money"));
        loadBtn.addActionListener(e -> handleTransaction("Load"));   
        billsBtn.addActionListener(e -> handleTransaction("Pay Bills"));
        sendMoneyBtn.addActionListener(e -> handleTransaction("Send Money"));
        cashInTop.addActionListener(e -> handleTransaction("Cash In"));
       
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(balanceCard, BorderLayout.CENTER);

        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(actionsPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputPanel, BorderLayout.NORTH);
        bottom.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private void handleTransaction(String type) {
        String recipient = recipientField.getText().trim();
        String amountText = amountField.getText().trim();

        if (type.equals("Cash In")) {
            recipient = "Wallet";
        }
        if (recipient.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //  para mavalidate sa cp# (only for Send and Load)
        if ((type.equals("Send Money") || type.equals("Load")) &&
            (!recipient.startsWith("09") || recipient.length() != 11 || !recipient.matches("\\d+"))) {
            JOptionPane.showMessageDialog(this,
                    "Invalid phone number! It must start with '09' and be exactly 11 digits.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid amount!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be positive!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirm " + type + " ₱" + amount + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        if (!type.equals("Cash In") && balance < amount) {
            JOptionPane.showMessageDialog(this,
                    "Insufficient balance!");
            return;
        }

        if (type.equals("Cash In"))
            balance += amount;
        else
            balance -= amount;
        balanceLabel.setText("₱ " +
                String.format("%.2f", balance));
        historyModel.addElement(
                new Transaction(type, recipient, amount).toString()
        );

        recipientField.setText("");
        amountField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new jcashie().setVisible(true);
        });
    }
}
