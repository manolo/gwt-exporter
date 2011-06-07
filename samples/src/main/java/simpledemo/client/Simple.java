package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export
@ExportPackage("jsc")
// When we annotate a class with Export all methods will be exported
// But method in parent classes will not.
public class Simple extends Abstract implements Exportable {

  public void show(){
    System.out.println("Call show");
  }
  
  public void echo(String e) {
    System.out.println("echo: " + e);
  }
  
  public void hello(Hello a6) {
    System.out.println("hello " + a6.sayHello());
  }
  
  public void executeJsClosure(Closure closure){
    closure.execute("Hello", "Friend");
  }
}