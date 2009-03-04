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

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ExportableTypeOracle {

  private TypeOracle typeOracle;

  private TreeLogger log;

  static final String EXPORTER_CLASS = "org.timepedia.exporter.client.Exporter";

  static final String EXPORTABLE_CLASS
      = "org.timepedia.exporter.client.Exportable";

  static final String EXPORT_OVERLAY_CLASS
      = "org.timepedia.exporter.client.ExportOverlay";

  private JClassType exportableType = null;

  private JClassType jsoType = null;

  private JClassType stringType = null;

  public static final String JSO_CLASS
      = "com.google.gwt.core.client.JavaScriptObject";

  private static final String STRING_CLASS = "java.lang.String";

  private JClassType exportOverlayType;

  private Map<JClassType, JExportOverlayClassType> overlayTypes
      = new HashMap<JClassType, JExportOverlayClassType>();

  public ExportableTypeOracle(TypeOracle typeOracle, TreeLogger log) {
    this.typeOracle = typeOracle;
    this.log = log;
    exportableType = typeOracle.findType(EXPORTABLE_CLASS);
    exportOverlayType = typeOracle.findType(EXPORT_OVERLAY_CLASS);
    jsoType = typeOracle.findType(JSO_CLASS);
    stringType = typeOracle.findType(STRING_CLASS);
    assert exportableType != null;
    assert exportOverlayType != null;
    assert jsoType != null;
    assert stringType != null;

    for (JClassType t : typeOracle.getTypes()) {
      if (t.isAssignableTo(exportOverlayType) && !t.equals(exportOverlayType)) {
        JClassType targetType = getExportOverlayType(t);
        overlayTypes.put(targetType, new JExportOverlayClassType(this, t));
      }
    }
  }

  public JExportableClassType findExportableClassType(String requestedClass) {
    JClassType requestedType = typeOracle.findType(requestedClass);
    if (requestedType != null) {

      if (requestedType.isAssignableTo(exportOverlayType)) {
        return new JExportOverlayClassType(this, requestedType);
      } else if (requestedType.isAssignableTo(exportableType)) {
        return new JExportableClassType(this, requestedType);
      }
      JExportOverlayClassType exportOverlay = overlayTypes.get(requestedType);
      return exportOverlay;
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
      } else if (type.isArray() != null) {
        return new JExportableArrayType(this, type.isArray());
      } else if (overlayTypes.containsKey(cType)) {
        return overlayTypes.get(cType);
      } else if (cType.isAssignableTo(exportOverlayType)) {
        return new JExportOverlayClassType(this, type.isClassOrInterface());
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
    if (qualifiedSourceName == null) {
      return false;
    }
    JType type = typeOracle.findType(qualifiedSourceName);
    if (type == null) {
      return false;
    }
    JClassType cType = type.isClassOrInterface();

    if (cType != null && cType.isAssignableTo(exportableType)) {
      ExportClosure ann = cType.getAnnotation(ExportClosure.class);
      if (ann != null && cType.isInterface() != null) {

        return cType.getMethods().length == 1;
      }
    }
    return false;
  }

  public boolean isArray(JExportableClassType jExportableClassType) {
    return jExportableClassType.getType().isArray() != null;
  }

  public boolean isExportOverlay(JClassType i) {
    return i.isAssignableTo(exportOverlayType);
  }

  public JClassType getExportOverlayType(JClassType requestedType) {
    JClassType[] inf = requestedType.getImplementedInterfaces();
    for (JClassType i : inf) {
      if (isExportOverlay(i)) {
        return i.isParameterized().getTypeArgs()[0];
      }
    }
    return null;
  }
}
