
var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqtthelper.js');


router.get('/offices/:id', (req, res) => {
  console.log('Requesting offices with id:', req.params.id);
  mqttClient.handleRequest(req, res, 'toothtrek/timeslots/get', req.params.id);
});


module.exports = router;