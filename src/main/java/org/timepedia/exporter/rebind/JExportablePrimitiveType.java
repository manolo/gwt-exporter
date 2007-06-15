package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * Created by IntelliJ IDEA.
 * User: ray
 * Date: Jun 12, 2007
 * Time: 5:46:57 PM
 * To change this template use File | Settings | File Templates.
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
