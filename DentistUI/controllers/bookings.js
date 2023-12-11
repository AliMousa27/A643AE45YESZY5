"use strict";

const express = require('express');
const router = express.Router();
const { v4: uuidv4 } = require('uuid');
const mqttClient = require('../mqtt/mqtthandler');

// Dentist cannot mark a booking as 'cancelled'. The patient can only cancel a booking.
const STATES = ['confirmed', 'rejected', 'completed'];

// Map to store the resolvers for each request
const resolvers = new Map();

// 'message' event listener when the script starts running.
// Instead of creating a new listener for each request.
// To prevent memory leaks 
// (MaxListenersExceededWarning: Possible EventEmitter memory leak.).
mqttClient.on('message', (topic, message) => {
    const resolve = resolvers.get(topic);
    if (resolve) {
        resolve(message.toString());
        resolvers.delete(topic);
    }
});

router.patch('/:timeslotId', async (req, res) => {

    if (!req.params.timeslotId || !req.body.state) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.timeslotId)) {
        return res.status(400).send('invalid parameters: timeslotId should be a number');
    }

    if (!STATES.includes(req.body.state.toLowerCase())) {
        return res.status(400).send('invalid parameters: state should be one of: ' + STATES.join(', ') + ')');
    }

    const responseTopic = 'toothtrek/booking_service/booking/state/' + uuidv4();

    var timeslot = {
        id: req.params.timeslotId,
        state: req.body.state.toLowerCase(),
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/booking/state/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(timeslot));

        // Create a new Promise for the request
        const response = await new Promise((resolve, reject) => {
            // Store the resolver in the map
            resolvers.set(responseTopic, resolve);

            // Set a timeout for the response
            const timeout = setTimeout(() => {
                mqttClient.unsubscribe(responseTopic);
                resolvers.delete(responseTopic);
                reject(new Error('Request timed out'));
            }, 15000);
        });

        mqttClient.unsubscribe(responseTopic);

        // Handle the response from the broker
        const parsedResponse = JSON.parse(response);
        if (parsedResponse.status === 'success') {
            return res.status(200).send(parsedResponse);
        }
        else {
            return res.status(400).send(parsedResponse);
        }

    } catch (error) {
        return res.status(500).send(error.message);
    }
});

module.exports = router;