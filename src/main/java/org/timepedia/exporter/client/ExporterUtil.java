package org.timepedia.exporter.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

import java.util.HashMap;

/**
 * Holds utility methods and wrapper state
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public class ExporterUtil {
    private static ExporterBaseImpl impl=GWT.create(ExporterBaseImpl.class);
  
    private static HashMap typeMap=new HashMap();

    public static void addTypeMap(Exportable type,
                                  JavaScriptObject exportedConstructor) {
        impl.addTypeMap(type, exportedConstructor);
    }

    public static void addTypeMap(String type,
                                  JavaScriptObject exportedConstructor) {
        impl.addTypeMap(type, exportedConstructor);
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
   
}
