package exporterdemo.client;

import org.timepedia.exporter.client.Exportable;

/**
 * Example of closure exporting, gwt.export says export the class methods, 
 * and gwt.exportClosure says auto-generate the makeClosure() method and 
 * closure handling code for any methods which take this interface as a parameter
 * @author Ray Cromwell &lt;ray@timepedia.org&gt;
 * @gwt.export
 * @gwt.exportPackage test
 * @gwt.exportClosure
 */
public interface MyClickListener extends Exportable {
  
    /**
     * the call back
     * @gwt.export
     */
    public void onClick(Employee emp, String foo, int num);
}


