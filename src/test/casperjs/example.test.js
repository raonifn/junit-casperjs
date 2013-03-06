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
