var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqtthelper.js');
 
router.post('/bookings/:id', (req, res) => {
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/create', req.params.id, req.body);
});


module.exports = router;