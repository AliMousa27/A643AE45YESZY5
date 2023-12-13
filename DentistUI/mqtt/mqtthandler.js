const mqtt = require('mqtt');
const { v4: uuidv4 } = require('uuid');
const protocol = 'tcp'
const host = process.env.MQTT_HOST || 'broker.hivemq.com'
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
})

mqttClient.on('error', (err) => {
    return next(err)
})

module.exports = mqttClient;
