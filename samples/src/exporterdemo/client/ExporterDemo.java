package exporterdemo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import org.timepedia.exporter.client.Exporter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ExporterDemo implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    try {
      Exporter exporter = (Exporter) GWT.create(Employee.class);
      exporter.export();
      Exporter mexporter = GWT.create(ExportManager.class);
      mexporter.export();
//       Exporter exporter3 = (Exporter) GWT.create(Person.TestEnum.class);
//      exporter3.export();
      Window.alert("foo!");
    } catch (Exception e) {
      Window.alert("Exception " + e.toString());
    }
  }
}
