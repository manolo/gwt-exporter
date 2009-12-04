package exporterdemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Blacklist export policy. All public methods exported by default
 *
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
@Export
@ExportPackage("test")
public class Employee extends Person implements Exportable {

  private String title;

  private int gender;

  private MyClickListener listener;

  public static final int MALE = 0, FEMALE = 1;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Employee(String title, String firstName, String lastName, int gender) {
    super(firstName, lastName);
    this.title = title;
    this.gender = gender;
    people=new Person[2];
    people[0]=new Person("John", "McCain");
    people[1]=new Person("Barack", "Obama");
  }

  public Employee(Employee old) {
    super(old.getFirstName(), old.getLastName());
    this.title = old.getTitle();
    this.gender = old.getGender();
  }

  /**
   * let's stop this one from being exported
   */
  @NoExport
  public String fullTitle() {
    return title + " " + super.fullTitle();
  }

  // should not be exported
  private String getBlah() {
    return "blah";
  }

  public int getGender() {
    return gender;
  }

  public static String sayHello(Person emp) {
    return "Hello " + emp.fullTitle();
  }

  /**
   * demonstrate how to avoid overload conflicts
   */
  @Export("sayHelloString")
  public static String sayHello(String foo) {
    return "Hello " + foo;
  }

  /**
   */
  @Export("addListener")
  public void addClickListener(MyClickListener listener) {
    this.listener = listener;
  }

  /**
   */
  @Export("fire")
  public void fireClickListener() {
    if (listener != null) {
      listener.onClick(this, "Hello", gender);
    }
  }

  /**
   */
  @Export
  public InnerTest getInnerTest() {
    return new InnerTest();
  }

  /**
   */
  @Export
  public static class InnerTest implements Exportable {

    public InnerTest() {
    }

    public int getFoo() {
      return 10;
    }
  }

    double values[] = new double[] {1,2,3,4,5};
  @Export
      public double[] getValues() {
      return values;
  }

    Person[] people;
//  @Export
//      public Person[] getPeople() { 
//      return people;
//  }
}
