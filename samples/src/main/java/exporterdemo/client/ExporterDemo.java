package exporterdemo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import org.timepedia.exporter.client.Exporter;
import org.timepedia.exporter.client.ExporterUtil;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ExporterDemo implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    GWT.create(MyClickListener.class);
    
    GWT.create(exporterdemo.client.Employee.class);
//    GWT.create(exporterdemo.client.Employee.InnerTest.class);
//    GWT.create(exporterdemo.client.Person.class);

//    try {
//      ExporterUtil.exportAll();
//    } catch (Exception e) {
//      Window.alert("Exception " + e.toString());
//    }
  }
}
