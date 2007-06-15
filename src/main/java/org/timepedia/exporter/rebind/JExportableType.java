package org.timepedia.exporter.rebind;

/**
 * Created by IntelliJ IDEA.
 * User: ray
 * Date: Jun 12, 2007
 * Time: 4:43:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JExportableType {
    boolean needsExport();

    String getQualifiedSourceName();
}
