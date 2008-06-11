package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

import java.io.PrintWriter;
import java.util.*;

public class ExporterGeneratorNoExport extends Generator {


    public String generate(TreeLogger logger, GeneratorContext ctx,
                           String requestedClass)
            throws UnableToCompleteException {
        ClassExporter classExporter = new ClassExporter(logger, ctx);
        String generatedClass = classExporter.exportClass(requestedClass, false);
        return generatedClass;

    }
}