package org.timepedia.exporter.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.HashMap;
import java.util.IdentityHashMap;

/**
 *
 */
public class ExporterBaseActual extends ExporterBaseImpl {

  public static final String WRAPPER_PROPERTY = "__gwtex_wrap";

  private native static JavaScriptObject wrap0(Exportable type,
      JavaScriptObject constructor, String wrapProp) /*-{
           return new (constructor)(type);
      }-*/;

  private HashMap typeMap = new HashMap();

  //TODO: track garbage collected wrappers and remove mapping
  private IdentityHashMap<Object, JavaScriptObject> wrapperMap = null;

  public ExporterBaseActual() {
    if (!GWT.isScript()) {
      wrapperMap = new IdentityHashMap<Object, JavaScriptObject>();
    }
  }

  public void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
    addTypeMap(GWT.getTypeName(type), exportedConstructor);
  }

  public void addTypeMap(String type, JavaScriptObject exportedConstructor) {
    typeMap.put(type, exportedConstructor);
  }

  public void setWrapper(Object instance, JavaScriptObject wrapper) {
    if (GWT.isScript()) {
      setWrapperJS(instance, wrapper, WRAPPER_PROPERTY);
    } else {
      setWrapperHosted(instance, wrapper);
    }
  }

  public JavaScriptObject typeConstructor(Exportable type) {
    return typeConstructor(GWT.getTypeName(type));
  }

  public JavaScriptObject typeConstructor(String type) {
    Object o = typeMap.get(type);
    return (JavaScriptObject) o;
  }

  public JavaScriptObject wrap(Exportable type) {
    if (type == null) {
      return null;
    }

    if (!GWT.isScript()) {
      JavaScriptObject wrapper = wrapperMap.get(type);
      if (wrapper != null) {
        return wrapper;
      }
    } else {
      JavaScriptObject wrapper = getWrapperJS(type, WRAPPER_PROPERTY);
      if (wrapper != null) {
        return wrapper;
      }
    }
    JavaScriptObject wrapper = wrap0(type, typeConstructor(type),
        WRAPPER_PROPERTY);
    setWrapper(type, wrapper);
    return wrapper;
  }

  public JavaScriptObject wrap(Exportable[] type) {
    if (type == null) {
      return null;
    }
    if (!GWT.isScript()) {
      JavaScriptObject wrapper = wrapperMap.get(type);
      if (wrapper != null) {
        return wrapper;
      }
    } else {
      JavaScriptObject wrapper = getWrapperJS(type, WRAPPER_PROPERTY);
      if (wrapper != null) {
        return wrapper;
      }
    }
    JavaScriptObject wrapper = JavaScriptObject.createArray();
    JsArray<JavaScriptObject> wrapperArray = wrapper.cast();
    for (int i = 0; i < type.length; i++) {
      wrapperArray.set(i, wrap(type[i]));
    }
    setWrapper(type, wrapperArray);
    return wrapper;
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
}
