package org.timepedia.exporter.client;

import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;

/**
 * Methods used to maintain a mapping between JS types and Java (GWT) objects.
 */
public class ExporterBaseActual extends ExporterBaseImpl {
  
  public static final String WRAPPER_PROPERTY = "__gwtex_wrap";

  private native static JavaScriptObject wrap0(Object type,
      JavaScriptObject constructor) /*-{
           return new (constructor)(type);
      }-*/;

  private HashMap typeMap = new HashMap();

  private HashMap<Class, JavaScriptObject> dispatchMap
      = new HashMap<Class, JavaScriptObject>();

  private HashMap<Class, JavaScriptObject> staticDispatchMap
      = new HashMap<Class, JavaScriptObject>();

  //TODO: track garbage collected wrappers and remove mapping

  private IdentityHashMap<Object, JavaScriptObject> wrapperMap = null;

  public ExporterBaseActual() {
    if (!GWT.isScript()) {
      wrapperMap = new IdentityHashMap<Object, JavaScriptObject>();
    }
  }

  public void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
    addTypeMap(type.getClass(), exportedConstructor);
  }

  public void addTypeMap(Class type, JavaScriptObject exportedConstructor) {
    typeMap.put(type, exportedConstructor);
  }

  public JavaScriptObject typeConstructor(Object type) {
    return typeConstructor(type.getClass());
  }

  public JavaScriptObject typeConstructor(Class type) {
    Object o = typeMap.get(type);
    return (JavaScriptObject) o;
  }

  private JavaScriptObject getWrapper(Object type) {
    JavaScriptObject wrapper = null;
    if (!GWT.isScript()) {
      wrapper = wrapperMap.get(type);
    } else {
      wrapper = getWrapperJS(type, WRAPPER_PROPERTY);
    }

    if (wrapper == null) {
      setWrapper(type);
    }
    return wrapper;
  }

  private static native JavaScriptObject reinterpretCast(Object nl) /*-{
    return nl;
  }-*/;
  
  private static native <T> T[] reinterpretArray(Object nl) /*-{
    return nl;
  }-*/;

  @Override
  public JavaScriptObject wrap(Date[] type) {
    JsArray<JavaScriptObject> ret = JavaScriptObject.createArray().cast();
    for (Date d : type) {
      ret.push(dateToJsDate(d));
    }
    return ret;
  }
  
  @Override
  public JavaScriptObject wrap(float[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray = JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }  

  @Override
  public JavaScriptObject wrap(byte[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(char[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(int[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(long[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(short[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(double[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayNumber wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }
  
  @Override
  public JavaScriptObject wrap(String[] type) {
    if (!GWT.isScript()) {
      if (type == null) {
        return null;
      }
      JsArrayString wrapperArray =  JavaScriptObject.createArray().cast();
      for (int i = 0; i < type.length; i++) {
        wrapperArray.set(i, type[i]);
      }
      return wrapperArray;
    } else {
      return reinterpretCast(type);
    }
  }

  @Override
  public JavaScriptObject wrap(Object type) {
    if (type == null) {
      return null;
    }

    JavaScriptObject wrapper = getWrapper(type);
    if (wrapper != null) {
      return wrapper;
    }

    return setWrapper(type);
  }

  @Override
  public JavaScriptObject wrap(Exportable[] type) {
    if (type == null) {
      return null;
    }
    JsArrayObject wrapperArray = JavaScriptObject.createArray().cast();
    for (int i = 0; i < type.length; i++) {
      wrapperArray.setObject(i, wrap(type[i]));
    }
    return wrapperArray;
  }

  public JavaScriptObject setWrapper(Object type) {
    if (type.getClass().isArray()) {
      return JavaScriptObject.createArray();
    }
    JavaScriptObject wrapper = wrap0(type, typeConstructor(type));
    setWrapper(type, wrapper);
    return wrapper;
  }
  
  @Override
  public void setWrapper(Object instance, JavaScriptObject wrapper) {
    if (GWT.isScript()) {
      setWrapperJS(instance, wrapper, WRAPPER_PROPERTY);
    } else {
      setWrapperHosted(instance, wrapper);
    }
  }

  public JavaScriptObject getWrapper(Exportable type) {
    JavaScriptObject wrapper = null;
    if (GWT.isScript()) {
      wrapper = getWrapperJS(type, WRAPPER_PROPERTY);
    } else {
      wrapper = wrapperMap.get(type);
    }
    return wrapper;
  }

  
  @Override
  public JavaScriptObject wrap(JavaScriptObject[] type) {
    if (type == null) {
      return null;
    }
    JsArray<JavaScriptObject> wrapperArray = JavaScriptObject.createArray().cast();
    for (int i = 0; i < type.length; i++) {
      wrapperArray.set(i, type[i]);
    }
    return wrapperArray;
  }
  
  // JsArray.get() returns a JavaScriptObject, so we need this wrapper 
  // class to avoid a casting exception at runtime.
  static class JsArrayObject extends JsArray<JavaScriptObject> {
    protected JsArrayObject(){}
    final public native <T> T getObject(int i) /*-{
      return this[i];
    }-*/;
    final public native <T> void setObject(int i, T o) /*-{
      this[i] = o;
    }-*/;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T> T[] toArrObject(JavaScriptObject j, T[] ret) {
    if (!GWT.isScript()) {
      JsArrayObject s = j.cast();
      int l = s.length();
      for (int i = 0; i < l; i++) {
        Object o = s.getObject(i);
        if (o instanceof JavaScriptObject && getGwtInstance((JavaScriptObject)o) != null) {
          o = getGwtInstance((JavaScriptObject)o);
        }
        ret[i] = (T)o;
      }
    } else {
      return (T[])reinterpretArray(j);
    }
    return ret;
  }
  
  @Override
  public JavaScriptObject[] toArrJsObject(JavaScriptObject p) {
    if (!GWT.isScript()) {
      JsArray<JavaScriptObject> s = p.cast();
      int l = s.length();
      JavaScriptObject[] ret = new JavaScriptObject[l];
      for (int i = 0; i < l; i++) {
        ret[i] = s.get(i);
      }
      return ret;
    } else {
      return reinterpretArray(p);
    }
  }
  
  private final static native Object getGwtInstance(JavaScriptObject o) /*-{
    return o.__gwt_instance || null;
  }-*/;
  
  public Exportable[] toArrExport(JavaScriptObject j) {
    JsArray<JavaScriptObject> s = j.cast();
    int l = s.length();
    Exportable[] ret = new Exportable[l];
    for (int i = 0; i < l; i++) {
      Object o = getGwtInstance(s.get(i));
      if (o == null) {
        o = s.get(i);
      }
      assert (o != null && (o instanceof Exportable));
      ret[i] = (Exportable) o;
    }
   return ret;
  }
  
  public String[] toArrString(JsArrayString s) {
    int l = s.length();
    String[] ret = new String[l];
    for (int i = 0; i < l; i++) {
      ret[i] = s.get(i);
    }
    return ret;
  }
  
  public long[] toArrLong(JsArrayNumber s) {
    int l = s.length();
    long[] ret = new long[l];
    for (int i = 0; i < l; i++) {
      ret[i] = (long)s.get(i);
    }
    return ret;
  }

  public double[] toArrDouble(JsArrayNumber s) {
    int l = s.length();
    double[] ret = new double[l];
    for (int i = 0; i < l; i++) {
      ret[i] = s.get(i);
    }
    return ret;
  }

  public int[] toArrInt(JsArrayNumber s) {
    int l = s.length();
    int[] ret = new int[l];
    for (int i = 0; i < l; i++) {
      ret[i] = (int)s.get(i);
    }
    return ret;
  }

  public byte[] toArrByte(JsArrayNumber s) {
    int l = s.length();
    byte[] ret = new byte[l];
    for (int i = 0; i < l; i++) {
      ret[i] = (byte)s.get(i);
    }
    return ret;
  }
  public char[] toArrChar(JsArrayNumber s) {
    int l = s.length();
    char[] ret = new char[l];
    for (int i = 0; i < l; i++) {
      ret[i] = (char)s.get(i);
    }
    return ret;
  }
  public float[] toArrFloat(JsArrayNumber s) {
    int l = s.length();
    float[] ret = new float[l];
    for (int i = 0; i < l; i++) {
      ret[i] = (long)s.get(i);
    }
    return ret;
  }
  public Date[] toArrDate(JavaScriptObject j) {
    JsArray<JavaScriptObject> s = j.cast();
    int l = s.length();
    Date[] ret = new Date[l];
    for (int i = 0; i < l; i++) {
      ret[i] = jsDateToDate(s.get(i));
    }
   return ret;
  }
  
  private native JsArray<JavaScriptObject> computeVarArguments(int len, JavaScriptObject args) /*-{
    var ret = [];
    for (i = 0; i < len - 1; i++) 
      ret.push(args[i]);
    var alen = args.length;
    var p = len - 1;
    if (alen >= len && Object.prototype.toString.apply(args[p]) === '[object Array]') {
        ret.push(args[p]);
    } else {
      var a = [];
      for (i = p; i < alen; i++) 
        a.push(args[i]);
      ret.push(a);  
    }
    return ret;
  }-*/;

  @Override
  public native JavaScriptObject unshift(Object o, JavaScriptObject arr) /*-{
    var ret = [o];
    for (i in arr) ret.push(arr[i]);
    return ret;
  }-*/;
  
  @Override
  public JavaScriptObject dateToJsDate(Date d) {
    return numberToJsDateObject(d.getTime());
  }

  @Override
  public Date jsDateToDate(JavaScriptObject jd) {
    return new Date((long)jsDateObjectToNumber(jd));
  }

  private native JavaScriptObject getWrapperJS(Object type, String wrapProp) /*-{
    return type[wrapProp];
  }-*/;

  private void setWrapperHosted(Object instance, JavaScriptObject wrapper) {
    wrapperMap.put(instance, wrapper);
  }

  private native void setWrapperJS(Object instance, JavaScriptObject wrapper,
      String wrapperProperty) /*-{
    instance[wrapperProperty] = wrapper;
  }-*/;

  private native void declarePackage0(JavaScriptObject prefix, String pkg) /*-{
    prefix[pkg] || (prefix[pkg] = {});
  }-*/;

  @Override
  public void declarePackage(String packageName,
      String enclosingClassesString) {
    String superPackages[] = packageName.split("\\.");
    JavaScriptObject prefix = getWindow();
    for (int i = 0; i < superPackages.length; i++) {
      if (!superPackages[i].equals("client")) {
        declarePackage0(prefix, superPackages[i]);
        prefix = getProp(prefix, superPackages[i]);
      }
    }
    String enclosingClasses[] = enclosingClassesString.split("\\.");
    for (String enclosingName : enclosingClasses) {
      if (!enclosingName.trim().equals("")) {
        declarePackage0(prefix, enclosingName);
        prefix = getProp(prefix, enclosingName);
      }
    }
  }

  private static native JavaScriptObject getWindow() /*-{
    return $wnd;
  }-*/;

  private static native JavaScriptObject getProp(JavaScriptObject jso,
      String key) /*-{
    return jso[key];
  }-*/;

  private JavaScriptObject getDispatch(Map<Class, JavaScriptObject> dmap,
      Class clazz, String meth, JsArray<JavaScriptObject> arguments) {

    JsArray<SignatureJSO> sigs = getSigs(dmap.get(clazz).cast(), meth,
        arguments.length());
    JavaScriptObject wFunc = null;
    JavaScriptObject cFunc = null;
    for (int i = 0; i < sigs.length(); i++) {
      SignatureJSO sig = sigs.get(i);
      if (sig.matches(arguments)) {
        JavaScriptObject javaFunc = sig.getFunction();
        if (!GWT.isScript()) {
          JavaScriptObject wrapFunc = sig.getWrapperFunc();
          wFunc = wrapFunc != null ? wrapFunction(wrapFunc, javaFunc) : javaFunc;
        } else {
          wFunc = javaFunc;
        }
        cFunc = sig.getWrapArgumentsFunc();
        break;
      }
    }
    if (wFunc == null) {
      return null;
    } else {
      JsArray<JavaScriptObject>ret = JavaScriptObject.createArray().cast();
      ret.push(wFunc);
      ret.push(arguments);
      ret.push(cFunc);
      return ret;
    }
  }
  
  
  @Override
  public JavaScriptObject getDispatch(Class clazz, String meth,
      JsArray<JavaScriptObject> arguments, boolean isStatic, boolean isVarArgs) {
    Map<Class, JavaScriptObject> dmap = isStatic ? staticDispatchMap : dispatchMap;
    if (isVarArgs) {
      for (int i = 1, l = getMaxArity(dmap.get(clazz).cast(), meth); i <= l; i++) {
        JsArray<JavaScriptObject> args = computeVarArguments(i, arguments);
        JavaScriptObject ret = getDispatch(dmap, clazz, meth, args);
        if (ret != null) {
          return ret;
        }
      }
    } else {
      JavaScriptObject ret = getDispatch(dmap, clazz, meth, arguments);
      if (ret != null) {
        return ret;
      }
    }
    throw new RuntimeException(
        "Can't find exported method for given arguments");
  }

  // this is way more complicated than it needs to be, thanks to hosted mode
  private native static JavaScriptObject wrapFunction(JavaScriptObject wrapFunc,
      JavaScriptObject javaFunc) /*-{
     return function() {
         var i, newArgs = [];
         for(i = 0; i < arguments.length; i++) {
             newArgs[i] = arguments[i].__gwt_instance || arguments[i];
         }
         return wrapFunc.apply(null, [javaFunc.apply(this, newArgs)]);
     };
  }-*/;

  private native JsArray<SignatureJSO> getSigs(JavaScriptObject jsoMap,
      String meth, int arity) /*-{
    return jsoMap[meth][arity];
  }-*/;
  
  private native int getMaxArity(JavaScriptObject jsoMap,
      String meth) /*-{
      var o = jsoMap[meth];
      var r = 0;
      for (k in o) r = Math.max(r, k);
      return r;
  }-*/;
  
  private static native JavaScriptObject numberToJsDateObject(double time) /*-{
    return new Date(time);
  }-*/;

  private static native double jsDateObjectToNumber(JavaScriptObject d) /*-{
    return (d && d.getTime) ? d.getTime(): 0;
  }-*/;

  @Override
  public void registerDispatchMap(Class clazz, JavaScriptObject dispMap,
      boolean isStatic) {
    HashMap<Class, JavaScriptObject> map = isStatic ? staticDispatchMap : dispatchMap;
    JavaScriptObject jso = map.get(clazz);
    if (jso == null) {
      jso = dispMap;
    } else  {
      mergeJso(jso, dispMap);
    }
    map.put(clazz, jso);
  }
  
  private static native void mergeJso(JavaScriptObject o1, JavaScriptObject o2) /*-{
    for(p in o2) {o1[p] = o2[p];}
  }-*/;

  final public static class SignatureJSO extends JavaScriptObject {

    protected SignatureJSO() {
    }

    public boolean matches(JsArray<JavaScriptObject> arguments) {
      // add argument matching logic
      // add structural type checks
      for (int i = 0; i < arguments.length(); i++) {
        Object jsType = getObject(i + 3);
        String argJsType = typeof(arguments, i);
        if (argJsType.equals("object") || argJsType.equals("array")) {
          Object gwtObject = getJavaObject(arguments, i);
          if (gwtObject != null) {
            if (!gwtObject.getClass().equals(jsType)) {
              return false;
            }
          } else if (!jsType.equals("object") && !jsType.equals("array")) {
            return false;
          }
        } else if (!jsType.equals(argJsType)) {
          return false;
        }
      }
      return true;
    }

    public native Object getJavaObject(JavaScriptObject args, int i) /*-{
      return args[i].__gwt_instance || null;
    }-*/;

    public native static String typeof(JavaScriptObject args, int i) /*-{
      return typeof(args[i]);
    }-*/;

    public native Object getObject(int i) /*-{
      return this[i];
    }-*/;

    public native JavaScriptObject getFunction() /*-{
      return this[0];
    }-*/;

    public native JavaScriptObject getWrapperFunc() /*-{
      return this[1];
    }-*/;
    
    public native JavaScriptObject getWrapArgumentsFunc() /*-{
      return this[2];
    }-*/;
  }
}
