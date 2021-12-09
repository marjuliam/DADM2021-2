'use strict';
const server = require('./src/server');
const port = 5000
server.listen(port);
console.log('Servidor escuchando en puerto ' + port);

server.on('error', err => {
    console.error(err);
});