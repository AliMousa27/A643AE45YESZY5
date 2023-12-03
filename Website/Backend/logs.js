var express = require('express');
var router = express.Router();
var mqttClient = require('./mqtthelper.js');

function addMessage(req, res, next) {
    const message = `User with UID ${req.body.uid} ${req.path.includes('logins') ? 'logged in' : 'registered'} at ${new Date().toString()} with email ${req.body.email}`;
    req.bodyWithMessage = { ...req.body, "message": message };
    next();
}

//regex for logins and registrations to use either or topic. and function to access body with message
router.post('/logs/:type(logins|registrations)', addMessage, function (req, res, next) {
    try {
        console.log("endpoint called")
        const topic = `toothtrek/authentication/${req.params.type}`;
        mqttClient.publish(topic, JSON.stringify(req.bodyWithMessage));
        res.status(201).json({ 'message': req.body });
    } catch (err) {
        next(err);
    }
});

module.exports = router;