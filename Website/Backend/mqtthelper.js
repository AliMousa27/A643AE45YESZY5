var mqtt = require('mqtt');
const protocol = 'tcp'
const host = 'broker.hivemq.com'
const mqttPort = '1883'
const clientId = `mqtt_${Math.random().toString(16).slice(3)}`
const connectUrl = `${protocol}://${host}:${mqttPort}`

const QOS = 1
const mqttClient = mqtt.connect(connectUrl, {
    clientId,
    clean: true,
    connectTimeout: 4000,
    username: 'emqx',
    password: 'public',
    reconnectPeriod: 1000,
})
mqttClient.on('connect', () => {
    console.log(`Connected to mqtt server with url ${connectUrl}`)
})
mqttClient.on('error', (err) => {
    return next(err)
})
mqttClient.handleRequest = function(req, res, requestTopic, uid) {
    try {
        console.log('Requesting timeslots for office:', uid);
        const responseTopic = `${requestTopic}/${uid}`;

        this.subscribe(responseTopic);
        this.publish(requestTopic, JSON.stringify({ id: uid }));

        const timeout = setTimeout(() => {
            res.status(500).json({ error: 'Request timed out' });
            this.unsubscribe(responseTopic);
        }, 50000);//CHANGE BACK TO 10 SEC

        this.once('message', (topic, message) => {
            if (topic === responseTopic) {
                clearTimeout(timeout);
                res.json(JSON.parse(message.toString()));
                this.unsubscribe(responseTopic);
            }
        });

    } catch (error) {
        console.error('Error:', error);
        res.status(500).send('Internal Server Error');
    }
};

module.exports = mqttClient;

module.exports = mqttClient;