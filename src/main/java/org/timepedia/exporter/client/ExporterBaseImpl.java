package org.timepedia.exporter.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;

/**
 * No-op implementation when export is disabled.
 */
public class ExporterBaseImpl {

  public void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
  }

  public void addTypeMap(Class type, JavaScriptObject exportedConstructor) {
  }

  public void setWrapper(Object instance, JavaScriptObject wrapper) {
  }

  public JavaScriptObject typeConstructor(Exportable type) {
    return null;
  }

  public JavaScriptObject typeConstructor(String type) {
    return null;
  }

  public JavaScriptObject wrap(Exportable type) {
    return null;
  }

  public JavaScriptObject wrap(Exportable[] type) {
    return null;
  }

  public JavaScriptObject wrap(JavaScriptObject[] type) {
    return null;
  }

  public JavaScriptObject wrap(double[] type) {
    return null;
  }

  public JavaScriptObject wrap(float[] type) {
    return null;
  }

  public JavaScriptObject wrap(int[] type) {
    return null;
  }

  public JavaScriptObject wrap(char[] type) {
    return null;
  }

  public JavaScriptObject wrap(byte[] type) {
    return null;
  }

  public JavaScriptObject wrap(long[] type) {
    return null;
  }

  public JavaScriptObject wrap(short[] type) {
    return null;
  }

  public void declarePackage(String packageName, String enclosingClasses) {
  }

  public JavaScriptObject getDispatch(Class clazz, String meth,
      JsArray<JavaScriptObject> arguments, boolean isStatic) {
    return null;
  }

  public void registerDispatchMap(Class clazz, JavaScriptObject dispMap, boolean isStatic) {
  }

  public JavaScriptObject wrap(String[] type) {
    return null;
  }

  public JavaScriptObject wrap(Date[] type) {
    return null;
  }

  public String[] toArrString(JsArrayString type) {
    return null;
  }

  public double[] toArrDouble(JsArrayNumber type) {
    return null;
  }

  public float[] toArrFloat(JsArrayNumber type) {
    return null;
  }

  public int[] toArrInt(JsArrayNumber type) {
    return null;
  }

  public byte[] toArrByte(JsArrayNumber type) {
    return null;
  }

  public char[] toArrChar(JsArrayNumber type) {
    return null;
  }

  public long[] toArrLong(JsArrayNumber type) {
    return null;
  }

  public Object[] toArrObject(JavaScriptObject type) {
    return null;
  }

  public Exportable[] toArrExport(JavaScriptObject p) {
    return null;
  }
  
  public Date[] toArrDate(JavaScriptObject j) {
    return null;
  }

  public Object[] toArrJsObject(JavaScriptObject type) {
    return null;
  }
  
  public JavaScriptObject computeVarArguments(int len, JavaScriptObject args) {
    return null;
  }
  
  public JavaScriptObject unshift(Object o, JavaScriptObject arr){
    return null;
  }

  public JavaScriptObject dateToJsDate(Date d) {
    return null;
  }

  public Date jsDateToDate(JavaScriptObject jd) {
    return null;
  }

}

