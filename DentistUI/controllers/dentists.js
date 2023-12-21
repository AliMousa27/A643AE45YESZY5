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

router.patch('/:dentistId', async (req, res) => {

    if (!req.params.dentistId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.dentistId)) {
        return res.status(400).send('invalid parameters: dentistId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/dentist/update/' + uuidv4();

    var dentist = {
        id: req.params.dentistId,
        responseTopic: responseTopic
    };

    if (req.body.name) {
        dentist.name = req.body.name;
    }

    if (req.body.dateOfBirth) {
        dentist.dateOfBirth = req.body.dateOfBirth;
    }


    try {
        let topic = 'toothtrek/booking_service/dentist/update/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(dentist));

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

router.get('/:dentistId', async (req, res) => {

    if (!req.params.dentistId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.dentistId)) {
        return res.status(400).send('invalid parameters: dentistId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/dentist/get/' + uuidv4();

    var dentist = {
        id: req.params.dentistId,
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/dentist/get/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(dentist));

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

router.get('/', async (req, res) => {

    const responseTopic = 'toothtrek/booking_service/dentist/get/' + uuidv4();

    var dentist = {
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/dentist/get/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(dentist));

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

router.post('/', async (req, res) => {

    if (!req.body.name) {
        return res.status(400).send('missing parameters' + JSON.stringify(req.body));
    }

    const responseTopic = 'toothtrek/booking_service/dentist/create/' + uuidv4();

    var dentist = {
        name: req.body.name,
        responseTopic: responseTopic
    };

    if (req.body.dateOfBirth) {
        dentist.dateOfBirth = req.body.dateOfBirth;
    }

    try {
        let topic = 'toothtrek/booking_service/dentist/create/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(dentist));

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

router.delete('/:dentistId', async (req, res) => {

    if (!req.params.dentistId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.dentistId)) {
        return res.status(400).send('invalid parameters: dentistId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/dentist/delete/' + uuidv4();

    var dentist = {
        id: req.params.dentistId,
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/dentist/delete/';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(dentist));

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
