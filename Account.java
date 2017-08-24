/**
 * Account.java - Basic account class
 * @author Andrew Eissen
 * Date: 07/10/17
 */

package atm;

public class Account {
    private static int numWithdraws = 0;
    private static final double SERVICE_FEE = 1.50;

    private String name;
    private double balance;

    /**
     * Parameterized Constructor
     * @param String name
     * @param double initialBalance
     */
    public Account(String name, double initialBalance) {
        this.setName(name);
        this.setBalance(initialBalance);
    }

    /**
     * The default, no arguments constructor
     */
    public Account() {
        this.setName("Account");
        this.setBalance(0.0);
    }

    /**
     * Sets the name of the account
     * @param String name 
     * @return void
     */
    private void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the balance field
     * @param double balance 
     * @return void
     */
    private void setBalance(double balance) {
        if (balance >= 0.0) {
            this.balance = balance;
        } else {
            this.balance = 0.0;
        }
    }

    /**
     * Returns the name of the account
     * @return String name 
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method returns the balance of the Account
     * @return double this.balance
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * Method returns the number of withdraws
     * @return int Account.numWithdraws
     */
    public static int getNumWithdraws() {
        return Account.numWithdraws;
    }

    /**
     * Method withdraws an inputted amount of money from the balance
     * @param double desiredWithdraw 
     * @return double this.balance
     * @throws atm.InsufficientFunds
     */
    public double withdrawAndReturnBalance(double desiredWithdraw) throws InsufficientFunds {
        Account.numWithdraws++;
        if (Account.numWithdraws > 4) {
            desiredWithdraw += Account.SERVICE_FEE;
        }
        
        if (desiredWithdraw > this.getBalance()) {
            throw new InsufficientFunds();
        }
        
        this.setBalance(this.balance - desiredWithdraw);
        return this.getBalance();
    }

    /**
     * Method deposits inputted amount of money to the balance
     * @param double desiredDeposit
     * @return double this.balance
     */
    public double depositAndReturnBalance(double desiredDeposit) {
        this.setBalance(this.balance + desiredDeposit);
        return this.getBalance();
    }

    /**
     * Adjusts the balance to reflect the desired fund removal
     * @param double desiredTransfer
     * @return double this.balance
     */
    public double transferAndReturnBalance(double desiredTransfer) {
        this.setBalance(this.balance - desiredTransfer);
        return this.getBalance();
    }
}