const TicTacToe = require('../db')

exports.GetAll = async () =>{
    try{
        let AllRes = await TicTacToe.find({ status: 'open'}) 
        console.log("orm-tic-tac-toe.GetAll = ", AllRes);
        return AllRes 
    }catch(err){
        console.log(" err orm-tic-tac-toe.GetAll = ", err);
        return await {err:{code: 123, messsage: err}}
    }
}

exports.CreateOne = async (fullBody, token_id) =>{
    try{
        let res = await TicTacToe.create({ name: fullBody.name, token: token_id, player1: fullBody.player1, player2:"NN", matrix:"[[0,0,0],[0,0,0],[0,0,0]]", winner:"NN",status:"open"})   
        return res       
    }catch(err){
        console.log(" err orm-tic-tac-toe.CreateOne = ", err);
        return await {err:{code: 123, messsage: err}}
    }    
}

exports.FindOne = async (tictactoe) =>{
    try{
        let response = await TicTacToe.findOne({ token: tictactoe.token })
        return response
        
    }catch(err){
        console.log(" err orm-tic-tac-toe.FindOne = ", err);
        return await {err:{code: 123, messsage: err}}
    }  
}

exports.DeleteOne = async (tictactoe) =>{
    try{
        const responce = await TicTacToe.deleteOne({ short: tictactoe  })    
        return responce 
    }catch(err){
        console.log(" err orm-tic-tac-toe.DeleteOne = ", err);
        return await {err:{code: 123, messsage: err}}
    } 
}

exports.UpdateOneJoin = async (fullBody) =>{
    try{
        const responce = await TicTacToe.updateOne({token:fullBody.token},{ player2: fullBody.player2, status:"OnGame"})    
        return responce 
    }catch(err){
        console.log(" err orm-tic-tac-toe.UpdateOneJoin = ", err);
        return await {err:{code: 123, messsage: err}}
    } 
}

exports.UpdateOneGame = async (fullBody) =>{
    try{
        const responce = await TicTacToe.updateOne({token:fullBody.token},{ player2: fullBody.player2, status:fullBody.status, winner:fullBody.winner, matrix:fullBody.matrix})    
        return responce 
    }catch(err){
        console.log(" err orm-tic-tac-toe.findOneAndUpdate = ", err);
        return await {err:{code: 123, messsage: err}}
    } 
}