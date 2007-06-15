package exporterdemo.client;

import org.timepedia.exporter.client.Exportable;


/**
 * Whitelist demo, manually export 
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public class Person implements Exportable {
    private String firstName;
    private String lastName;

    /**
     * @gwt.export
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @gwt.export
     */

    public String getLastName() {
        return lastName;
    }
 
    /**
     * @gwt.export
     */
   public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @gwt.export
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @gwt.export
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
 
    /**
     * @gwt.export
     */
   public String fullTitle() {
        return firstName+" "+lastName;
    }

    /**
     * @gwt.export
     */
    public Person getSelf() {
        return this;
    }
    
}
