package org.timepedia.exporter.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;

/**
 *
 */
public class ExporterBaseActual extends ExporterBaseImpl {

  private static HashMap typeMap = new HashMap();
  public static final String WRAPPER_PROPERTY="__gwtex_wrap";
  public void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
    addTypeMap(GWT.getTypeName(type), exportedConstructor);
  }

  public void addTypeMap(String type,
      JavaScriptObject exportedConstructor) {
    typeMap.put(type, exportedConstructor);
  }
         
  public JavaScriptObject typeConstructor(Exportable type) {
    return typeConstructor(GWT.getTypeName(type));
  }

  public JavaScriptObject typeConstructor(String type) {
    Object o = typeMap.get(type);
    return (JavaScriptObject) o;
  }

  public JavaScriptObject wrap(Exportable type) {
      return wrap0(type, typeConstructor(type), WRAPPER_PROPERTY);
  }

  private native static JavaScriptObject wrap0(Exportable type,
					       JavaScriptObject constructor, String wrapProp) /*-{
           var wrap=type[wrapProp] || new (constructor)(type);
           type[wrapProp]=wrap;
           return wrap;
      }-*/;
}
