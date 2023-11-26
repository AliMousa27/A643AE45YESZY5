var express = require('express');
var router = express.Router();
var mqttClient = require('./mqttHelper.js');

router.get('/:id', async function (req, res, next) {
    try {
        // set request and response topics
        const requestTopic = 'toothtrek/something';
        const responseTopic = `toothtrek/something/${req.params.id}`;

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(requestTopic, JSON.stringify({ id: req.params.id }));

        // Set a timeout for the response
        const timeout = setTimeout(() => {
            res.status(500).json({ error: 'Request timed out' });
            mqttClient.unsubscribe(responseTopic);
        }, 5000);

        // Handle the response from the broker
        mqttClient.on('message', (topic, message) => {
            if (topic === responseTopic) {
                clearTimeout(timeout);
                res.json(JSON.parse(message.toString()));
                mqttClient.unsubscribe(responseTopic);
            }
        });


    } catch (error) {
        console.error('Error:', error);
        res.status(500).send('Internal Server Error');
    }
});


module.exports = router;