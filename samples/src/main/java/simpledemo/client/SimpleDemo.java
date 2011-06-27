package simpledemo.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExporterUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class SimpleDemo implements EntryPoint {
  
  @ExportPackage("gwt")
  @Export
  public static class MLong implements Exportable {
    public static long rlongS() {
      return 4l;
    }
    public static String plongS(long a) {
      return "" + a;
    }
    public static String pdoubleS(double a) {
      return "" + a;
    }
    public long rlongC() {
      return rlongS();
    }
    public String plongC(long a) {
      return plongS(a);
    }
    public void normal(int a, double[] b, MLong l) {
      
    }
  }

  public void onModuleLoad() {
    ExporterUtil.exportAll();
    runJsTests();
  }
  
  public static <T> void print(T s) {
    RootPanel.get().add(new Label(s.toString()));
  }

  @ExportPackage("gwt")
  @Export
  public static class HelloClass implements Exportable {
    public String hello(){
      return this.getClass().getName();
    }

    public static String[] test0(char c, byte b, int i, double d, float f, String s, Object o, Exportable e) {
      String[] ret = new String[8];
      ret[0] = "" + (int)c;
      ret[1] = "" + b;
      ret[2] = "" + i;
      ret[3] = "" + d;
      ret[4] = "" + f;
      ret[5] = "" + s;
      ret[6] = "" + o;
      ret[7] = "" + e;
      return ret;
    }

    public static int[] test1(char[]c, byte[] b, int[] i, double[]d, float[] f, long[] l, String[] s, Object[] o, Exportable[] e) {
      int[] ret = new int[9];
      ret[0] = c.length;
      ret[1] = b.length;
      ret[2] = i.length;
      ret[3] = d.length;
      ret[4] = f.length;
      ret[5] = l.length;
      ret[6] = s.length;
      ret[7] = o.length;
      ret[8] = e.length;
      return ret;
    }

    public static long[] test2() {
      return new long[]{1l,2l};
    }
    
    public static Exportable[] test3() {
      return new HelloClass[]{new HelloClass()};
    }
    
    public static char test4() {
      return 1;
    }
    
    public static byte test5() {
      return 2;
    }
    
    public static int test6() {
      return 3;
    }
    
    public static double test7() {
      return 4;
    }
    
    public static float test8() {
      return 5;
    }
    
    public static String test9() {
      return "A";
    }
    
    public static JavaScriptObject test10() {
      return new Label("").getElement();
    }
    
    public static long test11() {
      return 6;
    }
    
    public static String test12(long l){
      return "" + l;
    }
    
    public static long test13(long l, double d) {
      return l + (long)d;
    }
    
    public long test14(long l, double d, long[] a) {
      return l + (long)d + a[0];
    }
    
    public long[] test15(long[] a) {
      return a;
    }

    public static String test16(long l) {
      return "" + l;
    }

    public static long test16(long a, long b) {
      return (a + b);
    }

    public String test17(long l) {
      return "" + l;
    }

    public long test17(long a, long b) {
      return (a + b);
    }
  }
  
  @ExportPackage("gwt")
  @Export
  public static class Foo implements Exportable {
    String n = "foo";
    public Foo() {
    }
    public Foo(String id) {
      n= id;
    }
    public Foo(String id, String a) {
      n= id;
    }
    public String toString(){
      return n;
    }
    public String toString(String a){
      return n + ">" + a;
    }
  }
  
  public native JavaScriptObject runJsTests() /*-{
    p = function(a) {@simpledemo.client.SimpleDemo::print(Ljava/lang/Object;)(a);}
    
    var v1 = new $wnd.gwt.SimpleDemo.Foo();
    p(v1);
    var v2 = new $wnd.gwt.SimpleDemo.Foo("foo2");
    p(v2);
    var v3 = new $wnd.gwt.SimpleDemo.Foo("foo3", "bbb");
    p(v3);
    p(v3.toString("ccc"));
    
    var h = new $wnd.gwt.SimpleDemo.HelloClass();
    p($wnd.gwt.SimpleDemo.HelloClass.test0(1, 2, 3, 4, 5, "S", window, h));
    p($wnd.gwt.SimpleDemo.HelloClass.test1([0], [0], [0], [0], [0], [1,2], ["a","b"], [window,document], [h]));
    p($wnd.gwt.SimpleDemo.HelloClass.test2());
    p($wnd.gwt.SimpleDemo.HelloClass.test3()[0].hello());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test4(1, "A"));
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test5());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test6());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test7());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test8());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test9());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test10());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test11());
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test12(1));
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test13(2, 3));
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test16(4));
    p("" + $wnd.gwt.SimpleDemo.HelloClass.test16(4, 10));
    
    var h = new $wnd.gwt.SimpleDemo.HelloClass();
    p("" + h.test14(1, 1, [100]));
    p("" + h.test15([100, 200]));
    p("" + h.test17(5));
    p("" + h.test17(5,10));
    
    var s = new $wnd.jsc.Simple();
    p(s.show());
    p(s.foo());
    p(s.echo("ECHO"));
    p(s.hello(new $wnd.jsc.Hello()));
    p(s.executeJsClosure(function(arg1, arg2) {
        return arg1 + "," + arg2;
    }));
    
  }-*/;
  
}
