const mongoose = require('mongoose')

// database connect
mongoose.connect('mongodb://mongo/tictactoe', {
  useNewUrlParser: true, useUnifiedTopology: true
})

//create a mongoose schema
const shortUrlSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  token: {
    type: String,
    required: true
  },
  player1: {
    type: String,
    required: true
  },
  player2: {
    type: String,
    required: true
  },
  matrix: {
    type: String,
    required: true
  },
  winner: {
    type: String,
    required: true
  },
  status: {
    type: String,
    required: true
  }
})

//export model
module.exports = mongoose.model('ShortUrl', shortUrlSchema)