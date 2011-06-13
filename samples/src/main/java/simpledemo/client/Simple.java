package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportPackage("jsc")
// When we annotate a class with Export all methods will be exported
// But method in parent classes will not.
public class Simple extends Abstract implements Exportable {

  public String show(){
    return("Simple.show");
  }
  
  public String echo(String e) {
    return("Simple.echo: " + e);
  }
  
  public String hello(Hello a6) {
    return("Simple.hello " + a6.sayHello());
  }
  
  public String executeJsClosure(Closure closure){
    return closure.execute("Hello", "Friend");
  }
}