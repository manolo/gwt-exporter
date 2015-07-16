# What is gwt-exporter? #
The project is a GWT module which contains a generator capable of taking GWT classes and exporting them as ordinary Javascript APIs callable from hand-written Javascript, without having to deal with JSNI, and suitable for use in mashups.

Develop an application or library in GWT and use gwtexporter annotations to make classes and methods available from javascript.

The project includes a class to generate the Api documentation of the exported javascript.

# Documentation #
Take a look to the [Getting Started Guide](GettingStarted.md) to learn how to use it.

[Here](Articles.md) you have are a set of useful links you can read as well.


# gwt-exporter in action #
  * In your java code modify your classes to implement the `Exportable` interface, and add some extra annotations
```
  package com.example;

  @Export() /* [1] */
  public class Person implements /* [2] */ Exportable {
    private String name;
    public Person(String name) {
      this.name = name;
    }
    public String execute(String key, long[] values) {
      return  key + " : " + values.length;
    }
    public String foo(Foo foo) {
      return foo.foo();
    }
  }

  public class Foo implements /* [3] */ Exportable {
    public String bar() {
      return "bar,";
    }
  }

  // [1] Export this class and all its public methods.
  // [2] [3] Mark classes as exportable: use the gwt-exporter generator.
  // [3] because this class is not annotated with @Export, it will be exported 
  //     only in the case it was used in a exported method.
```

  * In your `onModuleLoad` you have to say to the compiler which classes to export
```
  public void onModuleLoad() {
    GWT.exportAll(); // Export all Exportable classes, otherwise use GWT.create per class.
  }
```
  * **IMPORTANT:** The exported stuff will not be available in JS until your GWT app is fully loaded.
  * In compile time, gwt-exporter will decorate your classes with the necessary jsni code so as they are callable from javascript:
```
  <javascript>
    var person = new com.example.Person('manolo')
    var foo = new com.example.Person.Foo()
    foo.bar();
    person.foo(foo)
    person.execute('baz', [1,2,3])
  </javascript>
```

  * Additionally gwt-exporter's plenty of features to make your js api look better
```
  
  @ExportPackage("pkg") // Change the js namespace from "com.example" to "pkg" 
  @Export("person") // Change the class name from "Person" to "person"
  public class Person implements Exportable {
   [...]
    @Export("baz") // Rename methods
    public String execute(String key, long[] values) {
      [...]
    }

    public String run(Order callback) { // Run js functions
      callback.success("ok");
    }
  }

  @ExportClosure // Convert js functions to java objects
  public interface Order implements Exportable {
     void success(String msg);
  }

```
```
  <javascript>
    var person = new pkg.person('manolo')
    person.baz('baz', [1,2,3])
    person.run(function(msg){
     alert('msg')
    });
  </javascript>
```
* Articles and posts *

Here you have a set of articles, tutorials and links related with GwtExporter:

  * The [Getting started guide](wiki/GettingStarted) is the official documentation of the library.
  * [English](Exporting-GWT-to-JS) and [Spanish](Exportando-GWT-a-JS) tutorials.
  * [jsQuery](http://code.google.com/p/gwtquery/wiki/JsQuery) is a clone of jquery written in gwt and exported with Gwt. You can take a look to [this presentation](http://www.slideshare.net/dodotis/gquery-a-jquery-clone-for-gwt-rivieradev-2011) to get more information.
  * A list of some posts about gwtexporter in Ray's blog.
    * [Announcemen of a doclet class able to generate js-api documentation using javadoc comments](http://timepedia.blogspot.com/2009/01/gwt-exporter-now-generates-javascript.html)
    * [Ray's thoughts about the future of gwt exporter in March,2003](http://timepedia.blogspot.com/2009/03/whats-next-for-gwt-exporter.html)
    * [New features in 2.05](http://timepedia.blogspot.com/2009/03/gwt-exporter-205-released.html)
    * [Bug in 2.05](http://timepedia.blogspot.com/2009/04/small-update-to-gwt-exporter.html)
    * [New features in 2.09](http://timepedia.blogspot.com/2009/12/gwt-exporter-209-released.html)
