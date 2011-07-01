package org.timepedia.exporter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.Window;

/**
 * Holds utility methods and wrapper state
 *
 * @author Ray Cromwell
 */
public class ExporterUtil {

  private interface ExportAll extends Exportable {

  }

  private static ExporterBaseImpl impl = GWT.create(ExporterBaseImpl.class);

  public static void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
    impl.addTypeMap(type.getClass(), exportedConstructor);
  }

  public static void addTypeMap(Class type,
      JavaScriptObject exportedConstructor) {
    impl.addTypeMap(type, exportedConstructor);
  }

  public static void declarePackage(String packageName,
      String enclosingClasses) {
    impl.declarePackage(packageName, enclosingClasses);
  }

  public static void exportAll() {
    GWT.create(ExportAll.class);
  }

//  public static void exportAllAsync() {
//    GWT.runAsync(new RunAsyncCallback() {
//      public void onFailure(Throwable reason) {
//        throw new RuntimeException(reason);
//      }
//
//      public void onSuccess() {
//        GWT.create(ExportAll.class);
//        onexport();
//      }
//
//      private native void onexport() /*-{
//        $wnd.onexport();
//      }-*/;
//    });
//  }

  public static JavaScriptObject getDispatch(Class clazz, String meth,
      JsArray arguments, boolean isStatic) {
    return impl.getDispatch(clazz, meth, arguments, isStatic);
  }

  public static native byte getStructuralFieldbyte(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static native char getStructuralFieldchar(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static native double getStructuralFielddouble(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static native float getStructuralFieldfloat(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static native int getStructuralFieldint(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static long getStructuralFieldlong(JavaScriptObject jso,
      String field) {
    return (long) getStructuralFielddouble(jso, field);
  }

  public static native <T> T getStructuralFieldObject(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static native short getStructuralFieldshort(JavaScriptObject jso,
      String field) /*-{
      return jso[field];
  }-*/;

  public static void registerDispatchMap(Class clazz, JavaScriptObject dispMap,
      boolean isStatic) {
    impl.registerDispatchMap(clazz, dispMap, isStatic);
  }

  public static native void setStructuralField(JavaScriptObject jso,
      String field, Object val) /*-{
      jso[field]=type;
  }-*/;

  public static void setWrapper(Object instance, JavaScriptObject wrapper) {
    impl.setWrapper(instance, wrapper);
  }

  public static JavaScriptObject typeConstructor(Exportable type) {
    return impl.typeConstructor(type);
  }

  public static JavaScriptObject typeConstructor(String type) {
    return impl.typeConstructor(type);
  }

  public static JavaScriptObject wrap(Exportable type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(Exportable[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(String[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(double[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(float[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(int[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(char[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(byte[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(long[] type) {
    return impl.wrap(type);
  }

  public static JavaScriptObject wrap(short[] type) {
    return impl.wrap(type);
  }

  public static String[] toArrString(JavaScriptObject type) {
    return impl.toArrString(type.<JsArrayString>cast());
  }
  
  public static double[] toArrDouble(JavaScriptObject type) {
    return impl.toArrDouble(type.<JsArrayNumber>cast());
  }
  
  public static float[] toArrFloat(JavaScriptObject type) {
    return impl.toArrFloat(type.<JsArrayNumber>cast());
  }
  
  public static int[] toArrInt(JavaScriptObject type) {
    return impl.toArrInt(type.<JsArrayNumber>cast());
  }
  
  public static byte[] toArrByte(JavaScriptObject type) {
    return impl.toArrByte(type.<JsArrayNumber>cast());
  }
  
  public static char[] toArrChar(JavaScriptObject type) {
    return impl.toArrChar(type.<JsArrayNumber>cast());
  }
  
  public static long[] toArrLong(JavaScriptObject type) {
    return impl.toArrLong(type.<JsArrayNumber>cast());
  }
  
  public static Object[] toArrObject(JavaScriptObject type) {
    return impl.toArrObject(type);
  }
  
  // Although in Compiled mode we could cast an Exportable[] to any other T[] array
  // In hosted mode it is not possible, so we only support Exportable[] parameter
  public static Exportable[] toArrExport(JavaScriptObject type) {
    return impl.toArrExport(type);
  }
  
  public static boolean isTheSameClass(Object o, Class clazz) {
    return o.getClass() == clazz;
  }
  
  public static native JavaScriptObject computeVarArguments(int len, JavaScriptObject args) /*-{
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
  
  public static native JavaScriptObject unshift(Object o, JavaScriptObject arr) /*-{
    var ret = [o];
    for (i in arr) ret.push(arr[i]);
    return ret;
  }-*/;
}
