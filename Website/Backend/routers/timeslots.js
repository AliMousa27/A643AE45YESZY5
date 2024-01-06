
var express = require('express');
var router = express.Router();
var mqttClient = require('../mqttHelper');

router.get('/offices/:id', (req, res) => {
  console.log('Requesting offices with id:', req.params.id);
  mqttClient.handleRequest(req, res, 'toothtrek/timeslot/get/', req.params.id);
});


module.exports = router;