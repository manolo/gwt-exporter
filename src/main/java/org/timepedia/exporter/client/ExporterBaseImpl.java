package org.timepedia.exporter.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;

/**
 *
 */
public class ExporterBaseImpl {
  

    public static void addTypeMap(Exportable type,
                                  JavaScriptObject exportedConstructor) {
    }

    public static void addTypeMap(String type,
                                  JavaScriptObject exportedConstructor) {
    }

    public static JavaScriptObject typeConstructor(Exportable type) {
        return null;
    }

    public static JavaScriptObject typeConstructor(String type) {
        return null;
    }

    public static JavaScriptObject wrap(Exportable type) {
        return null;
    }

   
}
