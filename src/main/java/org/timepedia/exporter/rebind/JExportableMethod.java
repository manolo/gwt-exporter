package org.timepedia.exporter.rebind;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportInstanceMethod;

import com.google.gwt.core.ext.typeinfo.JAbstractMethod;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;

/**
 *
 */
public class JExportableMethod implements JExportable {

  public static final String WRAPPER_PREFIX = "__static_wrapper_";

  protected JExportableClassType exportableEnclosingType;

  protected JAbstractMethod method;

  private String exportName;
  
  private String qualifiedExportName;
  
  JExportableParameter[] exportableParameters;

  public JExportableMethod(JExportableClassType exportableEnclosingType,
      JAbstractMethod method) {
    this.exportableEnclosingType = exportableEnclosingType;
    this.method = method;
    
    String anValue = "";
    if (isExportInstanceMethod()) {
      anValue = method.getAnnotation(ExportInstanceMethod.class).value();
    } else {
      Export ann = method.getAnnotation(Export.class);
      anValue = ann != null ? ann.value().trim() : "";
    }

    if (!anValue.isEmpty()) {
      exportName = anValue;
      
      // Static methods can be assigned to any name-space if it starts with $wnd
      if (isStatic() && anValue.startsWith("$wnd.")) {
        qualifiedExportName = anValue.replaceFirst("\\$wnd\\.", "");
      }
    } else {
      exportName = method.getName();
    }
    
    if (qualifiedExportName == null) {
      qualifiedExportName = getEnclosingExportType().getJSQualifiedExportName()
      + "." + getUnqualifiedExportName();
    }
    
    JParameter[] params = method.getParameters();
    exportableParameters = new JExportableParameter[params.length];
    for (int i = 0; i < params.length; i++) {
      exportableParameters[i] = new JExportableParameter(this, params[i]);
    }
  }

  public String getUnqualifiedExportName() {
    return exportName;
  }

  public String getJSQualifiedExportName() {
    return qualifiedExportName;
  }

  public JExportableType getExportableReturnType() {
    ExportableTypeOracle xTypeOracle = getExportableTypeOracle();
    String returnTypeName = ((JMethod) method).getReturnType()
        .getQualifiedSourceName();
    return xTypeOracle.findExportableType(returnTypeName);
  }

  public JExportableParameter[] getExportableParameters() {
    return exportableParameters;
  }

  public JExportableClassType getEnclosingExportType() {
    return exportableEnclosingType;
  }
  
  public String getEnclosingTypeQualifiecSourceName() {
    if (exportableEnclosingType instanceof JExportOverlayClassType) {
      return ((JExportOverlayClassType)exportableEnclosingType).getOverlayType().getQualifiedSourceName();
    } else {
      return exportableEnclosingType.getQualifiedSourceName();
    }
  }

  public String getJSNIReference() {
    String reference = "";
    // we export the original method or the static wrapper
    if (!needsWrapper()) {
      // Use static ExportConstructor and ExportMethod methods in the overlay class
      if (isStatic() && exportableEnclosingType instanceof JExportOverlayClassType
          && (method.getAnnotation(ExportConstructor.class) != null || method.getAnnotation(ExportInstanceMethod.class) != null)) {
        reference = getEnclosingTypeQualifiecSourceName();
      } else {
        reference = exportableEnclosingType.getQualifiedSourceName();
      }
      reference +=  "::" + method.getName() + "(";      
    } else {
      reference = exportableEnclosingType.getQualifiedExporterImplementationName() + "::" + WRAPPER_PREFIX + method.getName() + "(";
      // Wrappers are static, so we pass the instance in the first argument.
      if (!isStatic()) {
        reference += exportableEnclosingType.getTypeToExport().getJNISignature();
      }
    }
    
    int len = exportableParameters.length;
    for (int i = 0; i < len; i++) {
      String signature = exportableParameters[i].getJNISignature();
      // Here we replace 
      // - long by double signature
      // - arrays and date by javascriptobject
      if ("J".equals(signature)) {
        signature = "D";
      } else if (
          signature.startsWith("[")
          || signature.equals("Ljava/util/Date;")) {
        signature = "Lcom/google/gwt/core/client/JavaScriptObject;";
      }
      reference += signature;
    }
    
    reference += ")";
    return reference;
  }
  
  private Boolean wrap = null;

  // return true if this method needs a static wrapper. 
  // - methods which have a 'long' parameter or return 'long'
  // - methods which have array parameters
  // - methods with variable arguments
  public boolean needsWrapper() {
    if (wrap == null) {
      wrap = false;
      if (method.isVarArgs()) {
        wrap = true;
      } else if (isExportInstanceMethod()) {
        wrap = true;
      } else if (getExportableReturnType() != null &&
          "long".equals(getExportableReturnType().getQualifiedSourceName())) {
        wrap = true;
      } else for (JExportableParameter p : getExportableParameters()) {
        if (p.getTypeName().matches("(long|.*\\[\\])$|java.util.Date")) {
          wrap = true;
          break;
        }
      }
    }
    return wrap;
  }
  
  public boolean isInStaticMap() {
    return isStatic() && !isExportInstanceMethod();
  }

  public boolean isStatic() {
    if (method instanceof JConstructor) {
      return false;
    } else {
      return ((JMethod) method).isStatic();
    }
  }
  
  public boolean isExportInstanceMethod() {
    return isStatic()
        && exportableEnclosingType instanceof JExportOverlayClassType
        && method.getAnnotation(ExportInstanceMethod.class) != null
//        && ((JExportOverlayClassType) exportableEnclosingType).getOverlayType() == ((JMethod) method).getReturnType()
        ;
  }

  public boolean isExportConstructor() {
    return isStatic()
        && exportableEnclosingType instanceof JExportOverlayClassType
        && method.getAnnotation(ExportConstructor.class) != null
//        && ((JExportOverlayClassType) exportableEnclosingType).getOverlayType() == method.getParameters()[0].getType()
        ;
  }

  public ExportableTypeOracle getExportableTypeOracle() {
    return getEnclosingExportType().getExportableTypeOracle();
  }

  public String toString() {
    String str = exportableEnclosingType.getQualifiedSourceName() + "."
        + method.getName() + "(";
    JExportableParameter[] params = getExportableParameters();
    for (int i = 0; i < params.length; i++) {
      str += params[i];
      if (i < params.length - 1) {
        str += ", ";
      }
    }
    return str + ")";
  }

  public String getName() {
    return method.getName();
  }
  
  public boolean isVarArgs() {
    return method.isVarArgs();
  }
  
}
