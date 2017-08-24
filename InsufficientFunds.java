/**
 * InsufficientFunds.java - User-defined checked exception
 * @author Andrew Eissen
 * Date: 07/10/17
 */

package atm;

class InsufficientFunds extends Exception {
    /**
     * Default constructor
     */
    public InsufficientFunds() {
        super();
    }

    /**
     * Parameterized constructor
     * @param String message
     */
    public InsufficientFunds(String message) {
       super(message);
    }

    /**
     * Parameterized constructor
     * @param String message
     * @param Throwable cause
     */
    public InsufficientFunds(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor
     * @param Throwable cause
     */
    public InsufficientFunds(Throwable cause) {
        super(cause);
    }
}