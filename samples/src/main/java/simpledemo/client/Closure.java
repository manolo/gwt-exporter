package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportPackage("jsc")
@ExportClosure
// Closure interfaces are used when we need to pass a js function 
// to a exported method.
public interface Closure extends Exportable {
  public void execute(String par1, String par2);
}