package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JParameter;

/**
 * 
 */
public class JExportableParameter {
    private JParameter param;
    private JExportableClassType exportableEnclosingType;

    public JExportableParameter(JExportableMethod exportableMethod,
                                JParameter param) {

        this.param = param;
        this.exportableEnclosingType =
                exportableMethod.getEnclosingExportType();
    }

    public String getTypeName() {
        return param.getType().getQualifiedSourceName();
    }

    public String getExportParameterValue(String argName) {
        ExportableTypeOracle xTypeOracle =
                exportableEnclosingType.getExportableTypeOracle();

        String paramTypeName = param.getType().getQualifiedSourceName();

        JExportableType type = xTypeOracle.findExportableType(paramTypeName);

        if (type != null && type.needsExport()) {
            JExportableClassType cType = (JExportableClassType) type;
            if (exportableEnclosingType.getExportableTypeOracle().isClosure(
                    type.getQualifiedSourceName())) {
                String value = "("+argName+".constructor == $wnd."+
                                   cType.getJSQualifiedExportName()+" ? " +
                                 argName+".instance : "+
                                "("+argName+".@java.lang.Object"+
                        "::hashCode() ? " + argName + " : @" +
                        cType.getQualifiedExporterImplementationName() + "::" +
                        "makeClosure(Lcom/google/gwt/core/client/JavaScriptObject;)("+
                        argName+")))";
                return value;
            } else {
                return argName + ".instance";
            }
        }
        return argName;
    }

    public String toString() {
        return param.getType().getSimpleSourceName();
    }

    public JExportableType getExportableType() {
        return exportableEnclosingType.getExportableTypeOracle()
                .findExportableClassType(getTypeName());
    }
}
