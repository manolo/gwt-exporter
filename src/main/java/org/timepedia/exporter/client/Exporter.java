package org.timepedia.exporter.client;


/**
 * Exportable classes passed to GWT.create() will return an implementation of
 * Exporter. Invoke the export() method to export the JavaScript bridge
 * classes and methods.
 *
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 */
public interface Exporter {
    void export();
}
