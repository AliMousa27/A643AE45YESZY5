var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqtthelper.js');

router.post('/bookings/:id', (req, res) => {
  console.log('booking:', req.params.id);
  mqttClient.handleRequest(req, res, 'toothtrek/booking/create', req.params.id);
});


module.exports = router;