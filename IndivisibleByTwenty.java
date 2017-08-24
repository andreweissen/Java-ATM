/**
 * IndivisibleByTwenty.java - User-defined checked exception
 * @author Andrew Eissen
 * Date: 07/10/17
 */

package atm;

class IndivisibleByTwenty extends Exception {
    /**
     * Default constructor
     */
    public IndivisibleByTwenty() {
        super();
    }

    /**
     * Parameterized constructor
     * @param String message
     */
    public IndivisibleByTwenty(String message) {
       super(message);
    }

    /**
     * Parameterized constructor
     * @param String message
     * @param Throwable cause
     */
    public IndivisibleByTwenty(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Parameterized constructor
     * @param Throwable cause
     */
    public IndivisibleByTwenty(Throwable cause) {
        super(cause);
    }
}