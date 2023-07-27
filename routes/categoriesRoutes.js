const CategoriesController = require('../controllers/categoriesController');

module.exports = (app, upload) => {
    // TRAER DATOS
    app.get('/api/categories/getAll',  CategoriesController.getAll); // Corrected method name

    // GUARDAR DATOS
    //Necesitamos enviar el token del usuario para crear una nueva categoria en la BD
    app.post('/api/categories/create', upload.array('image',1),CategoriesController.create);

}