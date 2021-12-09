'use strict';
const express   = require('express')
const apiServices = require('../controller/index');

const routers = (app) =>{
    app.use(express.urlencoded({ extended: false }))
    
    app.use('/',apiServices);
};

module.exports = routers;