package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JAbstractMethod;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.core.ext.typeinfo.TypeOracleException;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.NoExport;

/**
 *
 */
public class ExportableTypeOracle {

  private TypeOracle typeOracle;

  private TreeLogger log;

  static final String EXPORTER_CLASS = "org.timepedia.exporter.client.Exporter";

  static final String EXPORTABLE_CLASS
      = "org.timepedia.exporter.client.Exportable";

  private JClassType exportableType = null;

  private JClassType jsoType = null;

  private JClassType stringType = null;

  

  public static final String JSO_CLASS
      = "com.google.gwt.core.client.JavaScriptObject";

  private static final String STRING_CLASS = "java.lang.String";


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
    return field.isStatic() && field.isPublic() && field.isFinal() && (
        isExportable(field.getAnnotation(Export.class)) || (
            isExportable(field.getEnclosingType()) && !isNotExportable(
                field.getAnnotation(NoExport.class))));
  }

  private static boolean isNotExportable(NoExport annotation) {
    return annotation != null;
  }

  public static boolean isExportable(JClassType type) {
    return isExportable(type.getAnnotation(Export.class)) || (
        type.isInterface() != null && isExportable(
            type.getAnnotation(ExportClosure.class)));
  }

  private static boolean isExportable(ExportClosure annotation) {
    return annotation != null;
  }

  public static boolean isExportable(Export annotation) {
    return annotation != null;
  }

  public static boolean isExportable(JAbstractMethod method) {
    return (isExportable(method.getEnclosingType()) && method.isPublic()
        && !isNotExportable(method.getAnnotation(NoExport.class)))
        || (isExportable(method.getAnnotation(Export.class)));
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
      } else if (cType != null && (cType.isAssignableTo(exportableType)
          || cType.isAssignableTo(stringType) || cType
          .isAssignableTo(jsoType))) {
        return new JExportableClassType(this, type.isClassOrInterface());
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

    JExportableClassType exportable = findExportableClassType(
        type.getQualifiedSourceName());
    return exportable != null && exportable.needsExport() ? exportable
        : findFirstExportableSuperClassType(type.getSuperclass());
  }

  public boolean isClosure(String qualifiedSourceName) {
    JType type = typeOracle.findType(qualifiedSourceName);
    JClassType cType = type.isClassOrInterface();

    if (cType != null && cType.isAssignableTo(exportableType)) {
      ExportClosure ann = cType.getAnnotation(ExportClosure.class);
      if (ann != null 
          && cType.isInterface() != null) {

        return cType.getMethods().length == 1;
      }
    }
    return false;
  }
}
