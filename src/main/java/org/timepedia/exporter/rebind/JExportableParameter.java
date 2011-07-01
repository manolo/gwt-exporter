package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 *
 */
public class JExportableParameter {

  private JParameter param;

  public JParameter getParam() {
    return param;
  }

  private JExportableClassType exportableEnclosingType;

  public JExportableClassType getExportableEnclosingType() {
    return exportableEnclosingType;
  }

  public JExportableParameter(JExportableMethod exportableMethod,
      JParameter param) {

    this.param = param;
    this.exportableEnclosingType = exportableMethod.getEnclosingExportType();
  }

  public String getTypeName() {
    return param.getType().getQualifiedSourceName();
  }
  
  public String getJNISignature() {
    return param.getType().getJNISignature();
  }

  public String getExportParameterValue(String argName) {
    ExportableTypeOracle xTypeOracle = exportableEnclosingType
        .getExportableTypeOracle();
    
    String ret = argName;

    String paramTypeName = param.getType().getQualifiedSourceName();

    JExportableType type = xTypeOracle.findExportableType(paramTypeName);
    
    if (type != null && type.needsExport()) {
      JExportableClassType cType = (JExportableClassType) type;
      if (exportableEnclosingType.getExportableTypeOracle()
          .isClosure(type.getQualifiedSourceName())) {
        ret = "(" + argName + ".constructor == $wnd."
            + cType.getJSQualifiedExportName() + " ? " + argName
            + ".__gwt_instance : " 
// typeMarker makes Hosted mode fail in gwt-2.2.0 and gwt-2.3.0
//            + "(" + argName + ".@java.lang.Object"
//            + "::typeMarker ? " + argName + " : "
            + "@" + cType.getQualifiedExporterImplementationName() + "::"
            + "makeClosure(Lcom/google/gwt/core/client/JavaScriptObject;)("
            + argName 
//            + ")"
            + "))";
//        value = "@" 
//        + cType.getQualifiedExporterImplementationName() + "::"
//        + "makeClosure(Lcom/google/gwt/core/client/JavaScriptObject;)("
//        + argName + ")";
      } else if (!(type instanceof JExportableArrayType)){
        ret += ".__gwt_instance";
      }
    }
    return ret;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JExportableParameter that = (JExportableParameter) o;
    return getJsTypeOf().equals(that.getJsTypeOf());
  }

  public String getJsTypeOf() {
    if (param == null) {
      return "null";
    } else if (param.getType().isArray() != null) {
      return "array";
    } else if (param.getType().isPrimitive() != null) {
      JPrimitiveType prim = param.getType().isPrimitive();
      return prim == JPrimitiveType.BOOLEAN ? "boolean" : "number";
    } else if (exportableEnclosingType.getExportableTypeOracle()
        .isString(param.getType())) {
      return "string";
    } else if (exportableEnclosingType.getExportableTypeOracle()
        .isJavaScriptObject(param.getType())) {
      return "object";
    } else {
      return "@"+param.getType().getQualifiedSourceName()+"::class";
    }
  }

  @Override
  public int hashCode() {
    return param != null ? getJsTypeOf().hashCode() : 0;
  }

  public String toString() {
    return param.getType().getSimpleSourceName();
  }

  public JExportableType getExportableType() {
    return exportableEnclosingType.getExportableTypeOracle()
        .findExportableClassType(getTypeName());
  }
  
  public String getToArrayFunc(String qsn, String argName) {
    String ret = "ExporterUtil.";
    if (qsn.equals("java.lang.String[]")) {
      ret += "toArrString" ;
    } else if (qsn.equals("double[]")) {
      ret += "toArrDouble" ;
    } else if (qsn.equals("float[]")) {
      ret += "toArrFloat" ;
    } else if (qsn.equals("long[]")) {
      ret += "toArrLong" ;
    } else if (qsn.equals("int[]")) {
      ret += "toArrInt" ;
    } else if (qsn.equals("byte[]")) {
      ret += "toArrByte" ;
    } else if (qsn.equals("char[]")) {
      ret += "toArrChar" ;
    } else if (qsn.endsWith("Object[]")) {
      ret += "toArrObject" ;
    } else {
      ret += "toArrExport" ;
    }
    return ret + "(" + argName + ")";
  }

}
