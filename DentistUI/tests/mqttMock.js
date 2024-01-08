require('dotenv').config();

if (process.env.TEST !== 'true') {
    console.log('Environment variable TEST is not set to true. Exiting MQTT mock Service...');
    return;
}

const mqtt = require('mqtt');
const { v4: uuidv4 } = require('uuid');
const protocol = 'tcp'
const host = process.env.MQTT_HOST || 'localhost'
const mqttPort = process.env.MQTT_PORT || 1883
const clientId = process.env.MQTT_CLIENT_ID || uuidv4();
const connectUrl = `${protocol}://${host}:${mqttPort}`

const QOS = 1
const mqttClient = mqtt.connect(connectUrl, {
    clientId,
    clean: true,
    connectTimeout: 4000,
    reconnectPeriod: 1000,
})

mqttClient.on('connect', () => {
    console.log('')
    console.log(`Connected to mqtt server with url ${connectUrl}`)
    console.log(`   Client id is ${clientId}`)
    console.log(`   QOS is ${QOS}`)
    console.log('')

    const topicToSubscribe = 'toothtrek/#';
    mqttClient.subscribe(topicToSubscribe, (err) => {
        if (err) {
            console.error('Error subscribing to topic:', err);
        }
    });
})

mqttClient.on('error', (err) => {
    console.error('MQTT error:', err);
})

mqttClient.on('message', (topic, message) => {
    const topics = topic.split('/');
    if (topics.length == 3) {
        const parsedMessage = JSON.parse(message);
        const responseTopic = parsedMessage.responseTopic;
        const responseMessage = JSON.stringify({ "status": "success" });
        mqttClient.publish(responseTopic, responseMessage, (err) => {
            if (err) {
                console.error('Error publishing message:', err);
            }
        });
    }
});

module.exports = mqttClient;
