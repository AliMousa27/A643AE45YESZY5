
var express = require('express');
var router = express.Router();
var mqttClient = require('./../mqttHelper.js');

function publishToTopic(topic, message, next) {
    mqttClient.publish(topic, message, { qos: 1, retain: false }, (error) => {
        if (error) {
            return next(error)
        }
    })
}
router.get("/offices", function (req, res, next) {

  try {
    mqttClient.subscribe('toothtrek/timeslots/get/UID', { qos: 1 }, (error) => {
        if (error) {
            return next(error)
        }
    })
    publishToTopic('toothtrek/timeslots/get', 'offices', next);

        const messageHandler = (topic, message) => {
            if (topic === 'toothtrek/timeslots/get/UID') {
                console.log("message received and it is " + message.toString());
              mqttClient.removeListener('message', messageHandler); 
              mqttClient.unsubscribe('toothtrek/timeslots/get/UID', (error) => {
                if (error) {
                  return next(error)
                }
              })
              console.log(message.toString())
              res.status(200).json(JSON.parse(message.toString()))
            }
          } 
          mqttClient.once('message', (topic, message) => messageHandler(topic, message));
        } catch (err) { 
        return next(err);
    }
});

module.exports = router;