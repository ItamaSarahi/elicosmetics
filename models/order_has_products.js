const db = require('../config/config');

const OrderHasProducts = {};

OrderHasProducts.create = (id_order, id_product, quantity) => {
    const sql = `
    INSERT INTO 
        order_has_products(
            id_order,
            id_product,
            quantity,
            created_at,
            updated_at
        )
    VALUES ($1, $2, $3, $4, $5)
    `;
    
    return db.none(sql, [
        id_order,
        id_product,
        quantity,
        new Date(),
        new Date()
    ]);
}

/*Order.findByUser = (id_order) =>{
    const sql = `
    SELECT 
        id,
        id_user,
        address,
        neighborhood,
        lat,
        lng
    FROM
	    address
    WHERE 
	    id_user = $1
	`;

    return db.manyOrNone(sql, id_user)
}
*/
module.exports = OrderHasProducts;

  