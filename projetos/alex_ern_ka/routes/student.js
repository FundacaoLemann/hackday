var express = require('express');
var router = express.Router();

router.get('/', function(req, res, next) {
	//res.send('test');
	var db = req.db;
    	db.collection('student').find().toArray(function (err, items) {
        	res.json(items);
	});	
});

router.post('/', function(req, res, next) {
});

router.get('/:id', function(req, res, next) {
});

router.put('/:id', function(req, res, next) {
});

router.delete('/:id', function(req, res, next) {
});

module.exports = router;
