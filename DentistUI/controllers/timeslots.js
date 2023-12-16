"use strict";

const express = require('express');
const router = express.Router();
const moment = require('moment');
const { v4: uuidv4 } = require('uuid');
const mqttClient = require('../mqtt/mqtthandler');

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

// --- POST ---

// Create a new timeslot
router.post('/', async (req, res) => {
    if (!req.body.dentistId || !req.body.officeId || !req.body.dateAndTime) {
        return res.status(400).send('Bad request (missing parameters)');
    }

    if (isNaN(req.body.dentistId) || isNaN(req.body.officeId)) {
        return res.status(400).send('Bad request (invalid parameters)');
    }

    if (!moment(req.body.dateAndTime, 'YYYY-MM-DD HH:mm', true).isValid()) {
        return res.status(400).send('Bad request (invalid date format)');
    }

    if (new Date(req.body.dateAndTime) < new Date()) {
        return res.status(400).send('Bad request (date should be in the future)');
    }

    const topic = 'toothtrek/booking_service/timeslot/create/';
    const responseTopic = topic + uuidv4();

    var timeslot = {
        dentistId: req.body.dentistId,
        officeId: req.body.officeId,
        dateAndTime: req.body.dateAndTime,
        responseTopic: responseTopic
    };

    try {
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

        const parsedResponse = JSON.parse(response.toString());
        if (response.status === 'success') {
            return res.status(201).send(parsedResponse);
        }
        else {
            return res.status(400).send(parsedResponse);
        }

    } catch (error) {
        console.error('Error:', error);
        return next(error);
    }
});

// --- DELETE ---

// Cancel a timeslot
router.delete('/:timeslotId', async (req, res) => {
    const timeslotId = req.params.timeslotId;
    if (isNaN(timeslotId)) {
        return res.status(400).send('Bad request (invalid parameters)');
    }

    const topic = 'toothtrek/booking_service/timeslot/cancel/';
    const responseTopic = topic + uuidv4();

    var timeslot = {
        timeslotId: timeslotId,
        responseTopic: responseTopic
    };

    try {
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

        const parsedResponse = JSON.parse(response.toString());
        if (response.status === 'success') {
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