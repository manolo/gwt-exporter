package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

// Extended classes does not export methods unless
// we mark the class or the method as exportable.
//
// They have to implement Exportable though
public class Abstract implements Exportable {
  @Export("foo")
  public void initialise(){
    System.out.println("Call foo == initialise");
  }
}