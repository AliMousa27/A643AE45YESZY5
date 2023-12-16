var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqttHelper.js');
 
router.post('/patients/:id', (req, res) => {
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/patient/create/', req.params.id, req.body);
});

router.patch('/patients/:id', (req, res) => {
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/patient/set/', req.params.id, req.body);
});

router.get('/patients/:id', (req, res) => {
  var body = {
    "id": req.params.id
  }
  mqttClient.handleRequest(req, res, 'toothtrek/booking_service/patient/get/', req.params.id, body);
});

module.exports = router;