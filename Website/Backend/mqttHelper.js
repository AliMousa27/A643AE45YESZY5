require('dotenv').config();

var mqtt = require('mqtt');
const protocol = 'tcp'
const host = process.env.MQTT_HOST || 'localhost'
const mqttPort = process.env.MQTT_PORT || '1883'
const clientId = process.env.MQTT_CLIENT_ID || `mqtt_${Math.random().toString(16).slice(3)}`
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
    console.log(err)
})
const resolvers = new Map();
mqttClient.on('message', (topic, message) => {
    const resolve = resolvers.get(topic);
    if (resolve) {
        resolve(message.toString());
        resolvers.delete(topic);
    }
});
mqttClient.handleRequest = async function(req, res, requestTopic, uid,body) {
    try {
        const responseTopic = `${requestTopic}${uid}`;
        this.subscribe(responseTopic);
        var publishJson;
        if(body){
            publishJson = JSON.stringify({ "responseTopic": responseTopic, ...body });        }
        else{
            publishJson = JSON.stringify({ "responseTopic": responseTopic });
        }
        mqttClient.publish(requestTopic, publishJson);

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

        /*
        this.publish(requestTopic, publishJson);
        console.log("the publish json is: " + publishJson)
        console.log("published to topic " + requestTopic)
        const timeout = setTimeout(() => {
            this.unsubscribe(responseTopic);
            return res.status(500).json({ error: 'Request timed out' });
        }, 50000);//CHANGE BACK TO 10 SEC

        this.once('message', (topic, message) => {
            if (topic === responseTopic) {
                clearTimeout(timeout);
                this.unsubscribe(responseTopic);
                return res.json(JSON.parse(message.toString()));

            }
        });*/

    } catch (error) {
        console.error('Error:', error);
        return res.status(500).send('Internal Server Error');
    }
};

module.exports = mqttClient;

module.exports = mqttClient;