package org.timepedia.exporter.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;

/**
 * Holds utility methods and wrapper state
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public class ExporterBase {
    private static HashMap typeMap=new HashMap();

    public static void addTypeMap(Exportable type,
                                  JavaScriptObject exportedConstructor) {
        addTypeMap(GWT.getTypeName(type), exportedConstructor);
    }

    public static void addTypeMap(String type,
                                  JavaScriptObject exportedConstructor) {
        typeMap.put(type, exportedConstructor);
    }

    public static JavaScriptObject typeConstructor(Exportable type) {
        return typeConstructor(GWT.getTypeName(type));
    }

    public static JavaScriptObject typeConstructor(String type) {
        Object o =typeMap.get(type);
        return (JavaScriptObject) o;
    }

    public static JavaScriptObject wrap(Exportable type) {
        return wrap0(type, typeConstructor(type));
    }

    private native static JavaScriptObject wrap0(Exportable type,
                                          JavaScriptObject constructor) /*-{
         return new (constructor)(type);
    }-*/;
}
