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

function publishToTopic(topic, message, next) {
    mqttClient.publish(topic, message, { qos: QOS, retain: false }, (error) => {
        if (error) {
            return next(error)
        }
    })
}

function subscribeToTopic(topic, next) {
    mqttClient.subscribe(topic, { qos: QOS }, (error) => {
        if (error) {
            return next(error)
        }
    })
}

module.exports = mqttClient;