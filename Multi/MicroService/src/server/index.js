const express   = require('express')
const server    = express()
const bodyParser  	= require('body-parser');

const ShortUrl = require('../domain/db')
const idGenerator = require('../domain/tools/idGenerator')

server.use(bodyParser.json({limit: '50mb'}));
require('../routes')(server);

module.exports = server;