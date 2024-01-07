
var express = require('express');
var router = express.Router();
var mqttClient = require('../mqttHelper');

var counter = require('./metrics').counter;
var histogram = require('./metrics').histogram;

router.get('/offices/:id', (req, res) => {
  try {
    const start = new Date();
    mqttClient.handleRequest(req, res, 'toothtrek/booking_service/timeslot/get/', req.params.id);
    counter.inc({ Entity: 'timeslots', method: 'get', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'timeslots', method: 'get', statusCode: 500 });
    res.status(500).send();
  }
});


module.exports = router;