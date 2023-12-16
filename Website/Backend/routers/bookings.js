var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqttHelper.js');
 
router.post('/bookings/:id', (req, res) => {
  console.log("create booking for patient by id")
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/create', req.params.id, req.body);
});
//get bookings for patient by id
router.get('/bookings/:id', (req, res) => {
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/get', req.params.id, req.body);
});

module.exports = router;