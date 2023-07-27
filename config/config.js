const promise = require('bluebird');
const options = {
    promiseLib: promise,
    query: (e) => {}
}

const pgp = require('pg-promise')(options);
const types = pgp.pg.types;
types.setTypeParser(1114, function(stringValue) {
    return stringValue;
});

const databaseConfig = {
    'host': 'bhegem6cjdfhmr3sxudy-postgresql.services.clever-cloud.com',
    'port': 5432,
    'database': 'bhegem6cjdfhmr3sxudy',
    'user': 'u4uwrfgf7wgg4k7iwtg3',
    'password': 'Vog1Jp1xVxKIlJhrrV0CaH2Vppm5sx'
};

const db = pgp(databaseConfig);

module.exports = db;