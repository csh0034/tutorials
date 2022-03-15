'use strict';

module.exports.endpoint = (event, context, callback) => {

  console.log('event:', event);
  console.log('context:', context);
  console.log('callback', callback);

  const response = {
    statusCode: 200,
    body: JSON.stringify({
      message: `Hello, the current time is ${new Date().toTimeString()}.`,
    }),
  };

  callback(null, response);
};
