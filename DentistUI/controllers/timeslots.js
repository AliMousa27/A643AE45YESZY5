"use strict";

const express = require('express');
const router = express.Router();
const moment = require('moment');
const { v4: uuidv4 } = require('uuid');
const mqttClient = require('../mqtt/mqtthandler');

// --- POST ---

// Create a new timeslot
router.post('/', (req, res) => {
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

    var timeslot = {
        dentistId: req.body.dentistId,
        officeId: req.body.officeId,
        dateAndTime: req.body.dateAndTime
    };

    try {
        let topic = 'toothtrek/booking_service/timeslot/create/';
        let responseTopic = topic + uuidv4();

        if (req.body.responseTopic) {
            timeslot.responseTopic = responseTopic;
        }

        mqttClient.subscribe(responseTopic);
        mqttClient.publish(topic, JSON.stringify(timeslot), { properties: { responseTopic: responseTopic } });

        // Set a timeout for the response
        const timeout = setTimeout(() => {
            mqttClient.unsubscribe(responseTopic);
            return res.status(500).json({ error: 'Request timed out' });
        }, 15000);


        // Handle the response from the broker
        mqttClient.on('message', (topic, message) => {
            if (topic === responseTopic) {  
                clearTimeout(timeout);
                mqttClient.unsubscribe(responseTopic);
                const response = JSON.parse(message.toString());
                if (response.status === 'success') {
                    return res.status(201).send(response);
                }
                else {
                    return res.status(400).send(response);
                }
            }
        });

    } catch (error) {
        console.error('Error:', error);
        return next(error);
    }
});

module.exports = router;