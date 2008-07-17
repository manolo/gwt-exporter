package org.timepedia.exporter.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 */
public class ExporterBaseImpl {

  public void addTypeMap(Exportable type,
      JavaScriptObject exportedConstructor) {
  }

  public void addTypeMap(String type, JavaScriptObject exportedConstructor) {
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
}
