junit-casperjs 
==============

JUnit Runner to CasperJS (http://casperjs.org/) Tests.

All files sufixed with `.test.js` on classpath will be executed as a TestCase.

**IMPORTANT**
The casperjs and phantomjs (http://phantomjs.org/) MUST be in the PATH to CasperRunner works.

**IMPORTANT 2**
The CasperRunner depends that casperjs returns a `exit code > 0` to fail a test. Then, it's recomended the code below:
```javascript
var casper = require('casper');
//...
casper.run(function() {
  this.exit(this.test.getFailures().length);
});
```

##Travis CI

![Build Status](https://travis-ci.org/raonifn/junit-casperjs.png?branch=master)

https://travis-ci.org/raonifn/junit-casperjs

## Example
```java
@RunWith(CasperRunner.class)
public class CasperTest {

	@Before
	public void before() throws Exception {
    // Starting the webserver...
	}

	@CasperEnvironment
	public Map<String, String> env() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("START_URL", "http://localhost:9898/casper");
		return map;

	}

	@After
	public void after() {
		// Stopping the webserver...
	}
}
```
example.test.js
```javascript
var sys = require('system');
var url = sys.env.START_URL;
var casper = require('casper').create({
	verbose : true,
	logLevel : "debug",
	colorizerType : 'Dummy'
});

if (!url) {
	this.casper.die('START_URL not found');
}

casper.start(url);

casper.then(function() {
	var text = this.evaluate(function() {
		return __utils__.findOne('h1').innerHTML;
	});
	this.test.assertEquals(text, 'Hello World')
});

// Is important to CasperRunner that the test exits with error when there are
// failures.
casper.run(function() {
	this.exit(this.test.getFailures().length, 1000);
});
```

## Maven users
Maven repository: http://raonifn.github.com/repository/releases/

* Dependency
```xml
<dependency>
   <groupId>com.github.raonifn</groupId>
   <artifactId>casperjs-junit</artifactId>
   <version>0.1.1</version>
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
