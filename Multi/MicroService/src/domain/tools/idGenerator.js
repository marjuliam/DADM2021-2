const shortId = require('shortid')

module.exports =function generateId(){
    let id = shortId.generate()
    return id
}
