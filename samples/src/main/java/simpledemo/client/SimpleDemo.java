package simpledemo.client;

import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class SimpleDemo implements EntryPoint {

  public void onModuleLoad() {
    
    // We could export only selected classes
    // However, exportable classes used as arguments or returned in 
    // methods will be exported automatically
    GWT.create(Closure.class);
    GWT.create(Hello.class);
    GWT.create(Abstract.class);
    GWT.create(Simple.class);
    
    // Or we could export everything
    ExporterUtil.exportAll();
  }
}
