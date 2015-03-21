// database setup
var mongo = require('mongoskin');
var db = mongo.db("mongodb://localhost:27017/hackday", {native_parser:true});

// We need this to build our post string
var http = require('http');
var https = require('https');
var OAuth = require('oauth').OAuth;

updateFromKhanToDB();

function updateFromKhanToDB(){
    console.log("updateFromKhanToDB");

    // authenticate agains khan site
    var user = 'ambienteKhan';
    var password = 'tutores';
    var oauthRequest = 'https://www.khanacademy.org/api/auth/request_token';
    var oauthAuthorization = 'https://www.khanacademy.org/api/auth/default_callback';
    var url=String('https://www.khanacademy.org/api/v1/user');//TODO
    var consumerKey = '5dbf348aa966c5f7f07e8ce2ba5e7a3badc234bc';
    var consumerSecret = 'fceb3aedb960374e74f559caeabab3562efe97b4';

    consumer = new OAuth(oauthRequest,
                    oauthAuthorization,
                    consumerKey, consumerSecret, '1.0',
                    null, 'HMAC-SHA1');

    consumer.get(url, user, password, function (err, data, response){
        console.log('==>Oauth result: ');
        console.log('err=' + err);
        console.log('data='+ data);

	//if(err == 0){}
	getUserDataFromKhan();
	getUserExercisesFromKhan();
    });
};

function getUserDataFromKhan(){
    console.log("getUserFromKhan");

    // try to obtain list of teachers
    var options = {
        host: 'www.khanacademy.org',
        port: 80,
        path: '/api/v1/user',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    var prot = options.port == 443 ? https : http;
    var req = prot.request(options, function(res)
    {
        var output = '';
        res.setEncoding('utf8');

        res.on('data', function (chunk) {
            output += chunk;
        });

        res.on('end', function() {
		console.log("output=" + output);
		if(res.statusCode == 200){
			//TODO update if already exists
			db.collection('user').insert(output, function(erro, result){ console.log('db erro=' + erro)});
			console.log("success: user stored into db"); 
		}
		else {
			console.log("status code error=" + res.statusCode);
		}
        });
    });

    req.on('error', function(err) {
        console.log('err=' + err);
    });

    req.end();
};


function getUserExercisesFromKhan(){
    console.log("getUserExercisesFromKhan");

    // try to obtain list of teachers
    var options = {
        host: 'www.khanacademy.org',
        port: 80,
        path: '/api/v1/user/exercises',
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    var prot = options.port == 443 ? https : http;
    var req = prot.request(options, function(res)
    {
        var output = '';
        res.setEncoding('utf8');

        res.on('data', function (chunk) {
            output += chunk;
        });

        res.on('end', function() {
		console.log("output=" + output);
		if(res.statusCode == 200){
			//TODO update data if already exists
			db.collection('user_exercise').insert(output, function(erro, result){ console.log('db erro=' + erro)});
			console.log("success: user_exercise stored into db"); 
		}
		else {
			console.log("status code error=" + res.statusCode);
		}
        });
    });

    req.on('error', function(err) {
        console.log('err=' + err);
    });

    req.end();
};




