package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * 
 */
public class JExportablePrimitiveType implements JExportableType {
    private ExportableTypeOracle exportableTypeOracle;
    private JPrimitiveType primitive;

    public JExportablePrimitiveType(ExportableTypeOracle exportableTypeOracle,
                                    JPrimitiveType primitive) {
        this.exportableTypeOracle = exportableTypeOracle;
        this.primitive = primitive;
    }

    public boolean needsExport() {
        return false;
    }

    public String getQualifiedSourceName() {
        return primitive.getQualifiedSourceName();
    }
}
