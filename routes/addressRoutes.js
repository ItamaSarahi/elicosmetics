const AddressController = require('../controllers/addressController');
const passport = require('passport');

module.exports = (app) => {

    /*
    * GET ROUTES
    */
   app.get('/api/address/findByUser/:id_user',  AddressController.findByUser);

    /*
    * POST ROUTES
    */
   app.post('/api/address/create', AddressController.create);
}