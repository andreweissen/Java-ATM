/**
 * GUI.java - Constructs the user interface
 * @author Andrew Eissen
 * Date: 07/10/17
 */

package atm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

public final class GUI extends JFrame {
    // Constructor fields
    private String title;
    private int width;
    private int height;

    // GUI Declarations
    private JPanel mainPanel, buttonsPanel, textPanel;
    private JFrame frame;
    private ButtonGroup radioGroup;
    private JTextField userInputField;

    private DecimalFormat df = new DecimalFormat("####0.00");

    /**
     * Standard constructor
     * @param title
     * @param width
     * @param height 
     */
    public GUI(String title, int width, int height) {
        super(title);
        this.setWindowTitle(title);
        this.setWindowWidth(width);
        this.setWindowHeight(height);
    }

    /**
     * Default, no arguments constructor
     */
    public GUI() {
        super("Automated Teller Machine");
        this.setWindowTitle("Automated Teller Machine");
        this.setWindowWidth(400);
        this.setWindowHeight(200);
    }

    /**
     * Setter function for String title
     * @param String title
     * @return void
     */
    private void setWindowTitle(String title) {
        this.title = title;
    }

    /**
     * Setter function for int Width
     * @param int width
     * @return void
     */
    private void setWindowWidth(int width) {
        if (width > 400) {
            this.width = width;
        } else {
            this.width = 400;
        }
    }

    /**
     * Setter function for int height
     * @param int height
     * @return void
     */
    private void setWindowHeight(int height) {
        if (height > 200) {
            this.height = height;
        } else {
            this.height = 200;
        }
    }

    /**
     * Constructs the user interface
     * @param Account myCheckingAccount
     * @param Account mySavingsAccount
     * @return void
     */
    private void constructGUI(Account myCheckingAccount, Account mySavingsAccount) {

        // JPanel definitions
        mainPanel = new JPanel(new BorderLayout());
        buttonsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        textPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // ButtonGroup definitions
        radioGroup = new ButtonGroup();

        // JTextField declarations
        userInputField = new JTextField();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 55, 30, 55));

        // Assemble buttons with GUI item pseudoconstructors
        this.constructButton("Withdraw", "withdraw", myCheckingAccount, mySavingsAccount);
        this.constructButton("Deposit", "deposit", myCheckingAccount, mySavingsAccount);
        this.constructButton("Transfer to", "transfer", myCheckingAccount, mySavingsAccount);
        this.constructButton("Balance", "balance", myCheckingAccount, mySavingsAccount);
        this.constructRadioButton("Checking");
        this.constructRadioButton("Savings");

        // Input box
        textPanel.add(userInputField);
        
        // Add to main
        mainPanel.add(BorderLayout.NORTH, buttonsPanel);
        mainPanel.add(BorderLayout.SOUTH, textPanel);

        // JFrame
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Pseudoconstructor for JButtons
     * @param JButton button
     * @param String buttonText
     * @param String methodName
     * @param Account myCheckingAccount
     * @param Account mySavingsAccount
     * @return void
     */
    private void constructButton(
        String buttonText,
        String methodName,
        Account myCheckingAccount,
        Account mySavingsAccount
    ) {
        JButton button = new JButton(buttonText);
        buttonsPanel.add(button);
        button.addMouseListener(new AccountMouseAdapter(
            methodName,
            myCheckingAccount,
            mySavingsAccount
        ));
    }

    /**
     * Pseudoconstructor for JRadioButton
     * @param String buttonText
     * @return void
     */
    private void constructRadioButton(String buttonText) {
        JRadioButton radioButton = new JRadioButton(buttonText);
        radioButton.setActionCommand(buttonText);
        radioGroup.add(radioButton);
        buttonsPanel.add(radioButton);
        if ("Checking".equals(buttonText)) {
            radioButton.setSelected(true);
        }
    }

    /**
     * This method handles a withdraw from an inputted account
     * @param Account desiredAccount 
     * @return void
     */
    private void handleWithdraw(Account desiredAccount) {
        double inputDouble, newBalance;
        boolean isDivisibleBy20;

        try {
            inputDouble = Double.parseDouble(userInputField.getText());
            isDivisibleBy20 = inputDouble % 20 == 0;

            if (inputDouble < 0.0) {
                throw new NegativeInput();
            }

            if (!isDivisibleBy20) {
                throw new IndivisibleByTwenty();
            }

            // Account method throws InsufficientFunds exception as per the rubric
            newBalance = desiredAccount.withdrawAndReturnBalance(inputDouble);

            this.displayStatusPanel(
                "Success: $" + df.format(inputDouble) + " successfully withdrawn." 
                    + "\n\n" + desiredAccount.getName() + " Account Balance: $"
                    + df.format(newBalance),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (NumberFormatException error) {
            this.displayNumberFormatExceptionPanel();
        } catch (NegativeInput error) {
            this.displayNegativeInputPanel();
        } catch (IndivisibleByTwenty error) {
            this.displayStatusPanel(
                "Error: Input must be in increments of $20.",
                "IndivisibleByTwenty Error",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (InsufficientFunds error) {
            this.displayInsufficientFundsPanel();
        }

        // Reset the input fields
        userInputField.setText("");
    }

    /**
     * This method handles a deposit to an inputted account
     * @param Account desiredAccount
     * @return void
     */
    private void handleDeposit(Account desiredAccount) {
        double inputDouble, newBalance;

        try {
            inputDouble = Double.parseDouble(userInputField.getText());

            if (inputDouble < 0.0) {
                throw new NegativeInput();
            }

            newBalance = desiredAccount.depositAndReturnBalance(inputDouble);
            this.displayStatusPanel(
                "Success: $" + df.format(inputDouble) + " successfully deposited."
                    + "\n\n" + desiredAccount.getName() + " Account Balance: $"
                    + df.format(newBalance),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch(NumberFormatException error) {
            this.displayNumberFormatExceptionPanel();
        } catch (NegativeInput error) {
            this.displayNegativeInputPanel();
        }

        // Reset the input fields
        userInputField.setText("");
    }

    /**
     * This method transfers money from one account to another.
     * @param Account recipient
     * @param Account origin
     * @return void
     */
    private void handleTransferTo(Account recipient, Account origin) {
        double inputDouble, originNewBalance, recipientNewBalance;

        try {
            inputDouble = Double.parseDouble(userInputField.getText());

            if (inputDouble < 0.0) {
                throw new NegativeInput();
            }

            if (inputDouble > origin.getBalance()) {
                throw new InsufficientFunds();
            }

            originNewBalance = origin.transferAndReturnBalance(inputDouble);
            recipientNewBalance = recipient.depositAndReturnBalance(inputDouble);
            this.displayStatusPanel(
                "Success: $" + df.format(inputDouble) + " successfully transferred."
                    + "\n\n" + origin.getName() + " Account Balance: $"
                    + df.format(originNewBalance) + "\n" + recipient.getName()
                    + " Account Balance: $" + df.format(recipientNewBalance),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch(NumberFormatException error) {
            this.displayNumberFormatExceptionPanel();
        } catch (NegativeInput error) {
            this.displayNegativeInputPanel();
        } catch (InsufficientFunds error) {
            this.displayInsufficientFundsPanel();
        }

        // Reset the input fields
        userInputField.setText("");
    }

    /**
     * This account displays a popup that displays the amount in the account.
     * @param Account desiredAccount
     * @return void
     */
    private void handleBalance(Account desiredAccount) {
        this.displayStatusPanel(
            "Current " + desiredAccount.getName() + " Balance: $"
                + df.format(desiredAccount.getBalance()),
            "Your Balance",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Reset the input fields
        userInputField.setText("");
    }

    /**
     * Standard pseudoconstructor for status popups
     * @param String message
     * @param String title
     * @param int status
     * @return void
     */
    private void displayStatusPanel(String message, String title, int status) {
        JOptionPane.showMessageDialog(mainPanel, message, title, status);
    }

    /**
     * Custom popup for InsufficientFunds error
     * @return void
     */
    private void displayInsufficientFundsPanel() {
        JOptionPane.showMessageDialog(
            mainPanel,
            "Error: Not enough money in account.",
            "InsufficientFunds error",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Custom popup for NegativeInput error
     * @return void
     */
    private void displayNegativeInputPanel() {
        JOptionPane.showMessageDialog(
            mainPanel,
            "Error: Input must be positive.",
            "NegativeInput error",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Custom popup for NumberFormatException error
     * @return void
     */
    private void displayNumberFormatExceptionPanel() {
        JOptionPane.showMessageDialog(
            mainPanel,
            "Error: Improper input detected.",
            "NumberFormatException error",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Main method of the class, with two Account objects as requested.
     * @param String[] args
     */
    public static void main(String[] args) {
        // Instances
        GUI newGUI = new GUI("ATM Machine", 400, 200);
        Account checkingAccount = new Account("Checking", 0.0);
        Account savingsAccount = new Account("Savings", 0.0);

        // Construct the user interface
        newGUI.constructGUI(checkingAccount, savingsAccount);
    }

    /**
     * Event listener adapter class
     * Extends MouseAdapter
     */
    class AccountMouseAdapter extends MouseAdapter {
        private String methodName;
        private Account selectedAccount, alternate, myCheckingAccount, mySavingsAccount;

        /**
         * Parameterized constructor
         * @param String methodName
         * @param Account myCheckingAccount
         * @param Account mySavingsAccount
         */
        public AccountMouseAdapter(
            String methodName,
            Account myCheckingAccount,
            Account mySavingsAccount
        ) {
            this.methodName = methodName;
            this.myCheckingAccount = myCheckingAccount;
            this.mySavingsAccount = mySavingsAccount;
        }

        /**
         * Overridden method that handles mouse click events
         * @param MouseEvent e
         * @return void
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if ("Checking".equals(radioGroup.getSelection().getActionCommand())) {
                this.selectedAccount = myCheckingAccount;
                this.alternate = mySavingsAccount;
            } else if ("Savings".equals(radioGroup.getSelection().getActionCommand())) {
                this.selectedAccount = mySavingsAccount;
                this.alternate = myCheckingAccount;
            }

            switch(methodName) {
                case "withdraw":
                    handleWithdraw(selectedAccount);
                    break;
                case "deposit":
                    handleDeposit(selectedAccount);
                    break;
                case "transfer":
                    handleTransferTo(selectedAccount, alternate);
                    break;
                case "balance":
                    handleBalance(selectedAccount);
                    break;
            }
        }
    }
}