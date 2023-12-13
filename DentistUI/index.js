"use strict";

const express = require('express');
const morgan = require('morgan');

// Controllers
const timeslotsController = require('./controllers/timeslots');
const bookingsController = require('./controllers/bookings');

const app = express();
const port = process.env.PORT || 3000;

// Parse requests of content-type 'application/json'
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
// HTTP request logger
app.use(morgan('dev'));

// Controllers usage
app.use('/api/timeslots', timeslotsController);
app.use('/api/bookings', bookingsController);

// Catch all non-error handler for api (i.e., 404 Not Found)
app.use('/api/*', function (req, res) {
    res.status(404).json({ 'message': 'Not Found' });
});

// Error handler (i.e., when exception is thrown) must be registered last
var env = app.get('env');
// eslint-disable-next-line no-unused-vars
app.use(function (err, req, res, next) {
    console.error(err.stack);
    var err_res = {
        'message': err.message,
        'error': {}
    };
    if (env === 'development') {
        // Return sensitive stack trace only in dev mode
        err_res['error'] = err.stack;
    }
    res.status(err.status || 500);
    res.json(err_res);
});


app.listen(port, () => {
    console.log(`Server listening at http://localhost:${port}`);
});

module.exports = app;
