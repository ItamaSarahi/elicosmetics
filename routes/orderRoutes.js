const OrdersController = require('../controllers/OrdersController');

module.exports = (app) => {
    // GET ROUTES
    app.get('/api/orders/findByStatus/:status', OrdersController.findByStatus); // Corrected method name
    app.get('/api/orders/findByClientAndStatus/:id_client/:status', OrdersController.findByClientAndStatus); // Corrected method name
    app.get('/api/orders/findByDeliveryAndStatus/:id_delivery/:status', OrdersController.findByDeliveryAndStatus); // Corrected method name

    // POST ROUTES
    app.post('/api/orders/create', OrdersController.create);

    // PUT ROUTES
    app.put('/api/orders/updateToDispatched', OrdersController.updateToDispatched);
    app.put('/api/orders/updateToOnTheWay', OrdersController.updateToOnTheWay);
    app.put('/api/orders/updateToDelivery', OrdersController.updateToDelivery);
    app.put('/api/orders/updateLatLng', OrdersController.updateLatLng);
}

