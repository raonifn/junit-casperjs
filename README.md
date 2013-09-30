junit-casperjs 
==============

JUnit Runner to CasperJS (http://casperjs.org/) Tests.

All files sufixed with `.test.js` on classpath will be executed as a TestCase.

**IMPORTANT**
The CasperRunner depends that casperjs returns a `exit code > 0` to fail a test. Then, it's recomended the code below:
```javascript
var casper = require('casper');
//... 
casper.run(function() {
  this.exit(this.test.getFailures().length);
});
```

## Current Version
0.4.0

##Travis CI

[![Build Status](https://travis-ci.org/raonifn/junit-casperjs.png)](https://travis-ci.org/raonifn/junit-casperjs)

##PhantomJS and CasperJS executable
If the `casperjs` or `phantomjs` aren't in the PATH, you may set the java System Properties `casperjs.executable` and `phantomjs.executable` to say to `CasperRunner` where the executables are, e.g.: 
```
mvn test -Dcasperjs.executable=/opt/casperjs/bin/casperjs -Dphantomjs.executable=/opt/phantomjs/bin/phantomjs
```

## Example
 * JUnit Test Class: [CasperTest.java](src/test/java/com/github/raonifn/casperjs/junit/CasperTest.java)

 * Casper Test: [example.test.js](src/test/casperjs/example.test.js)

## Maven users
Maven repository: http://raonifn.github.com/repository/releases/

* Dependency
```xml
<dependency>
   <groupId>com.github.raonifn</groupId>
   <artifactId>casperjs-junit</artifactId>
   <version>0.4.0</version>
</dependency>
```

* Repository
```xml
<repository>
   <id>raonifn-repo</id>
   <name>Raonifn Maven Repository</name>
   <url>http://raonifn.github.com/repository/releases/</url>
   <layout>default</layout>
   <snapshots>
      <enabled>false</enabled>
   </snapshots>
</repository>
```
