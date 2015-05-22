

# Quick start guide #
  * Add the gwtexporter-x.x.x.jar to your project either adding it to your classpath, or adding a [Maven dependency](MavenConfiguration.md) to your pom.xml file.
  * Inherit the gwt-exporter stuff, and tell the gwt compiler whether you want to export your project.
```
 <module>
  ...
  <inherits name='org.timepedia.exporter.Exporter'/>
  <set-property name="export" value="yes"/>
 </module>
```
Tip: The export flag here is useful when sharing your code having Exportable classes with code which does not need anything to be exported to the js world.

  * Implement `Exportable` in all classes you wanted to export, and use annotations to mark which parts to export.
```
  package com.example;
  @Export
  public static class A implements Exportable {
    public String foo() {
      return "foo";
    }
    public static String bar(){
      return "bar";
    }
  }
```

  * In the onModuleLoad() call whether `GWT.create()` to export each exportable class, or `ExporterUtil.exportAll()` to export all of them.
```
  public void onModuleLoad() {
    // Export C class and all dependent classes it uses
    GWT.create(C.class);
  }
```
```
  public void onModuleLoad() {
    // Export all Exportable classes 
    ExporterUtil.exportAll();
  }
```

  * Finally you can use your code in any hand-written javascript
```
   // create an instance of A
   var a = new com.example.A()
   // call an instance method
   a.foo();
   // call a static method
   com.example.A.bar();
```

  * **IMPORTANT**: GWT scripts are not loaded until the appropriate permutation has been computed, which is after the document is ready. If you are using the exported code in the document, it is necessary that you enclose your javascript code in a callback function which you have to run at the end of the `onModuleLoad` method.
```
  public void onModuleLoad() {
    ExporterUtil.exportAll();
    [...]
    onLoad();
  }
  private native void onLoad() /*-{
    if ($wnd.myInit) $wnd.myInit();
  }-*/;
```
```
 <script>
  function myInit() {
    [...]
  }
 </script>
```

# Exportable classes #
You can export either: classes, abstract types, and interfaces, although only instances of classes witch have public constructors can be instantiated from js.

Abstract classes and Interfaces are exported so as you can use them in exported methods, but instances of them have to be created either from gwt, or from exported classes which implement them and have public constructors.

# Exportable methods and attribures #
Gwt-exporter only exports all public methods and constructors marked to export.

You can export static methods as well, and you can call them without instantiating the class is belongs to, but using its namespace.

Gwtexporter only exports public and final attributes.

# Exportable types #
Only certain types and classes can be used as arguments or return types of methods and constructors:
  * primitive types like `double` but not `Double`.
  * Although gwt JSNI does not support `long`, gwtexporter is able to handle it.
  * `String`
  * `java.util.Date`
  * Classes extending `JavaScriptObject`
  * Classes or interfaces implementing `Exportable`
  * `Arrays` of the above types, but not lists nor collections

As an additional note, know that you could use any gwt-object as parameters or return types, but those objects could only be used in javascript as  references, because their gwt methods are not exported.

# Interfaces #
All classes (including interfaces or abstract) which you wanted to use in javascript must implement the `Exportable` interface, or could be exported via the `ExportOverlay` technique. Of course primitives and classes which extends the `JavaScriptObject` like `Element`, `JsArray`, etc can be used directly.

### Exportable ###
It is the marker interface to export a class to javascript. Note that the class should be public.
```
 public class MyClass implements Exportable {
 }
```
### ExportOverlay ###
It is used to export a class or interface which we don't want to modify.

In this example, `MyClass` will be exported to use methods in  `MyOtherClass` which does not implement Exportable.

It is a technique very convenient to export code without modifying the original class nor adding gwt-exporter dependency to the original package.

You can use an interface where you define methods to export from the original class, or you can use a class where you can add extra functionality like customized constructors, export static methods etc.
When you use a class instead of an interface, you have either: stub all methods to export or use an abstract class.
```
 public class MyOtherClass {
   public static void method1() {
   }
   public String method2() {
     return "hello";
   }
   public void method3() {
   }
 }
```
```
 public interface MyClass extends ExportOverlay<MyOtherClass> {
   // no way to export static methods in interfaces
   /* static void method1() {} */

   // Instance methods with the same signature than in the original class
   String method2();
   void method3();
 }
```
```
 public abstract class MyClass implements ExportOverlay<MyOtherClass> {
   // always stub static methods     
   public static void method1() {} 

   // stub instance methods or make them abstract
   public String method2 {return null;} 
   public abstract void method3();
 }
```

# Annotations #
Are used to mark/unmark the classes and methods to be exported, to rename the exported package, the class, or method names, to define closures, etc.

### @Export ###
Marks a method, field, constructor, or class as exportable.

When a class is marked with `@Export`, all public methods will be exported unless they are marked with `@NoExport`.

It accepts two parameters: `value` used to define the final name of the method or class in javascript, and `all` which says to the compiler that it has to export methods in super classes.

Tip: If you wanted a static method to be available in the window object you  could mark the method with a name starting with "$wnd.".
```
 public class MyOtherClass implements Exportable {
  public void methodThree() {
  }
 }
```
```
 // Export all public methods in MyClass:
 //   new mypackage.mclass().methodOne()
 //   new mypackage.mclass().methodTwo()
 // And all public methods in MyOtherClass:
 //   new mypackage.mclass().methodThree()
 @Export(value="mclass", all=true)
 public class MyClass extends MyOtherClass implements Exportable {
  public void methodOne() {
  }
  public void methodTwo() {
  }
 }
```
```
 // Export only one instance method
 //   new mypackage.OtherClass().method()
 // And one static method in the window object
 //   myFunction('hello')
 public class OtherClass implements Exportable {
  public void methodOne() {
  }
  @Export("method")
  public void methodTwo() {
  }
  @Export("$wnd.myFunction");
  public static void staticMethod(String parameter) {
  }
  public static void otherStaticMethod() {
  }
 }
```

### @NoExport ###
If a class is marked to export, use this method to selectively disable certain methods or fields from being exported.
```
 // method1, method4, method5 will be exported but not 
 // method2, method3 which are enclosed between @NoExport and @Export
 @Export
 public class MyClass implements Exportable {
  public void method1() {
  }
  @NoExport
  public void method2() {
  }
  public void method3() {
  }
  @Export
  public void method4() {
  }
  public void method5() {
  }
 }
```

### @ExportPackage ###
Used to rename the package name.

Normally gwt-exporter uses the java full qualified name to export the class, but you can redefine this using both annotations: `@ExportPackage` to rename the package, and `@Export` to rename the class name.

Note: Nested classes inherit the package name of the enclosing class unless they redefine it.

Tip: If you wanted the class available in the window object use an empty package name.

```
 package mypackage;
 // The exported method will be available as:
 // p.c.m() instead of mypackage.MyClass.method()
 @ExportPackage("p")
 @Export("c");
 public class MyClass implements Exportable {
  @Export("m");
  public void method() {
  }
 }
```

### @ExportClosure ###
Indicates that an interface allows JS functions to be automatically promoted to its type when it appears as an argument to an exported function. For example:

```
  @Export
  @ExportClosure
  public interface JsClosure extends Exportable {
    public void execute(String par1, String par2);
  }

  @Export("dpicker");
  @ExportPackage("jsc")
  public class DatePicker implements Exportable {
  public executeJsClosure(JsClosure closure){
   closure.execute("Hello", "Friend");
  }
```
```
  var picker = new jsc.dpicker();
  picker.executeJsClosure(function(a, b){
     alert a + " " + b;
  });
```

You can export closures with more than one executable method, but you have to be aware of which method is being called each time to provide enough arguments to the javascript function. Also it is possible to export a class via `ExportClosure` if you combine it with the `ExportOverlay` interface :

```
 public class Function() {
   public String method1(String a) {
     return null;
   }
   public boolean method2(int a) {
     return false;
   }
 }

 @ExportOverlay
 public interface MyFunction extends ExportOverlay<Function> {
   String method1(String a);
   boolean method2(int a);
 }
 @ExportPackage("js")
 @Export(value="mclass")
 public class MyClass extends MyOtherClass implements Exportable {
  public void runMethodOne(Function f) {
    String a = f.method1("hello");
  }
  public void runMethodTwo(Function f) {
    boolean b = f.method2(4);
  }

 }
```
```
 <script>
  var c = new js.mclass();
  alert(c.runMethodOne(function(a) {
    return "-> " + a;
  });
  alert(c.runMethodTwo(function(a) {
    return a > 10 ? true : false;
  });
 </script>
```

### @ExportConstructor ###
Marks a static method which returns a class instance to be used as a constructor in javascript.

Tip: It is useful to maintain just one instance of the same class like it is shown in the next example:

```
 @ExportPackage("gwt")
 @Export("c")
 public static class TestConstructors implements Exportable {
   private static TestConstructors instance;
   private String msg;

   @ExportConstructor
   public static TestConstructors constructor(String msg) {
     if (instance == null) {
       instance = new TestConstructors();
       instance.msg = msg;
     }
     return instance;
   }
   // Constructor is private
   private TestConstructors() {
   }
   public String echo() {
    return msg;
   }
 }
```

### @ExportStaticMethod ###
> Marks a static method in an `ExportOverlay` class to be exported as an static method in the namespace of the original class. See the example below.

### @ExportInstanceMethod ###
> Marks a static method in an `ExportOverlay` class to be exported as an instance method of the original class.

> Because this method is static, the first argument must be an instance of the original class.

```
  public static class Person {
    private String name;
    public Person(String cname) {
      name = cname;
    }
    public String getName() {
      return name;
    }
  }

  @ExportPackage("ex")
  @Export("Man")
  public static class XPerson implements ExportOverlay<Person>{
    public XPerson(String s){}
    
    @Export("name")
    public String getName() {return null;}

    @ExportConstructor
    public static Person constructor(String name, String surname) {
      return new Person(name + " " + surname);
    }

    @ExportInstanceMethod("foo")
    public static String instanceMethod(Person instance, String surname) {
      return instance.getName() + "-" + surname;
    }
    
    @ExportStaticMethod("sfoo")
    public static String staticMethod(String name)  {
      return name + " sfoo";
    }
  }
```
```
 <script>
  var p1 = new ex.Man('manolo');
  p1.name(); //returns: manolo
  
  var p2 = new ex.Man('manuel', 'carrasco');
  p2.name(); //returns: manuel carrasco
  
  p1.foo('carrasco'); // returns: manolo - carrasco
  
  ex.Man.sfoo('jose'); // returns: jose sfoo
 </script>
```

### @ExportJsInitMethod ###
> Marks an instance method in the `Exportable` class to return the JavascriptObject which will be used when wrapping the gwt object.
> The returned object will be extended with all the exported methods.
```
  public static class Q {
    private NodeList<Element> nodeList = JavaScriptObject.createArray().cast();
    public Q() {
      nodeList = Document.get().<Element>cast().getElementsByTagName("body");
    }
    public int size() {
      return nodeList.getLength();
    }
    public NodeList<Element> get() {
      return nodeList;
    }
  }
  
  @ExportPackage("")
  @Export("JQ")
  public static class EJQ implements ExportOverlay<Q> {
    public int size(){return 0;}
    @ExportJsInitMethod
    public NodeList<Element> get(){return null;}
  }
```
```
 <script>
   var jq = new JQ();
   // call a property of the original js object
   jq.length; // returns: 1
   // call an exported method
   jq.size(); // returns: 1
 </script>
```
### @ExportAfterCreateMethod ###
> Marks a static method in an `Exportable` or an `ExportOverLay` class so as it will run after the class has been exported.
> It is run just once and it is useful to redefine name-spaces, run jsni code, or whatever you needed to do after the class has been exported.

```
 @Export
 public class MyClass implements Exportable {
   public void method1() {
   }
   @ExportAfterCreatedMethod
   public static void runAfter() {
     GWT.create(Something.class);
   }
 }
```

# Generator of Javascript API Documentation #
Gwtexporter comes with the class `JsDoclet` able to generate the javascript api documentation for your project. The class inspects your sources and documents exported methods and classes.

Note: jsdoclet does not work well with ExportOverlay classes.

  * You can run it from your IDE, for instance in eclipse you can run `Project -> Generate Javadoc ... -> Use custom doclet` like is shown in this image (Note that it is not clear where eclipse will leave the jsdoc.html file, in linux it is in the user's home):

![http://gwt-exporter.googlecode.com/files/jsdoc_eclipse.jpg](http://gwt-exporter.googlecode.com/files/jsdoc_eclipse.jpg)

  * Or you can setup your maven project to generate the documentation in the package phase, you have a pom example [here](http://code.google.com/p/gwt-exporter/wiki/MavenConfiguration#JsDoclet).

  * Finally you have to look for the jsdoc.html file in your home folder if you used eclipse, or in the target/ folder of your maven project, which will look similar to:

![http://gwt-exporter.googlecode.com/files/jsdoc_html.jpg](http://gwt-exporter.googlecode.com/files/jsdoc_html.jpg)