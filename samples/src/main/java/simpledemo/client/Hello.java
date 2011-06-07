package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportPackage("jsc")
// Classes used as arguments should implement Exportable
// It it not necessary to export them because they will be
// automatically exported if needed.
public class Hello implements Exportable {
  public String sayHello(){
    return "Hello";
  }
}