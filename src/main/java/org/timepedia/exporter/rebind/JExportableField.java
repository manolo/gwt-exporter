package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JField;

/**
 *
 */
public class JExportableField {
    private JExportableClassType enclosingExportType;
    private JField field;
    private String exportName;

    public JExportableField(JExportableClassType enclosingExportType,
                            JField field) {
        this.enclosingExportType = enclosingExportType;
        this.field = field;
        String metaData[][] =
                field.getMetaData(ExportableTypeOracle.GWT_EXPORT_MD);

        if(metaData != null && metaData.length > 0 && metaData[0] != null &&
                metaData[0].length > 0) {
            exportName = metaData[0][0];
        } else {
            exportName = field.getName();
        }

    }

    public String getJSExportName() {
        return exportName;
    }

    public String getJSQualifiedExportName() {
        return enclosingExportType.getJSQualifiedExportName()+ "." + getJSExportName();
    }

    public String getJSNIReference() {
        return field.getEnclosingType().getQualifiedSourceName()+"::"+field.getName();
    }
}
