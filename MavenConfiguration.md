# Dependencies #

Gwtexporter is available in maven central repositories, so you do not have to setup additional repositories, just select the appropriate version for your gwt sdk.

  * gwtexporter (2.2.0, 2.4.0)
```
    <dependency>
      <groupId>org.timepedia.exporter</groupId>
      <artifactId>gwtexporter</artifactId>
      <version>2.4.0</version>
      <scope>provided</scope>
    </dependency>
```

  * gwtexporter with gwt-2.0.x and gwt-2.1.x
```
    <dependency>
      <groupId>org.timepedia.exporter</groupId>
      <artifactId>gwtexporter</artifactId>
      <version>2.1.1</version>
      <scope>provided</scope>
    </dependency>
```

  * gwtexporter snapshot (gwt-2.2.0, gwt-2.3.0, gwt-2.4.0, 2.5.x)
```
    <repository>
      <id>sonatype</id>
      <url>http://oss.sonatype.org/content/repositories/snapshots</url>
      <snapshots><enabled>true</enabled></snapshots>
      <releases><enabled>false</enabled></releases>
    </repository>
    <dependency>
      <groupId>org.timepedia.exporter</groupId>
      <artifactId>gwtexporter</artifactId>
      <version>2.5.0-SNAPSHOT</version>
      <scope>provided</scope>
    </dependency>
```

  * **NOTE:** If you either don't plan to use jsdoclet (see what it is below), or your java sdk does not include the `tools.jar` library which is provided with the sun sdk, you have to add this exclusion to your pom file:
```
    <dependency>
      <groupId>org.timepedia.exporter</groupId>
      <artifactId>gwtexporter</artifactId>
      <version>[Set your version here]</version>
      <scope>provided</scope>
      <exclusions>
        <exclusion>
          <groupId>com.sun</groupId>
          <artifactId>tools</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```

# JsDoclet #

Gwtexporter comes with a class able to generate the javascript api documentation of your project. The class inspects your sources and documents exported methods and classes.

If you configure correctly your pom.xml, the javadoc plugin will produce the file `./target/apidocs/jsdoc.html` after running `mvn package`.

Your pom.xml should look like this:

```
  <dependencies>
    ...
    <dependency>
      <groupId>com.sun</groupId>
      <artifactId>tools</artifactId>
      <version>1.6</version>
      <scope>system</scope>
      <optional>true</optional>
      <systemPath>${java.home}/../lib/tools.jar</systemPath>
    </dependency>
  </dependencies>

  <build>
    ...
    <plugins>
      ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <finalName>js</finalName>
          <doclet>org.timepedia.exporter.doclet.JsDoclet</doclet>
          <docletArtifact>
            <groupId>org.timepedia.exporter</groupId>
            <artifactId>gwtexporter</artifactId>
            <version>2.3.0</version>
          </docletArtifact>
          <destDir>${project.build.directory}/apidocs</destDir>
        </configuration>
        <executions>
          <execution>
            <phase>prepare-package</phase>
            <id>attach-jsdoc</id>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```
