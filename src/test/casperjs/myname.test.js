var casper = require('casper').create({
	verbose : true,
	logLevel : "debug",
	colorizerType : 'Dummy'
});

casper.test.assertEquals(casper.cli.get('script-name').trim(), 'myname.test.js');
casper.exit(casper.test.getFailures().length);