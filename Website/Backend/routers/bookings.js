var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqttHelper.js');

var counter = require('./metrics').counter;
var histogram = require('./metrics').histogram;
 
router.post('/bookings/:id', (req, res) => {
  try {
    const start = new Date();
    mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/create/', req.params.id, req.body);
    counter.inc({ Entity: 'bookings', method: 'post', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'bookings', method: 'post', statusCode: 500 });
    res.status(500).send();
  }
});

router.get('/bookings/:id', (req, res) => {
  try {
    const start = new Date();
    var body = {
      "patientId": req.params.id
    }
    mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/get/', req.params.id, body);
    counter.inc({ Entity: 'bookings', method: 'get', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'bookings', method: 'get', statusCode: 500 });
    res.status(500).send();
  }
});

router.delete('/bookings/:patentId/:bookingId', (req, res) => {
  try {
    const start = new Date();
    var body = {
      "patientId": req.params.patentId,
      "bookingId": req.params.bookingId,
      state: "cancelled"
    }
    mqttClient.handleRequest(req, res, 'toothtrek/booking_service/booking/state/', req.params.patentId, body);
    counter.inc({ Entity: 'bookings', method: 'delete', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'bookings', method: 'delete', statusCode: 500 });
    res.status(500).send();
  }
});

module.exports = router;