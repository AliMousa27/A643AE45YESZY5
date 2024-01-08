var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqttHelper.js');

var counter = require('./metrics').counter;
var histogram = require('./metrics').histogram;
 
router.post('/patients/:id', (req, res) => {
  try {
    const start = new Date();
    mqttClient.handleRequest(req, res, 'toothtrek/patient/create/', req.params.id, req.body);
    counter.inc({ Entity: 'patients', method: 'post', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'patients', method: 'post', statusCode: 500 });
    res.status(500).send();
  }
});

router.patch('/patients/:id', (req, res) => {
  try {
    const start = new Date();
    mqttClient.handleRequest(req, res, 'toothtrek/patient/update/', req.params.id, req.body);
    counter.inc({ Entity: 'patients', method: 'patch', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'patients', method: 'patch', statusCode: 500 });
    res.status(500).send();
  }
});

router.get('/patients/:id', (req, res) => {
  try {
    const start = new Date();
    var body = {
      "id": req.params.id
    }
    mqttClient.handleRequest(req, res, 'toothtrek/patient/get/', req.params.id, body);
    counter.inc({ Entity: 'patients', method: 'get', statusCode: 200 });
    const end = new Date();
    histogram.observe((end - start) / 1000);
  } catch (err) {
    console.error(err);
    counter.inc({ Entity: 'patients', method: 'get', statusCode: 500 });
    res.status(500).send();
  }
});

module.exports = router;