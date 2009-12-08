package org.timepedia.exporter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.RunAsyncCallback;

/**
 * Holds utility methods and wrapper state
 *
 * @author Ray Cromwell 
 */
public class ExporterUtil {

  private static ExporterBaseImpl impl = GWT.create(ExporterBaseImpl.class);

  public static void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
    impl.addTypeMap(type.getClass(), exportedConstructor);
  }

  public static void addTypeMap(Class type,
      JavaScriptObject exportedConstructor) {
    impl.addTypeMap(type, exportedConstructor);
  }

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

  public static void declarePackage(String packageName,
      String enclosingClasses) {
    impl.declarePackage(packageName, enclosingClasses);
  }

  public static JavaScriptObject getDispatch(Class clazz, String meth,
      JsArray arguments, boolean isStatic) {
    return impl.getDispatch(clazz, meth, arguments, isStatic);
  }

  public static void registerDispatchMap(Class clazz, JavaScriptObject dispMap,
      boolean isStatic) {
    impl.registerDispatchMap(clazz, dispMap, isStatic);
  }
  
  public static void exportAll() {
    GWT.create(ExportAll.class);
  }
  
  public static void exportAllAsync() {
    GWT.runAsync(new RunAsyncCallback() {
      public void onFailure(Throwable reason) {
        throw new RuntimeException(reason);
      }

      public void onSuccess() {
        GWT.create(ExportAll.class);
        onexport();
      }

      private native void onexport() /*-{
        $wnd.onexport();
      }-*/;
    });
  }
  
  private interface ExportAll extends Exportable {}
}
