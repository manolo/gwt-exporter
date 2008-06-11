package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;

import org.timepedia.exporter.client.ExportPackage;

import java.util.ArrayList;

/**
 *
 */
public class JExportableClassType implements JExportable, JExportableType {

  private ExportableTypeOracle exportableTypeOracle;

  private JClassType type;

  private static final String IMPL_EXTENSION = "ExporterImpl";

  public JExportableClassType(ExportableTypeOracle exportableTypeOracle,
      JClassType type) {
    this.exportableTypeOracle = exportableTypeOracle;

    this.type = type;
  }

  public String getExporterImplementationName() {
    return type.getSimpleSourceName() + IMPL_EXTENSION;
  }

  public String getPackageName() {
    return type.getPackage().getName();
  }

  public String getQualifiedExporterImplementationName() {
    return getPackageName() + "." + getExporterImplementationName();
  }

  public String getJSExportPackage() {
    String requestedPackageName = getPrefix();
    ExportPackage ann = type.getAnnotation(ExportPackage.class);
    if (ann != null) {
      requestedPackageName = ann.value();
    } else if (type.getEnclosingType() != null) {
      JExportableClassType encType = exportableTypeOracle
          .findExportableClassType(
              type.getEnclosingType().getQualifiedSourceName());
      if(encType != null) return encType.getJSExportPackage();
    }
    return requestedPackageName;
  }

  public JExportableField[] getExportableFields() {
    ArrayList<JExportableField> exportableFields
        = new ArrayList<JExportableField>();

    for (JField field : type.getFields()) {
      if (ExportableTypeOracle.isExportable(field)) {
        exportableFields.add(new JExportableField(this, field));
      }
    }
    return exportableFields.toArray(new JExportableField[0]);
  }

  public String getPrefix() {
    String prefix = "";
    boolean firstClientPackage = true;
    for (String pkg : type.getPackage().getName().split("\\.")) {
      if (firstClientPackage && pkg.equals("client")) {
        firstClientPackage = false;
        continue;
      }
      prefix += pkg;
      prefix += '.';
    }
    // remove trailing .
    return prefix.substring(0, prefix.length() - 1);
  }

  public JExportableConstructor[] getExportableConstructors() {
    ArrayList<JExportableConstructor> exportableCons
        = new ArrayList<JExportableConstructor>();

    for (JConstructor method : type.getConstructors()) {
      if (method.isConstructor() == null) {
        continue;
      }

      if (ExportableTypeOracle.isExportable(method)) {
        exportableCons.add(new JExportableConstructor(this, method));
      }
    }
    return exportableCons.toArray(new JExportableConstructor[0]);
  }

  public JExportableMethod[] getExportableMethods() {
    ArrayList<JExportableMethod> exportableMethods
        = new ArrayList<JExportableMethod>();

    for (JMethod method : type.getMethods()) {
      if (method.isConstructor() != null) {
        continue;
      }

      if (ExportableTypeOracle.isExportable(method)) {
        exportableMethods.add(new JExportableMethod(this, method));
      }
    }
    return exportableMethods.toArray(new JExportableMethod[0]);
  }

  public JExportableClassType getExportableSuperClassType() {
    return exportableTypeOracle
        .findFirstExportableSuperClassType(type.getSuperclass());
  }

  public String getQualifiedSourceName() {
    return type.getQualifiedSourceName();
  }

  public String getJSQualifiedExportName() {
    return getJSConstructor();
  }

  public String getJSNIReference() {
    return type.getQualifiedSourceName();
  }

  public boolean needsExport() {
    return !isPrimitive() && !isTransparentType();
  }

  public boolean isPrimitive() {
    return type.isPrimitive() != null;
  }

  public String getJSConstructor() {
    return getJSExportPackage() + "." + type.getName();
  }

  public ExportableTypeOracle getExportableTypeOracle() {
    return exportableTypeOracle;
  }

  public boolean isTransparentType() {
    return exportableTypeOracle.isJavaScriptObject(this) || exportableTypeOracle
        .isString(this);
  }

  public JClassType getType() {
    return type;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JExportableClassType that = (JExportableClassType) o;

    return getQualifiedSourceName().equals(that.getQualifiedSourceName());
  }

  public int hashCode() {
    return getQualifiedSourceName().hashCode();
  }

  public String[] getEnclosingClasses() {
    String[] enc = type.getName().split("\\.");
    String[] enclosingTypes = new String[enc.length - 1];
    if (enc.length > 1) {
      System.arraycopy(enc, 0, enclosingTypes, 0, enclosingTypes.length);
    }
    return enclosingTypes;
  }
}
