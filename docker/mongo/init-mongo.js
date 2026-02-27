db = db.getSiblingDB('admin');

db.createUser({
    user: 'customer_app',
    pwd: 'customer_app_password',
    roles: [
        {
            role: 'readWrite',
            db: 'customer'
        }
    ]
});
