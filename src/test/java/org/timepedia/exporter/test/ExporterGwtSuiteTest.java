package org.timepedia.exporter.test;

import junit.framework.Test;

import org.timepedia.exporter.test.issues.Issue25aTestGwt;
import org.timepedia.exporter.test.issues.Issue25bTestGwt;
import org.timepedia.exporter.test.issues.Issue33TestGwt;
import org.timepedia.exporter.test.issues.Issue34TestGwt;
import org.timepedia.exporter.test.issues.Issue35TestGwt;
import org.timepedia.exporter.test.issues.Issue44TestGwt;
import org.timepedia.exporter.test.issues.Issue48TestGwt;

import com.google.gwt.junit.tools.GWTTestSuite;
 public class ExporterGwtSuiteTest extends GWTTestSuite
  {
      public static Test suite()
      {
          GWTTestSuite suite = new GWTTestSuite( "Exporter Suite" );
          suite.addTestSuite(CoreTestGwt.class );
          suite.addTestSuite(Issue25aTestGwt.class);
          suite.addTestSuite(Issue25bTestGwt.class);
          suite.addTestSuite(Issue33TestGwt.class);
          suite.addTestSuite(Issue34TestGwt.class);
          suite.addTestSuite(Issue35TestGwt.class);
          suite.addTestSuite(Issue44TestGwt.class);
          suite.addTestSuite(Issue48TestGwt.class);
          return suite;
      }
}