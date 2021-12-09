const ShortUrl = require('../db')
const idGenerator = require('../tools/idGenerator')
const orm = require('../orm/tic-tac-toe-orm');

exports.GetAll = async (req, res) =>{
    try{
        respOrm = await orm.GetAll();  
        return res.status(200).send(respOrm);
    }catch(err){
        console.log("err =  get", err)
        return res.status(500)
    }
}

exports.CreateGame = async (req, res) =>{
    try{
        console.log("Creando nuevo juego --> ",req.body)
        let token = idGenerator()        
        respOrm = await orm.CreateOne(req.body, token);  
        console.log("Creando nuevo juego --> ",token)

        res.status(200).send({token: respOrm.token});
    }catch(err){
        console.log("err = ", err)
        return res.status(500).send("err",err);
    }    
}

exports.JoinGame = async (req, res) =>{
    try{
        respOrm = await orm.UpdateOneJoin(req.body);          
        if (respOrm == null) return res.sendStatus(404)
        res.status(200).send(respOrm);
    }catch(err){
        console.log("err = ", err)
        return res.status(500).send("err",err);
    }  
}

exports.Game = async (req, res) =>{
    try{        
        respOrm = await orm.UpdateOneGame(req.body); 
        console.log("Juego actualizado --> "+req.body.token)
        res.status(200).send(respOrm);
    }catch(err){
        console.log("err = ", err)
        return res.status(500).send("err",err);
    }  
}

exports.GetGame = async (req, res) =>{
    try{        
        respOrm = await orm.FindOne(req.body); 
        res.status(200).send(respOrm);
    }catch(err){
        console.log("err = ", err)
        return res.status(500).send("err",err);
    }  
}


