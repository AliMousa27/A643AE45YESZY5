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

router.patch('/:officeId', async (req, res) => {

    if (!req.params.officeId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.officeId)) {
        return res.status(400).send('invalid parameters: officeId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/office/update/' + uuidv4();

    var office = {
        id: req.params.officeId,
        responseTopic: responseTopic
    };

    if (req.body.name) {
        office.name = req.body.name;
    }

    if (req.body.address) {
        office.address = req.body.address;
    }

    if (req.body.longitude) {
        office.longitude = req.body.longitude;
    }

    if (req.body.latitude) {
        office.latitude = req.body.latitude;
    }


    try {
        let topic = 'toothtrek/booking_service/office/update';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(office));

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

router.get('/:officeId', async (req, res) => {

    if (!req.params.officeId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.officeId)) {
        return res.status(400).send('invalid parameters: officeId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/office/get/' + uuidv4();

    var office = {
        id: req.params.officeId,
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/office/get';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(office));

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

    const responseTopic = 'toothtrek/booking_service/office/get/' + uuidv4();

    var office = {
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/office/get';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(office));

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

    if (!req.body.name || !req.body.address || !req.body.longitude || !req.body.latitude) {
        return res.status(400).send('missing parameters' + JSON.stringify(req.body));
    }

    if (isNaN(req.body.longitude) || isNaN(req.body.latitude)) {
        return res.status(400).send('invalid parameters: longitude and latitude should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/office/create/' + uuidv4();

    var office = {
        name: req.body.name,
        address: req.body.address,
        longitude: req.body.longitude,
        latitude: req.body.latitude,
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/office/create';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(office));

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

router.delete('/:officeId', async (req, res) => {

    if (!req.params.officeId) {
        return res.status(400).send('missing parameters');
    }

    if (isNaN(req.params.officeId)) {
        return res.status(400).send('invalid parameters: officeId should be a number');
    }

    const responseTopic = 'toothtrek/booking_service/office/delete/' + uuidv4();

    var office = {
        id: req.params.officeId,
        responseTopic: responseTopic
    };

    try {
        let topic = 'toothtrek/booking_service/office/delete';

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(office));

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
