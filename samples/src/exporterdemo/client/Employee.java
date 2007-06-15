package exporterdemo.client;

import org.timepedia.exporter.client.Exportable;

/**
 * Blacklist export policy. All public methods exported by default
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 * @gwt.export
 * @gwt.exportPackage test
 */
public class Employee extends Person implements Exportable {
    private String title;
    private int gender;
    private MyClickListener listener;

    public static final int MALE=0, FEMALE=1;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Employee(String title, String firstName, String lastName,
                    int gender) {
        super(firstName, lastName);
        this.title = title;
        this.gender = gender;
    }

   
    public Employee(Employee old) {
        super(old.getFirstName(), old.getLastName());
        this.title = old.getTitle();
        this.gender=old.getGender();
    }

    /**
     * let's stop this one from being exported
     * @gwt.noexport
     */
    public String fullTitle() {
        return title+" "+super.fullTitle();
    }

    // should not be exported
    private String getBlah() {
	return "blah";
    }


    public int getGender() {
        return gender;
    }

    public static String sayHello(Person emp) {
        return "Hello "+emp.fullTitle();
    }

    /**
     * demonstrate how to avoid overload conflicts
     * @gwt.export sayHelloString
     * @param foo
     * @return
     */
    public static String sayHello(String foo) {
        return "Hello "+foo;
    }

    /**
     * @gwt.export addListener
     */
    public void addClickListener(MyClickListener listener) {
	this.listener=listener;
    }

    /**
     * @gwt.export fire
     */
    public void fireClickListener() {
	if(listener != null) listener.onClick(this, "Hello", gender);
    }
}
