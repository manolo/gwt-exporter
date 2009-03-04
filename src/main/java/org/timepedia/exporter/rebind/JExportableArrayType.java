package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JArrayType;

/**
 *
 */
public class JExportableArrayType extends JExportableClassType
    implements JExportableType {

  public JExportableArrayType(ExportableTypeOracle exportableTypeOracle,
      JArrayType array) {
    super(exportableTypeOracle, array);
    this.exportableTypeOracle = exportableTypeOracle;
    this.array = array;
  }

  private ExportableTypeOracle exportableTypeOracle;

  private JArrayType array;

  public boolean needsExport() {
    return true;
  }

  public String getQualifiedSourceName() {
    return array.getQualifiedSourceName();
  }

  public JExportableType getComponentType() {
    return exportableTypeOracle
        .findExportableType(array.getComponentType().getQualifiedSourceName());
  }
}
