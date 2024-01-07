"use strict";

const express = require('express');
const router = express.Router();
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
    if (!req.body.timeslotId) {
        return res.status(400).send('Bad request (missing parameters)');
    }

    if (isNaN(req.body.timeslotId)) {
        return res.status(400).send('Bad request (invalid parameters)');
    }

    const topic = 'toothtrek/record/create';
    const responseTopic = topic + '/' + uuidv4();

    var record = {
        timeslotId: req.body.timeslotId,
        notes: req.body.notes || '',
        responseTopic: responseTopic
    };

    try {
        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(record));

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
            return res.status(201).send(parsedResponse);
        }
        else {
            return res.status(400).send(parsedResponse);
        }

    } catch (error) {
        return res.status(500).send(error.message);
    }

});

// Get a record
router.get('/:id', async (req, res) => {
    const topic = 'toothtrek/record/get';
    const responseTopic = topic + '/' + uuidv4();

    var record = {
        id: req.params.id,
        responseTopic: responseTopic
    };

    try {
        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(record));

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
        res.status(500).send(error.message);
    }
});

// Update a record
router.patch('/:id', async (req, res) => {
    const topic = 'toothtrek/record/update';
    const responseTopic = topic + '/' + uuidv4();

    var record = {
        id: req.params.id,
        notes: req.body.notes || '',
        responseTopic: responseTopic
    };

    try {
        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(record));

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
            return res.status(201).send(parsedResponse);
        }
        else {
            return res.status(400).send(parsedResponse);
        }

    } catch (error) {
        res.status(500).send(error.message);
    }
});

module.exports = router;