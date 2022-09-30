var stompClient = null;
var socket = null;

function scrollToBottom(){
    var lastMessage = document.querySelector('.container > div:last-of-type');
    lastMessage.scrollIntoView();
}

function connect() {
    socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame){onConnected(frame)}, function(err){onError(err)});

    scrollToBottom();
    var textInput = document.getElementById('messageText');

    textInput.addEventListener("keypress", function(event) {
        if (event.key === "Enter" && textInput === document.activeElement && textInput.value !== "") {
            event.preventDefault();
            document.getElementById("button-addon2").click();
        }
    });
};

async function onConnected(frame){
    console.log(frame.command);
    const currentUserId = document.getElementById("current-user-id").innerText;
    console.log("connected");

    while(socket.readyState === "CONNECTING"){
        await sleep(2000);
    }

    stompClient.subscribe(
      "/user/" + currentUserId + "/queue/messages", function(msg) {
        onMessageReceived(msg);
    });
};

function onError(err){
    console.log(err);
};

function onMessageReceived(msg){
    const message = JSON.parse(msg.body);

//    var parentDiv = document.getElementById('message-parent-div');
//    var nestedDiv = document.createElement('div');
//    var innerDiv = document.createElement('div');
//    var messageSenderDiv = document.createElement('div');
//    var messageTextDiv = document.createElement('div');
//    var messageTimeDiv = document.createElement('div');
//
//    nestedDiv.className = 'd-flex justify-content-start';
//    nestedDiv.id = 'contact-nested-div';
//    messageSenderDiv.className = 'sender-name';
//
//    var sender = document.createTextNode(message.sender);
//
//    messageSenderDiv.appendChild(sender);
//    messageTextDiv.className = 'message-text';
//
//    var messageText = document.createTextNode(message.messageText);
//
//    messageTextDiv.appendChild(messageText);
//    messageTimeDiv.className = 'message-time d-flex justify-content-end';
//
//    var messageTime = document.createTextNode(message.messageTime);
//
//    messageTimeDiv.appendChild(messageTime);
//
//    innerDiv.appendChild(messageSenderDiv);
//    innerDiv.appendChild(messageTextDiv);
//    innerDiv.appendChild(messageTimeDiv);
//    nestedDiv.appendChild(innerDiv);
//    parentDiv.appendChild(nestedDiv);
    window.location.reload(true);

    scrollToBottom();
 };

async function sendMessage(){
    var text = document.getElementById('messageText').value;

    const chatDTO = {
        userContactId: document.getElementById('contact-id').innerText,
        messageText: text,
        username: document.getElementById('current-username').innerText
    };

    text = '';
    stompClient.send("/app/chat", {}, JSON.stringify(chatDTO));
    await sleep(100);
    window.location.reload(true);
    scrollToBottom();
};

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}