package org.timepedia.exporter.rebind;

/**
 *
 */
public interface JExportableType {
    boolean needsExport();

    String getQualifiedSourceName();
}
