var stompClient = null;
var socket = null;

function scrollToBottom(){
    var lastMessage = document.querySelector('.container > div:last-of-type');
    lastMessage.scrollIntoView();
}

function connect(type) {
    console.log(type);
    socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(){onConnected(type)}, function(err){onError(err)});

    if(type === 'chat'){
        scrollToBottom();
        var textInput = document.getElementById('messageText');

        textInput.addEventListener("keypress", function(event) {
            if (event.key === "Enter" && textInput === document.activeElement && textInput.value !== "") {
                event.preventDefault();
                document.getElementById("button-addon2").click();
            }
        });
    }

//    var requestNavItem = document.getElementById('request-nav-item');
//    requestNavItem.addEventListener("click", function(event) {
//        var requestBadge = document.getElementById('request-badge');
//        requestBadge.setAttribute('id','reveal');
//    });
};

async function onConnected(type){
    const currentUserId = document.getElementById("current-user-id").innerText;

    while(socket.readyState === "CONNECTING"){
        await sleep(2000);
    }
    console.log("connected");

    if(type === 'chat'){
        stompClient.subscribe(
          "/user/" + currentUserId + "/queue/messages", function(msg) {
            onMessageReceived(msg, type);
        });
        console.log('Subscribed to message queue');
    }

    stompClient.subscribe(
      "/user/" + currentUserId + "/queue/connections", function(msg) {
        onMessageReceived(msg, type);
    });
    console.log('Subscribed to connection queue');
};

function onError(err){
    console.log(err);
};

function onMessageReceived(msg, type){
    const message = JSON.parse(msg.body);
    window.location.reload(true);
    console.log(message.activity);

    if(type === 'chat'){
        scrollToBottom();
    }

    if(type === 'connection' && message.activity === "RECEIVED_REQUEST"){
        alert("You have a new connection request from " + message.senderName);
//        var requestBadge = document.getElementById('reveal');
//        requestBadge.setAttribute('id','request-badge');
    }else if(type === 'connection' && message.activity === "ACCEPTED_YOUR_REQUEST"){
        alert(message.senderName + " accepted your connection request.");
    }
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

async function sendConnectionRequest(userId){
    const connectDTO = {
        recipientId: userId
    };

    stompClient.send("/app/request/send", {}, JSON.stringify(connectDTO));
    await sleep(500);
    window.location.reload(true);
};

async function acceptConnectionRequest(requestId){
    const requestStatusDTO = {
        status: "ACCEPTED"
    };

    stompClient.send("/app/request/" + requestId + "/update", {},  JSON.stringify(requestStatusDTO));
    await sleep(500);
    window.location.reload(true);
};

async function rejectConnectionRequest(requestId){
    const requestStatusDTO = {
        status: "REJECTED"
    };

    stompClient.send("/app/request/" + requestId + "/update", {}, JSON.stringify(requestStatusDTO));
    await sleep(500);
    window.location.reload(true);
};

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function findUserModalEvent(){
    var modal = document.getElementById('connectionModal');

    modal.addEventListener('show.bs.modal', function(e) {
        var userId = e.relatedTarget.getAttribute('data-user-id');
        var userName = e.relatedTarget.getAttribute('data-user-name');

        e.currentTarget.querySelector('#proceed-button').setAttribute('onclick', "sendConnectionRequest(" + userId + ")");
        e.currentTarget.querySelector('.modal-body').innerText = "You are about to send a connection request to " +
        userName + ". You'll be able to exchange messages with them only when this request is accepted.";
    });
}

function requestsModalEvent(){
    var rejectRequestModal = document.getElementById('rejectRequestModal');
    var acceptRequestModal = document.getElementById('acceptRequestModal');

    rejectRequestModal.addEventListener('show.bs.modal', function(e) {
        var requestId = e.relatedTarget.getAttribute('data-request-id');
        var senderName = e.relatedTarget.getAttribute('data-request-sender-name');

        e.currentTarget.querySelector('.proceed-button').setAttribute('onclick', "rejectConnectionRequest(" + requestId + ")");
        e.currentTarget.querySelector('.modal-body').innerText = "Reject connection request from " + senderName + ".";
    });

    acceptRequestModal.addEventListener('show.bs.modal', function(e) {
        var requestId = e.relatedTarget.getAttribute('data-request-id');
        var senderName = e.relatedTarget.getAttribute('data-request-sender-name');

        e.currentTarget.querySelector('.proceed-button').setAttribute('onclick', "acceptConnectionRequest(" + requestId + ")");
        e.currentTarget.querySelector('.modal-body').innerText = "Accept connection request from " + senderName + ".";
    });
}