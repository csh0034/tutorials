'use strict';

module.exports.dispatch = (event, context, callback) => {
  console.log("Received event:", JSON.stringify(event, null, 2));

  const message = event.Records[0].Sns.Message;
  handleBounce(message);

  callback(null,'');
};

function handleBounce(message) {
  const messageId = message.mail.messageId;
  const addresses = message.bounce.bouncedRecipients.map(recipient => {
    return recipient.emailAddress;
  });
  const bounceType = message.bounce.bounceType;
  const bounceSubType = message.bounce.bounceSubType;

  console.log("messageId:", messageId)
  console.log("addresses:", addresses)
  console.log("bounceType:", bounceType)
  console.log("bounceSubType:", bounceSubType)
}
