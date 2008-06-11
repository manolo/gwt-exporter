package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class performs the generation of export methods for a single class
 *
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public class ClassExporter {

  private TreeLogger logger;

  private GeneratorContext ctx;

  private ExportableTypeOracle xTypeOracle;

  private SourceWriter sw;

  private ArrayList<JExportableClassType> exported;

  private HashSet<String> visited;

  private static final String ARG_PREFIX = "arg";

  public ClassExporter(TreeLogger logger, GeneratorContext ctx) {
    this(logger, ctx, new HashSet<String>());
  }

  public ClassExporter(TreeLogger logger, GeneratorContext ctx,
      HashSet<String> visited) {
    this.logger = logger;
    this.ctx = ctx;
    // a type oracle that can answer questions about whether types are
    // exportable
    xTypeOracle = new ExportableTypeOracle(ctx.getTypeOracle(), logger);
    this.visited = visited;
    exported = new ArrayList<JExportableClassType>();
  }

  /**
   * This method generates an implementation of the specified interface that
   * accepts a JavaScriptObject in its constructor containing a callback. It
   * then delegates the single-method of the interface to this callback. <p/>
   * For example: <p/> <p/> / ** * @gwt.exportClosure * / public interface
   * ClickListener implements Exportable { public void onClick(Sender s); } <p/>
   * generates a delegation class <p/> public class ClickListenerImpl implements
   * Exporter, ClickListener { <p/> private JavaScriptObject jso; public
   * ClickListenerClosure(JavaScriptObject jso) { this.jso = jso; } <p/> public
   * void onClick(Sender s) { invoke(jso, ExporterBase.wrap(s)); } <p/> public
   * native void invoke(JavaScriptObject closure, JavascriptObject s) {
   * closure(s); } <p/> }
   */
  public void exportClosure(JExportableClassType requestedType)
      throws UnableToCompleteException {

    if (requestedType == null) {
      logger.log(TreeLogger.ERROR, "Type '"
          + requestedType.getQualifiedSourceName()
          + "' does not implement Exportable", null);
      throw new UnableToCompleteException();
    }

    // get the name of the Java class implementing Exporter
    String genName = requestedType.getExporterImplementationName();

    sw.indent();

    // export constructor
    sw.println("private " + ExportableTypeOracle.JSO_CLASS + " jso;");
    sw.println();
    sw.println("public " + genName + "() {}");

    sw.println(
        "public " + genName + "(" + ExportableTypeOracle.JSO_CLASS + " jso) {");
    sw.indent();
    sw.println("this.jso = jso;");
    sw.outdent();
    sw.println("}");
    sw.println();

    // export static factory method
    sw.println("public static " + genName + " makeClosure("
        + ExportableTypeOracle.JSO_CLASS + " closure) {");
    sw.indent();
    sw.println("return new " + genName + "(closure);");
    sw.outdent();
    sw.println("}");
    sw.println();

    JExportableMethod[] methods = requestedType.getExportableMethods();

    if (methods.length != 1) {
      logger.log(TreeLogger.ERROR, "Interface "
          + requestedType.getQualifiedSourceName() + " has more than one "
          + "declared method. @gwt.exportClosure only currently works for "
          + "single method interfaces.", null);
      throw new UnableToCompleteException();
    }

    JExportableMethod method = methods[0];
    JExportableType retType = method.getExportableReturnType();
    if (retType == null) {
      logger.log(TreeLogger.ERROR,
          "Return type of method " + method + " is not exportable.", null);
      throw new UnableToCompleteException();
    }

    if (retType.needsExport() && !exported
        .contains(retType.getQualifiedSourceName())) {
      if (exportDependentClass(retType.getQualifiedSourceName())) {
        exported.add((JExportableClassType) retType);
      }
    }

    exportDependentParams(method);

    boolean isVoid = retType.getQualifiedSourceName().equals("void");
    boolean noParams = method.getExportableParameters().length == 0;
    sw.print("public " + method.getExportableReturnType()
        .getQualifiedSourceName());

    sw.print(" " + method.getName() + "(");
    declareParameters(method, true);
    sw.println(") {");
    sw.indent();
    sw.print((isVoid ? "" : "return ") + "invoke(jso" + (noParams ? "" : ","));
    declareJavaPassedValues(method, false);
    sw.println(");");
    sw.outdent();
    sw.println("}");
    sw.println();
    sw.print(
        "public native " + (isVoid ? "void" : method.getExportableReturnType()
            .getQualifiedSourceName()));
    sw.print(" invoke(" + ExportableTypeOracle.JSO_CLASS + " closure");
    if (method.getExportableParameters().length > 0) {
      sw.print(", ");
    }

    declareParameters(method, true);
    sw.println(") /*-{");
    sw.indent();
    sw.print((!isVoid ? "var result= " : "") + "closure(");
    declareJavaPassedValues(method, true);
    sw.println(");");
    if (retType.needsExport() && !isVoid) {
      sw.println("if(result != null && result != undefined) "
          + "result=result.instance;");
      sw.println("else if(result == undefined) result=null;");
    }
    if (!isVoid) {
      sw.println("return result;");
    }
    sw.outdent();
    sw.println("}-*/;");
    sw.println();
    sw.outdent();
  }

  /**
   * This method generates an implementation class that implements Exporter and
   * returns the fully qualified name of the class.
   */
  public String exportClass(String requestedClass, boolean export)
      throws UnableToCompleteException {

    // JExportableClassType is a wrapper around JClassType
    // which provides only the information and logic neccessary for
    // the generator
    JExportableClassType requestedType = xTypeOracle
        .findExportableClassType(requestedClass);

    // add this so we don't try to recursively reexport ourselves later
    exported.add(requestedType);
    visited.add(requestedType.getQualifiedSourceName());

    if (requestedType == null) {
      logger.log(TreeLogger.ERROR,
          "Type '" + requestedClass + "' does not implement Exportable", null);
      throw new UnableToCompleteException();
    }

    // get the name of the Java class implementing Exporter
    String genName = requestedType.getExporterImplementationName();

    // get the package name of the Exporter implementation
    String packageName = requestedType.getPackageName();

    // get a fully qualified reference to the Exporter implementation
    String qualName = requestedType.getQualifiedExporterImplementationName();

    boolean isClosure = xTypeOracle.isClosure(requestedClass);

    // try to construct a sourcewriter for the qualified name
    if (isClosure) {
      sw = getSourceWriter(logger, ctx, packageName, genName, "Exporter",
          requestedType.getQualifiedSourceName());
    } else {
      sw = getSourceWriter(logger, ctx, packageName, genName, "Exporter");
    }
    if (sw == null) {
      return qualName; // null, already generated
    }

    if (export) {
      if (isClosure) {
        exportClosure(requestedType);
      }

      sw.indent();

      // here we define a JSNI Javascript method called export0()
      sw.println("public native void export0() /*-{");
      sw.indent();

      // if not defined, we create a Javascript package hierarchy
      // foo.bar.baz to hold the Javascript bridge
      declarePackages(requestedType);

      // export Javascript constructors
      exportConstructor(requestedType);

      // export all static fields
      exportFields(requestedType);

      // export all exportable methods
      exportMethods(requestedType);

      // add map from TypeName to JS constructor in ExporterUtil
      registerTypeMap(requestedType);

      sw.outdent();
      sw.println("}-*/;");

      sw.println();

      // the Javascript constructors refer to static factory methods
      // on the Exporter implementation, referenced via JSNI
      // We generate them here
      exportStaticFactoryConstructors(requestedType);

      // finally, generate the Exporter.export() method
      // which invokes recursively, via GWT.create(),
      // every other Exportable type we encountered in the exported ArrayList
      // ending with a call to export0()

      genExportMethod(requestedType, exported);
      sw.outdent();
    } else {
      sw.indent();
      sw.println("public void export() {}");
      sw.outdent();
    }

    sw.commit(logger);

    // return the name of the generated Exporter implementation class
    return qualName;
  }

  private void registerTypeMap(JExportableClassType requestedType) {
    sw.print(
        "@org.timepedia.exporter.client.ExporterUtil::addTypeMap(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("
            +
//                        "Ljavg/lang/String;" +
//                        "Lcom/google/gwt/core/client/JavaScriptObject;)(" +
            "\"" + requestedType.getQualifiedSourceName() + "\", " + "$wnd."
            + requestedType.getJSQualifiedExportName() + ");");
  }

  /**
   * Exports a static factory method corresponding to each exportable
   * constructor of the class
   */
  private void exportStaticFactoryConstructors(
      JExportableClassType requestedType) {

    JExportableConstructor[] constructors = requestedType
        .getExportableConstructors();

    for (JExportableConstructor constructor : constructors) {
      exportStaticFactoryConstructor(constructor);
    }
  }

  /**
   * Exports all exportable methods of a class
   */
  private void exportMethods(JExportableClassType requestedType)
      throws UnableToCompleteException {
    HashMap<String, JExportableMethod> visited
        = new HashMap<String, JExportableMethod>();

    HashMap<String, JExportableMethod> staticVisited
        = new HashMap<String, JExportableMethod>();

    for (JExportableMethod method : requestedType.getExportableMethods()) {
      exportMethod(method, visited, staticVisited);
    }
  }

  /**
   * Exports a Javascript constructor as $wnd.packageName.classname =
   * function(args) { if(arg0 is GWT type) { this.instance = arg0; } else
   * this.instance = invoke static factory method with args }
   */
  private void exportConstructor(JExportableClassType requestedType)
      throws UnableToCompleteException {
    sw.println("if($wnd." + requestedType.getJSQualifiedExportName() + ") {");
    sw.println(
        "var pkg = $wnd." + requestedType.getJSQualifiedExportName() + ";");
    sw.println("}");

    // constructor.getJSQualifiedExportName() returns fully qualified package
    // + exported class name
    sw.print(
        "$wnd." + requestedType.getJSQualifiedExportName() + " = function(");

    // for every parameter 0..n of the constructor, we generate
    // arg0, ..., argn
//        declareJSParameters(constructor);
    sw.println(") {");
    sw.indent();
    // check if this is being used to wrap GWT types
    // e.g. code is calling constructor as
    // new $wnd.package.className(opaqueGWTobject)
    // if so, we store the opaque reference in this.instance
    sw.println("if(arguments.length == 1 && (arguments[0] != null && "
        + "@com.google.gwt.core.client.GWT::getTypeName(Ljava/lang/Object;)(arguments[0]) == '"
        + requestedType.getQualifiedSourceName() + "')) {");
    sw.indent();

    sw.println(" this.instance = arguments[0];");
    sw.outdent();
    sw.println("}");

    JExportableConstructor[] constructors = requestedType
        .getExportableConstructors();

    // used to hold arity of constructors that have been generated
    HashMap<Integer, JExportableConstructor> arity
        = new HashMap<Integer, JExportableConstructor>();

    for (JExportableConstructor constructor : constructors) {
      int numArguments = constructor.getExportableParameters().length;
      JExportableConstructor conflicting = arity.get(numArguments);
      if (conflicting != null) {
        logger.log(TreeLogger.ERROR, "Constructor " + conflicting + " with "
            + numArguments + " " + "arguments conflicts with " + constructor
            + "." + "Two constructors may not have identical numbers of "
            + "arguments.", null);
        throw new UnableToCompleteException();
      }
      arity.put(numArguments, constructor);
      sw.println("else if(arguments.length == " + numArguments + ") {");
      sw.indent();

      // else someone is calling the constructor normally
      // we generate a JSNI call to the matching static factory method
      // and store it in this.instance
      sw.print("this.instance = @" + constructor.getStaticFactoryJSNIReference()
          + "(");

      // pass arguments[0], ..., arguments[n] to the JSNI call
      declareJSPassedValues(constructor, true);
      sw.println(");");
      sw.outdent();
      sw.println("}");
    }

    sw.outdent();
    sw.println("}");

    JExportableClassType superClass = requestedType
        .getExportableSuperClassType();

    if (superClass != null && superClass.needsExport() && !exported
        .contains(superClass)) {
      if (exportDependentClass(superClass.getQualifiedSourceName())) {
        ;
      }
      exported.add(superClass);
    }
    // we assign the prototype of the class to underscore so we can use it
    // later to define a bunch of methods
    sw.print("var _=$wnd." + requestedType.getJSQualifiedExportName()
        + ".prototype = ");
    sw.println(superClass == null ? "new Object();"
        : "new $wnd." + superClass.getJSQualifiedExportName() + "();");

    // restore inner class namespace
    sw.println("if(pkg) {");
    sw.println("for(p in pkg) { $wnd."
        + requestedType.getJSQualifiedExportName() + "[p]=pkg[p]; }");
    sw.println("}");
  }

  /**
   * We create a static factory method public static [typeName] ___create(args)
   * that just invokes the real constructor with the args
   */
  private void exportStaticFactoryConstructor(
      JExportableConstructor constructor) {
    JExportableClassType consType = (JExportableClassType) constructor
        .getExportableReturnType();
    String typeName = consType
        .getQualifiedSourceName();
    sw.print("public static " + typeName + " "
        + constructor.getStaticFactoryMethodName() + "(");
    declareParameters(constructor);
    sw.println(") {");
    sw.indent();
    sw.print("return new " + typeName + "(");
    declareJavaPassedValues(constructor, false);
    sw.println(");");
    sw.outdent();
    sw.println("}");
  }

  /**
   * Generate comma separated list of argnames, arg0, ..., arg_n where n =
   * number of parameters of method
   *
   * @param useArgumentsArray use arguments[n] instead of argn
   */
  private void declareJSPassedValues(JExportableMethod method,
      boolean useArgumentsArray) {
    JExportableParameter params[] = method.getExportableParameters();
    for (int i = 0; i < params.length; i++) {
      sw.print(params[i].getExportParameterValue(
          useArgumentsArray ? "arguments[" + i + "]" : ARG_PREFIX + i));
      if (i < params.length - 1) {
        sw.print(", ");
      }
    }
  }

  /**
   * Generate comma separated list of argnames, arg0, ..., arg_n where n =
   * number of parameters of method
   *
   * @param wrap whether to wrap the passed value with ExporterBase::wrap
   */
  private void declareJavaPassedValues(JExportableMethod method, boolean wrap) {
    JExportableParameter params[] = method.getExportableParameters();
    for (int i = 0; i < params.length; i++) {
      JExportableType eType = params[i].getExportableType();
      boolean needExport = eType != null && eType.needsExport();
      if (wrap && needExport) {
        sw.print(
            "@org.timepedia.exporter.client.ExporterUtil::wrap(Lorg/timepedia/exporter/client/Exportable;)(");
      }
      sw.print(ARG_PREFIX + i);
      if (wrap && needExport) {
        sw.print(")");
      }
      if (i < params.length - 1) {
        sw.print(", ");
      }
    }
  }

  /**
   * Generate comma separated list of argnames, arg0, ..., arg_n where n =
   * number of parameters of constructor
   *
   * @param includeTypes true if arg names should have declared types
   */
  private void declareParameters(JExportableMethod method,
      boolean includeTypes) {
    JExportableParameter params[] = method.getExportableParameters();
    for (int i = 0; i < params.length; i++) {
      sw.print(
          (includeTypes ? params[i].getTypeName() : "") + " " + ARG_PREFIX + i);
      if (i < params.length - 1) {
        sw.print(", ");
      }
    }
  }

  /**
   * declare java typed Java method parameters
   */
  private void declareParameters(JExportableMethod method) {
    declareParameters(method, true);
  }

  /**
   * declare type-less Javascript method parameters
   */
  private void declareJSParameters(JExportableMethod method) {
    declareParameters(method, false);
  }

  /**
   * For each exportable field Foo, we generate the following Javascript:
   * $wnd.package.className.Foo = JSNI Reference to Foo
   */
  private void exportFields(JExportableClassType requestedType)
      throws UnableToCompleteException {
    for (JExportableField field : requestedType.getExportableFields()) {

      sw.print("$wnd." + field.getJSQualifiedExportName() + " = ");
      sw.println("@" + field.getJSNIReference() + ";");
    }
  }

  /**
   * Export a method If the return type of the method is Exportable, we invoke
   * ClassExporter recursively on this type <p/> For static methods, the
   * Javascript looks like this: $wnd.package.className.staticMethod =
   * function(args) { // body } <p/> for regular methods, it looks like <p/>
   * _.methodName = function(args) { //body } <p/> where _ is previously
   * assigned to $wnd.package.className.prototype <p/> For methods returning
   * Exportable types, the body looks like <p/> return new
   * $wnd.package.className(this.instance.@methodNameJSNI(args)); <p/> which
   * wraps the returned type, otherwise it looks like this <p/> return
   * this.instance.@methodNameJSNI(args); <p/> for primitives, String,
   * subclasses of Number, and JavaScriptObject
   */
  private void exportMethod(JExportableMethod method,
      HashMap<String, JExportableMethod> visited,
      HashMap<String, JExportableMethod> staticVisited)
      throws UnableToCompleteException {
    JExportableType retType = method.getExportableReturnType();

    if (retType == null) {
      logger.log(TreeLogger.ERROR,
          "Return type of method " + method.toString() + " is not Exportable.",
          null);
      throw new UnableToCompleteException();
    }

    int arity = method.getExportableParameters().length;
    String name = method.getUnqualifiedExportName();
    String key = name + "_" + arity;

    JExportableMethod conflicting = method.isStatic() ? staticVisited.get(key)
        : visited.get(key);

    if (conflicting != null) {
      logger.log(TreeLogger.ERROR, "Method " + method + " having " + arity
          + " arguments conflicts with " + conflicting + ". "
          + "Two exportable methods cannot have the same number of arguments. "
          + "Use @gwt.export <newName> on one of the methods to disambiguate.",
          null);
      throw new UnableToCompleteException();
    } else {
      if (method.isStatic()) {
        staticVisited.put(key, method);
      } else {
        visited.put(key, method);
      }
    }

    // return type needs to be exported if it is not a primitive
    // String,Number,JSO, etc and it hasn't already been exported
    // we need to export it because we need it to wrap the returned value
    if (retType != null && retType.needsExport() && !exported
        .contains(retType)) {
      if (exportDependentClass(retType.getQualifiedSourceName())) {
        ;
      }
      exported.add((JExportableClassType) retType);
    }

    exportDependentParams(method);

    if (method.isStatic()) {
      sw.print("$wnd." + method.getJSQualifiedExportName() + "= function(");
    } else {
      sw.print("_." + method.getUnqualifiedExportName() + "= function(");
    }
    declareJSParameters(method);
    sw.print(") { ");
    boolean isVoid = retType.getQualifiedSourceName().equals("void");

    sw.print((isVoid ? "" : "var x=")
        + (method.isStatic() ? "@" : "this.instance.@")
        + method.getJSNIReference() + "(");

    declareJSPassedValues(method, false);

    // end method call
    sw.print(");");

    if (!retType.needsExport()) {
      sw.print(isVoid ? "" : "return (");
    } else {

      sw.print((isVoid ? "" : "return ")
          + "@org.timepedia.exporter.client.ExporterUtil::wrap(Lorg/timepedia/exporter/client/Exportable;)("

      );
    }

    // end wrap() or non-exportable return case call
    if (!isVoid) {
      sw.println("x);");
    }
    sw.println("}");
  }

  private void exportDependentParams(JExportableMethod method)
      throws UnableToCompleteException {
    // for convenience to the developer, let's export any exportable
    // parameters
    for (JExportableParameter param : method.getExportableParameters()) {
      JExportableType eType = param.getExportableType();
      if (eType != null && eType.needsExport() && !exported.contains(eType)) {
        if (exportDependentClass(eType.getQualifiedSourceName())) {
          exported.add((JExportableClassType) eType);
        }
      }
    }
  }

  private boolean exportDependentClass(String qualifiedSourceName)
      throws UnableToCompleteException {

    if (visited.contains(qualifiedSourceName)) {
      return false;
    }
    visited.add(qualifiedSourceName);
    ClassExporter exporter = new ClassExporter(logger, ctx, visited);
    exporter.exportClass(qualifiedSourceName, true);
    return true;
  }

  /**
   * For each subpackage of sub1.sub2.sub3... we create a chain of objects
   * $wnd.sub1.sub2.sub3
   */
  private void declarePackages(JExportableClassType requestedClassType) {
    String requestedPackageName = requestedClassType.getJSExportPackage();
    String superPackages[] = requestedPackageName.split("\\.");
    String prefix = "";
    for (int i = 0; i < superPackages.length; i++) {
      if (!superPackages[i].equals("client")) {
        sw.println("if(!$wnd." + prefix + "" + superPackages[i] + ") $wnd."
            + prefix + superPackages[i] + " = {} ");
        prefix += superPackages[i] + ".";
      }
    }
    String[] enclosingClasses = requestedClassType.getEnclosingClasses();
    for (String enclosingName : enclosingClasses) {
      sw.println("if(!$wnd." + prefix + "" + enclosingName + ") $wnd." + prefix
          + enclosingName + " = {} ");
      prefix += enclosingName + ".";
    }

//        sw.println(
//                "if(!$wnd.___gwtwrapper) $wnd.___gwtwrapper = function(arg) {" +
//                        " return { instance: arg }; }"
//        );

  }

  /**
   * Generate the main export method <p/> <p/> We generate a method that looks
   * like: <p/> public void export() { Exporter export1 =
   * (Exporter)GWT.create(ExportableDependency1.class) export1.export(); <p/>
   * Exporter export2 = (Exporter)GWT.create(ExportableDependency2.class)
   * export2.export(); <p/> ... export0(); }
   *
   * @param exported a list of other types that we depend on to be exported
   */
  private void genExportMethod(JExportableClassType requestedType,
      ArrayList<JExportableClassType> exported) {
    sw.println("public void export() { ");
    sw.indent();
    // first, export our dependencies
    int exprCount = 0;
    for (JExportableClassType classType : exported) {
      if (requestedType.getQualifiedSourceName()
          .equals(classType.getQualifiedSourceName())) {
        continue;
      }
      String qualName = classType.getQualifiedSourceName();

      String var = "export" + exprCount++;
      sw.println(ExportableTypeOracle.EXPORTER_CLASS + " " + var + " = ("
          + ExportableTypeOracle
          .EXPORTER_CLASS + ") GWT.create(" + qualName + ".class);");
      sw.println(var + ".export();");
    }

    // now export our class
    sw.println("export0();");

    sw.outdent();
    sw.println("}");
  }

  /**
   * Get SourceWriter for following class and preamble package packageName;
   * import com.google.gwt.core.client.GWT; import org.timepedia.exporter.client.Exporter;
   * public class className implements interfaceName (usually Exporter) { <p/>
   * }
   *
   * @param interfaceNames vararg list of interfaces
   */
  protected SourceWriter getSourceWriter(TreeLogger logger,
      GeneratorContext context, String packageName, String className,
      String... interfaceNames) {
    PrintWriter printWriter = context.tryCreate(logger, packageName, className);
    if (printWriter == null) {
      return null;
    }
    ClassSourceFileComposerFactory composerFactory
        = new ClassSourceFileComposerFactory(packageName, className);
    composerFactory.addImport("com.google.gwt.core.client.GWT");
    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }

    composerFactory.addImport("org.timepedia.exporter.client.Exporter");
    return composerFactory.createSourceWriter(context, printWriter);
  }
}
