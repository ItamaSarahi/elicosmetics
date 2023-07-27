const db= require('../config/config');
const bcrypt = require ('bcryptjs')


const User ={};

User.getAll=()=>{
    const sql =`
    SELECT 
        *
    FROM
      usuarios
    `;

    return db.manyOrNone(sql); //devolver muchos o ninguno
 
}

User.findByEmail=(email)=>{
  const sql= `
    SELECT
    U.id,
    U.email,
    U.nombre,
    U.apellido,
    U.image,
    U.telefono,
    U.password,
    U.session_token,
  json_agg(
    json_build_object(
    'id', R.id,
    'name', R.name,
    'image', R.image,
    'route', R.route
    )
  ) AS roles
  FROM
    usuarios AS u
  INNER JOIN
    user_has_roles AS UHR
  ON
  UHR.id_user = U.id
  INNER JOIN
    roles AS R
  
  ON
      R.id= UHR.id_rol
  
  WHERE
    U.email=$1
  
  GROUP BY
    U.id
    `
     
     return db.oneOrNone (sql,email)

}

User.findDelivary=()=>{
  const sql= `
SELECT
    U.id,
    U.email,
    U.nombre,
    U.apellido,
    U.image,
    U.telefono,
    U.password,
    U.session_token
  FROM
    usuarios AS u
  INNER JOIN
    user_has_roles AS UHR
  ON
  UHR.id_user = U.id
  INNER JOIN
    roles AS R
  ON
      R.id= UHR.id_rol
  WHERE
    R.id=3;
    `  
    return db.manyOrNone (sql)
}


User.findById=(id, callback)=>{
  const sql= `
  SELECT
    id,
    email,
    nombre,
    apellido,
    image,
    telefono,
    password,
    session_token
  FROM
    usuarios
  WHERE
     id=$1
     `;

     return db.oneOrNone (sql,id).then(user=>{callback(null,user)})//retorno uno o ningun usuario.

}





User.create=async (user)=>{

  const hash= await bcrypt.hash(user.password,10);
    const sql = `
    INSERT INTO
        usuarios(
          email,
          nombre,
          apellido,
          telefono,
          image,
          password,
          created_at,
          updated_at
        )
        VALUES($1, $2, $3, $4, $5, $6, $7, $8) RETURNING id
    `;

    return db.oneOrNone(sql, [

      user.email,
      user.nombre,
      user.apellido,
      user.telefono,
      user.image,
      hash,
      new Date(),
      new Date()

    ]);

}

User.update=(user)=>{
  const sql= `
  UPDATE
     usuarios
  SET 
     nombre= $2,
     apellido=$3,
     telefono=$4,
     image= $5,
     updated_at= $6
  WHERE
     id= $1
     `;

     return db.none(sql,[
        user.id,
        user.nombre,
        user.apellido,
        user.telefono,
        user.image,
        new Date()
     ]);
}


module.exports= User;