var stompClient = null;

function scrollToBottom(){
    var lastMessage = document.querySelector('.container > div:last-of-type');
    lastMessage.scrollIntoView();
}

function connect() {
    var socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected(), function(err){onError(err)});

    scrollToBottom();
};

//  const connect = () => {
//    const Stomp = require("stompjs");
//    var SockJS = require("sockjs-client");
//    SockJS = new SockJS("http://localhost:8080/ws");
//    stompClient = Stomp.over(SockJS);
//    stompClient.connect({}, onConnected, onError);
//  };

async function onConnected(){
    const currentUserId = document.getElementById("current-user-id").innerText;
    console.log("connected");
    await sleep(3000);
    stompClient.subscribe(
      "/user/" + currentUserId + "/queue/messages", function(msg) {
        onMessageReceived(msg);
    });
};

//  const onConnected = () => {
//    console.log("connected");
//    stompClient.subscribe(
//      "/user/" + currentUser.id + "/queue/messages",
//      onMessageReceived
//    );
//  };

function onError(err){
    console.log(err);
};

function onMessageReceived(msg){
    const message = JSON.parse(msg.body);

    var parentDiv = document.getElementById('message-parent-div');
    var nestedDiv = document.createElement('div');
    var innerDiv = document.createElement('div');
    var messageSenderDiv = document.createElement('div');
    var messageTextDiv = document.createElement('div');
    var messageTimeDiv = document.createElement('div');

    nestedDiv.className = 'd-flex justify-content-start';
    nestedDiv.id = 'contact-nested-div';
    messageSenderDiv.className = 'sender-name';

    var sender = document.createTextNode(message.sender);

    messageSenderDiv.appendChild(sender);
    messageTextDiv.className = 'message-text';

    var messageText = document.createTextNode(message.messageText);

    messageTextDiv.appendChild(messageText);
    messageTimeDiv.className = 'message-time d-flex justify-content-end';

    var messageTime = document.createTextNode(message.messageTime);

    messageTimeDiv.appendChild(messageTime);

    innerDiv.appendChild(messageSenderDiv);
    innerDiv.appendChild(messageTextDiv);
    innerDiv.appendChild(messageTimeDiv);
    nestedDiv.appendChild(innerDiv);
    parentDiv.appendChild(nestedDiv);
    window.location.reload(true);

    scrollToBottom();
 };

//  const onMessageReceived = (msg) => {
//    const notification = JSON.parse(msg.body);
//    const active = JSON.parse(sessionStorage.getItem("recoil-persist"))
//      .chatActiveContact;
//
//    if (active.id === notification.senderId) {
//      findChatMessage(notification.id).then((message) => {
//        const newMessages = JSON.parse(sessionStorage.getItem("recoil-persist"))
//          .chatMessages;
//        newMessages.push(message);
//        setMessages(newMessages);
//      });
//    } else {
//      message.info("Received a new message from " + notification.senderName);
//    }
//    loadContacts();
//  };

function sendMessage(){
    var text = document.getElementById('messageText').value;

    const chatDTO = {
        userContactId: document.getElementById('contact-id').innerText,
        messageText: text,
        username: document.getElementById('current-username').innerText
    };

    text.value = '';
    window.location.reload(true);
    stompClient.send("/app/chat", {}, JSON.stringify(chatDTO));
    scrollToBottom();

//    var parentDiv = document.getElementById('message-parent-div');
//    var nestedDiv = document.createElement('div');
//    var innerDiv = document.createElement('div');
//    var messageSenderDiv = document.createElement('div');
//    var messageTextDiv = document.createElement('div');
//    var messageTimeDiv = document.createElement('div');
//
//    nestedDiv.className = 'd-flex justify-content-end';
//    nestedDiv.id = 'host-nested-div';
//    messageSenderDiv.className = 'sender-name d-flex justify-content-start';
//
//    var sender = document.createTextNode("You");
//
//    messageSenderDiv.appendChild(sender);
//    messageTextDiv.className = 'message-text d-flex justify-content-start';
//
//    var messageText = document.createTextNode(text);
//
//    messageTextDiv.appendChild(messageText);
//    messageTimeDiv.className = 'message-time d-flex justify-content-end';
//
//    var messageTime = document.createTextNode("00:00");
//
//    messageTimeDiv.appendChild(messageTime);
//
//    innerDiv.appendChild(messageSenderDiv);
//    innerDiv.appendChild(messageTextDiv);
//    innerDiv.appendChild(messageTimeDiv);
//    nestedDiv.appendChild(innerDiv);
//    parentDiv.appendChild(nestedDiv);
//    window.location.reload(true);
};

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

//  const sendMessage = (msg) => {
//    if (msg.trim() !== "") {
//      const message = {
//        senderId: currentUser.id,
//        recipientId: activeContact.id,
//        senderName: currentUser.name,
//        recipientName: activeContact.name,
//        content: msg,
//        timestamp: new Date(),
//      };
//      stompClient.send("/app/chat", {}, JSON.stringify(message));
//
//      const newMessages = [...messages];
//      newMessages.push(message);
//      setMessages(newMessages);
//    }
//  };