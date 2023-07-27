const UsersConstroller = require('../controllers/usersController');
const User = require('../models/user');

module.exports= (app, upload) =>{

    //Obtener datos
    app.get('/api/users/getAll',UsersConstroller.getAll);
    app.get('/api/users/findDelivary',UsersConstroller.findDelivary);

    //Guardar o crear datos
    app.post('/api/users/create', UsersConstroller.register);

    //Iniciar sesion
    app.post('/api/users/login', UsersConstroller.login);

    //actualizacion de datos
    app.put('/api/users/update',upload.array('image', 1), UsersConstroller.update);

    app.put('/api/users/updateWithoutImage',UsersConstroller.updateWithoutImage);
} 