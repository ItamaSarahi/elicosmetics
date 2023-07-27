const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const logger = require('morgan');
const cors = require('cors');
const passport= require('passport')
const multer= require('multer')
const serviceAccount= require('./serviceAccountKey.json');
const admin = require('firebase-admin')

const io = require('socket.io')(server);
const ordersDeliverySocket = require('./sockets/orders_delivery_socket');




admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const upload = multer({
    storage: multer.memoryStorage()
});




/*
* RUTAS
*/
const users= require('./routes/usersRoutes');
const categories= require('./routes/categoriesRoutes');
const products = require('./routes/productsRoutes');
const address = require('./routes/addressRoutes')
const orders= require('./routes/orderRoutes')
const port = process.env.PORT || 3000;

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));
app.use(cors());
app.use(passport.initialize());
app.use(passport.session());

require('./config/passport')(passport);

app.disable('x-powered-by');

app.set('port', port);

//Llamando a las rutas

users(app,upload);
categories(app,upload);
products(app,upload);
address(app)
orders(app)

//INVOCAR A LOS SOCKETS
ordersDeliverySocket(io);

server.listen(3000,'192.168.1.81'|| 'localhost', function(){
    console.log('Aplicacion de NodeJS '+ port+ ' Iniciada...')
});


//ERROR
app.use((err,req,res,next)=>{
    console.log(err);
    res.status(err.status||500).send(err.stack);
});

module.exports={
    app:app,
    server:server
}