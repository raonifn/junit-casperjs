var casper = require('casper').create({
	verbose : true,
	logLevel : "debug",
	colorizerType : 'Dummy'
});

var sys = require('system');
casper.test.assertTruthy(sys.env.PHANTOMJS_EXECUTABLE);

casper.exit(casper.test.getFailures().length);