package org.timepedia.exporter.doclet;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;
import com.sun.tools.doclets.formats.html.ConfigurationImpl;
import com.sun.tools.doclets.formats.html.HtmlDoclet;
import com.sun.tools.doclets.formats.html.HtmlDocletWriter;
import com.sun.tools.doclets.internal.toolkit.util.ClassTree;
import com.sun.tools.doclets.internal.toolkit.util.DocletConstants;
import com.sun.tools.doclets.internal.toolkit.util.PackageListWriter;
import com.sun.tools.doclets.internal.toolkit.util.Util;

import java.io.File;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Generates Js and Gss docs.
 */
public class JsDoclet extends HtmlDoclet {

  public static boolean start(RootDoc rootDoc) {
    JsDoclet jsDoclet = new JsDoclet();

    try {
      return jsDoclet.startGeneration3(rootDoc);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean startGeneration3(RootDoc root) throws Exception {
    configuration = ConfigurationImpl.getInstance();
    configuration.root = root;

    if (root.classes().length == 0) {
      configuration.message.
          error("doclet.No_Public_Classes_To_Document");
      return false;
    }
    configuration.setOptions();
    configuration.getDocletSpecificMsg().notice("doclet.build_version",
        configuration.getDocletSpecificBuildDate());
    ClassTree classtree = new ClassTree(configuration,
        configuration.nodeprecated);

    generateClassFiles(root, classtree);
    if (configuration.sourcepath != null
        && configuration.sourcepath.length() > 0) {
      StringTokenizer pathTokens = new StringTokenizer(configuration.sourcepath,
          String.valueOf(File.pathSeparatorChar));
      boolean first = true;
      while (pathTokens.hasMoreTokens()) {
        Util.copyDocFiles(configuration,
            pathTokens.nextToken() + File.separator,
            DocletConstants.DOC_FILES_DIR_NAME, first);
        first = false;
      }
    }

    PackageListWriter.generate(configuration);
    generatePackageFiles(classtree);

    generateOtherFiles(root, classtree);
    configuration.tagletManager.printReport();
    return true;
  }

  @Override
  protected void generateOtherFiles(RootDoc rootDoc, ClassTree classTree)
      throws Exception {
    super.generateOtherFiles(rootDoc, classTree);
    HtmlDocletWriter writer = new HtmlDocletWriter(configuration, "jsdoc.html");

    writer.html();
    writer.head();
    writer.println(getCSS());
    writer.headEnd();
    writer.body("white", true);

    writer.h1("Exported JavaScript-API: Index of Classes");
    writer.ul();
    
    ClassDoc[] classes = rootDoc.classes();
    Arrays.sort(classes);
    for (ClassDoc clz : classes) {
      if (isExportable(clz) && hasMethods(clz) && clz.methods().length > 0
          && !isExportedClosure(clz.methods()[0])) {
        String packageName = getExportedPackage(clz);
        String className = getExportedName(clz, false);
        writer.li();
        writer.println(packageName + "." + "<a href=#" + packageName + "."
            + className + "><b>" + className + "</b></a>");
      }
    }
    writer.ulEnd();
    
    for (ClassDoc clz : classes) {
      if (isExportable(clz) && hasMethods(clz) && clz.methods().length > 0
          && !isExportedClosure(clz.methods()[0])) {
        String className = getExportedPackage(clz) + "." + getExportedName(clz, false);
        writer.h2("<div id=" + className + ">"+ className + "</div>");
        writer.println("<div class=jsdocText>" + filter(clz.commentText()) + "</div>");
        writer.table(1, "100%", 0, 0);

        boolean firstcon = true;
        for (ConstructorDoc cd : clz.constructors()) {
          if (isExportable(cd)) {
            if (firstcon) {
              writer.tr();
              writer.tdColspanBgcolorStyle(2, "", "jsdocHeader");
              writer.print("Constructors");
              firstcon = false;
              writer.tdEnd();
              writer.trEnd();
            }
            writer.tr();
            writer.tdVAlignClass("top", "jsdocRetType");
            writer.print("&nbsp");
            writer.tdEnd();
            writer.tdVAlignClass("top", "jsdocMethod");
            writer.print("<span class=jsdocMethodName>" + cd.name() + "</span>(");
            writeParameters(writer, cd.parameters());
            writer.print(")");
            writer.br();
            writer.print("<span class=jsdocComment>"
                + filter(cd.commentText()) + "</span>");

            writer.tdEnd();
            writer.trEnd();
          }
        }
        
        firstcon = true;
        for (MethodDoc cd : clz.methods()) {
          if (isExportable(cd)) {
            if (firstcon) {
              writer.tr();
              writer.tdColspanBgcolorStyle(2, "", "jsdocHeader");
              writer.print("Methods");
              firstcon = false;
              writer.tdEnd();
              writer.trEnd();
            }
            writer.tr();
            writer.tdVAlignClass("top", "jsdocRetType");
            writer.print(getExportedName(cd.returnType(), true));

            writer.tdEnd();
            writer.tdVAlignClass("top", "jsdocMethod");
            writer.print(
                "<b class=jsdocMethodName>" + getExportedName(cd) + "</b>"
                    + "(");
            writeParameters(writer, cd.parameters());
            writer.print(")");
            writer.br();
            writer.print("<span class=jsdocComment>"
                + filter(cd.commentText()) + "</span>");
            writer.tdEnd();
            writer.trEnd();
          }
        }

        writer.tableEnd();
        writer.br();
        writer.hr();
      }
    }
    writer.bodyEnd();
    writer.htmlEnd();
    writer.flush();
    writer.close();
  }

  private boolean hasMethods(ClassDoc clz) {
    int countExportedMethods = 0;
    for (ConstructorDoc cd : clz.constructors()) {
      if (isExportable(cd)) {
        countExportedMethods++;
      }
    }
    for (MethodDoc md : clz.methods()) {
      if (isExportable(md)) {
        countExportedMethods++;
      }
    }
    return countExportedMethods > 0;
  }
  
  private String getExportedName(MethodDoc cd) {
    String ename = cd.name();
    for (AnnotationDesc a : cd.annotations()) {
      if (a.annotationType().name().equals("Export")) {
        for (AnnotationDesc.ElementValuePair p : a.elementValues()) {
          ename = p.value().toString();
          break;
        }
      }
    }
    return ename.replaceAll("\"", "");
  }

  protected String filter(String s) {
    if (s.startsWith("Created")) {
      return "";
    }
    s = s.replaceAll("(?s)\\{@link\\s[^\\}]*?#(.+)\\}", "$1");
    s = s.replaceAll("(?s)\\{@link\\s[^\\}]*?([^\\.\\}]+)\\}", "<a href=#$1>$1</a>");
    return s;
  }

  private String getExportedName(Type clz, boolean link) {
    return (clz.isPrimitive() ? "void".equals(clz.typeName()) ? "&nbsp;" 
        : clz.typeName() : getExportedName(clz.asClassDoc(), link)) + clz.dimension();
  }

  private void writeParameters(HtmlDocletWriter writer, Parameter[] ps) {
    writer.print(getParameterString(ps));
  }

  private boolean isExportable(ConstructorDoc cd) {
    boolean export = isExported(cd.containingClass());
    for (AnnotationDesc a : cd.annotations()) {
      if (a.annotationType().name().equals("Export")) {
        export = true;
      }
      if (a.annotationType().name().equals("NoExport")) {
        export = false;
      }
    }
    return export;
  }

  private boolean isExportable(MethodDoc cd) {
    boolean export = isExported(cd.containingClass());
    for (AnnotationDesc a : cd.annotations()) {
      if (a.annotationType().name().equals("Export")) {
        export = true;
      }
      if (a.annotationType().name().equals("NoExport")) {
        export = false;
      }
    }
    return export;
  }

  private boolean isExportedClosure(MethodDoc md) {
    ClassDoc clz = md.containingClass();
    for (AnnotationDesc a : clz.annotations()) {

      String aname = a.annotationType().name();
      if (aname.equals("ExportClosure")) {
        return true;
      }
    }
    return false;
  }

  private boolean isExported(ClassDoc clz) {
    for (AnnotationDesc a : clz.annotations()) {
      String aname = a.annotationType().name();
      if (aname.equals("Export") || aname.equals("ExportClosure")) {
        return true;
      }
    }
    return false;
  }

  private String getExportedName(ClassDoc clz, boolean link) {
    if (clz == null) {
      return "";
    }

    PackageDoc cpkg = clz.containingPackage();
    String pkg = cpkg == null ? "" : (cpkg.name() + ".");
    String name = clz.name();
    
    boolean isClosure = false;

    for (AnnotationDesc a : clz.annotations()) {
      if (a.annotationType().name().equals("ExportPackage")) {
        for (AnnotationDesc.ElementValuePair p : a.elementValues()) {
          pkg = p.value().toString();
          break;
        }
      }
      if (a.annotationType().name().equals("Export")) {
        for (AnnotationDesc.ElementValuePair p : a.elementValues()) {
          name = p.value().toString();
          break;
        }
      }
      if (a.annotationType().name().equals("ExportClosure")) {
        isClosure = true;
        name = "<i class=jsdocClosureFunc>function</i>(";
        name += getParameterString(clz.methods()[0].parameters());
        name += ")";
        pkg = "";
      }
    }
    pkg = pkg.replaceAll("\"", "").replaceFirst("\\.+$", "");
    if (link && !isClosure && !"String".equals(name)) {
      name = "<a href=#" + pkg + "." + name + ">" + name + "</a>";  
    }
    return name;
  }
  
  private String getParameterString(Parameter[] ps) {
    String result = "";
    for (int i = 0; i < ps.length; i++) {
      Type type = ps[i].type();
      String ename = getExportedName(type, true);
      String pname =  ename.contains("function") ? "{}" : ps[i].name();
      result += "<span class=jsdocParameterType>" + ename
          + "</span> <span class=jsdocParameterName>" + pname
          + "</span>";
      if (i < ps.length - 1) {
        result += ", ";
      }
    }
    return result;
  }

  private String getExportedPackage(ClassDoc clz) {
    if (clz == null) {
      return "";
    }

    PackageDoc cpkg = clz.containingPackage();
    String pkg = cpkg == null ? "" : (cpkg.name());

    for (AnnotationDesc a : clz.annotations()) {
      if (a.annotationType().name().equals("ExportPackage")) {
        for (AnnotationDesc.ElementValuePair p : a.elementValues()) {
          pkg = p.value().toString();
          break;
        }
      }
    }
    pkg = pkg.replaceAll("\"", "");
    return pkg;
  }

  private static boolean isExportable(ClassDoc clz) {
    for (ClassDoc i : clz.interfaces()) {
      if (i.name().contains("Exportable")) {
        return true;
      }
    }
    return false;
  }
  
  public String getCSS() {
    return "<style>"
    + "  body,table {"
    + "    background-color: #f3ead8;"
    + "    color: #000000;"
    + "    font-family: Arial, Helvetica, sans-serif;"
    + "    font-size: 14px;"
    + "  }"
    + "  h1,h2 {"
    + "    font-family: Trebuchet MS;"
    + "    color: navy;"
    + "    font-size: 18pt;"
    + "    border-bottom: double 3px;"
    + "    padding-top: 20px;"
    + "    padding-left: 20px;"
    + "  }"
    + "  table {"
    + "    width: 100%;"
    + "    border: 1px;"
    + "  }"
    + "  td.jsdocRetType {"
    + "    width: 200px;"
    + "  }"
    + "  div.jsdocText {"
    + "    padding-bottom: 20px;"
    + "  }"
    + "  .jsdocHeader {"
    + "    background-color:  #E8F1F6;"
    + "    padding: 5px;"
    + "    font-weight: bold;"
    + "  }"
    + "  td {"
    + "    padding-left: 5px;"
    + "    padding-right: 10px;"
    + "  }"
    + "  .jsdocMethodName {"
    + "    font-weight: bold;"
    + "  }"
    + "  .jsdocComment,.jsdocComment p {"
    + "    font-size: 11px;"
    + "    color: blue;"
    + "    padding-left: 15px;"
    + "  }"
    + "  a {"
    + "    color: #331166;"
    + "    text-decoration: none;"
    + "  }"
    + "  a:hover {"
    + "    color: #666666;"
    + "    text-decoration: underline;"
    + "  }"
    + "</style>";
  }
  
}
