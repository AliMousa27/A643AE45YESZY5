var express = require('express');
var router = express.Router();
const client = require('prom-client');

const counter = new client.Counter({
    name: 'total_requests',
    help: 'Total number of requests',
    labelNames: ['Entity', 'method', 'statusCode'],
});

const histogram = new client.Histogram({
    name: 'http_request_duration_seconds',
    help: 'Duration of HTTP requests in seconds',
    buckets: [0.000001, 0.00001, 0.0001, 0.001, 0.01,0.1,1,5, 10,15],
});

router.get('/metrics', async (req, res) => {
    try {
        res.set('Content-Type', client.register.contentType);
        const metrics = await client.register.metrics();
        res.end(metrics);
    } catch (err) {
        console.error(err);
        res.status(500).end();
    }
});

module.exports = {counter, histogram, router};