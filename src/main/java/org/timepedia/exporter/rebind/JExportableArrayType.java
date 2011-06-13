package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JType;

import org.timepedia.exporter.client.ExporterUtil;

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

  @Override
  public String getWrapperFunc() {
    JType type = array.getComponentType();
    return "@" + ExporterUtil.class.getName() + "::wrap(["
        + type.getJNISignature() + ")";
  }
  
  public String getToArrayFunc(String argName) {
    String ret = "@org.timepedia.exporter.client.ExporterUtil::";
    String qsn = array.getQualifiedSourceName();
    if (qsn.equals("java.lang.String[]")) {
      ret += "toArrString(Lcom/google/gwt/core/client/JsArrayString;)" ;
    } else if (qsn.equals("double[]")) {
      ret += "toArrDouble(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.equals("float[]")) {
      ret += "toArrFloat(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.equals("long[]")) {
      ret += "toArrLong(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.equals("int[]")) {
      ret += "toArrInt(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.equals("byte[]")) {
      ret += "toArrByte(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.equals("char[]")) {
      ret += "toArrChar(Lcom/google/gwt/core/client/JsArrayNumber;)" ;
    } else if (qsn.endsWith("Object[]")) {
      ret += "toArrObject(Lcom/google/gwt/core/client/JavaScriptObject;)" ;
    } else {
      ret += "toArrExport(Lcom/google/gwt/core/client/JavaScriptObject;)" ;
    }
    return ret + "(" + argName + ")";
  }

  public JExportableType getComponentType() {
    return exportableTypeOracle
        .findExportableType(array.getComponentType().getQualifiedSourceName());
  }
}
