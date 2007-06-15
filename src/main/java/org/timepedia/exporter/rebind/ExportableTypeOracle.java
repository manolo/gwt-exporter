package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.core.ext.TreeLogger;


/**
 *
 */
public class ExportableTypeOracle {
    private TypeOracle typeOracle;
    private TreeLogger log;
    static final String EXPORTER_CLASS =
            "org.timepedia.exporter.client.Exporter";
    static final String EXPORTABLE_CLASS =
            "org.timepedia.exporter.client.Exportable";
    private JClassType exportableType = null;
    private JClassType jsoType = null;
    private JClassType stringType = null;

    public static final String GWT_EXPORT_MD = "gwt.export";
    private static final String GWT_NOEXPORT_MD = "gwt.noexport";
    public static final String JSO_CLASS =
            "com.google.gwt.core.client.JavaScriptObject";
    private static final String STRING_CLASS = "java.lang.String";
    private static final String GWT_EXPORTCLOSURE = "gwt.exportClosure";

    public ExportableTypeOracle(TypeOracle typeOracle, TreeLogger log) {
        this.typeOracle = typeOracle;
        this.log = log;
        exportableType = typeOracle.findType(EXPORTABLE_CLASS);
        jsoType = typeOracle.findType(JSO_CLASS);
        stringType = typeOracle.findType(STRING_CLASS);
        assert exportableType != null;
    }

    public JExportableClassType findExportableClassType(String requestedClass) {
        JClassType requestedType = typeOracle.findType(requestedClass);
        if (requestedType != null) {

            if (requestedType.isAssignableTo(exportableType)) {
                return new JExportableClassType(this, requestedType);
            }
        }
        return null;
    }

    public static boolean isExportable(JField field) {
        return field.isStatic() &&
                field.isPublic() &&
                field.isFinal() &&
                ( isExportable(field.getMetaData(GWT_EXPORT_MD)) ||
                        ( isExportable(field.getEnclosingType()) &&
                                !isNotExportable(
                                        field.getMetaData(
                                                GWT_NOEXPORT_MD
                                        )
                                ) )
                );
    }

    private static boolean isNotExportable(String[][] metaData) {

        return metaData != null && metaData.length != 0;
    }

    public static boolean isExportable(JClassType type) {
        String[][] metaData =
                type.getMetaData(GWT_EXPORT_MD);
        return isExportable(metaData) || (type.isInterface() != null &&
        isExportable(type.getMetaData(GWT_EXPORTCLOSURE)));
    }

    public static boolean isExportable(String[][] metaData) {
        return metaData != null && metaData.length != 0;
    }

    public static boolean isExportable(JAbstractMethod method) {
        return ( isExportable(method.getEnclosingType()) && method.isPublic() &&
                !isNotExportable(method.getMetaData(GWT_NOEXPORT_MD)) ) ||
                ( isExportable(method.getMetaData(GWT_EXPORT_MD)) );
    }

    public boolean isJavaScriptObject(JExportableClassType type) {
        return type.getType().isAssignableTo(jsoType);
    }

    public boolean isString(JExportableClassType type) {
        return type.getType().isAssignableTo(stringType);
    }

    public JExportableType findExportableType(String paramTypeName) {
        try {
            JType type = typeOracle.parse(paramTypeName);
            JClassType cType = type != null ? type.isClassOrInterface() : null;

            if (type.isPrimitive() != null) {
                return new JExportablePrimitiveType(this, type.isPrimitive());
            } else if (cType != null && (
                    cType.isAssignableTo(exportableType) ||
                            cType.isAssignableTo(stringType) ||
                            cType.isAssignableTo(jsoType) )) {
                return new JExportableClassType(
                        this, type.isClassOrInterface()
                );
            } else {
                return null;
            }

        } catch (TypeOracleException e) {
            return null;
        }

    }

    public JExportableClassType findFirstExportableSuperClassType(
            JClassType type) {

        if (type == null) {
            return null;
        }

        JExportableClassType exportable =
                findExportableClassType(type.getQualifiedSourceName());
        return exportable != null && exportable.needsExport() ? exportable :
                findFirstExportableSuperClassType(type.getSuperclass());

    }

    public boolean isClosure(String qualifiedSourceName) {
        JType type = typeOracle.findType(qualifiedSourceName);
        JClassType cType = type.isClassOrInterface();

        if (cType != null && cType.isAssignableTo(exportableType)) {
            String metadata[][] = cType.getMetaData(GWT_EXPORTCLOSURE);
            if (metadata != null && metadata.length != 0 &&
                    cType.isInterface() != null) {

                return cType.getMethods().length == 1;
            }
        }
        return false;

    }
}
