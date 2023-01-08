var stompClient = null;
var socket = null;

function scrollToBottom(){
    var lastMessage = document.querySelector('#message-container > div:last-of-type');
    lastMessage.scrollIntoView();
}

function connect(type) {
    console.log(type);
    socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(){onConnected(type)}, function(err){onError(err)});

    if(type === 'chat'){
        scrollToBottom();
        stickyNav();
        var textInput = document.getElementById('messageText');

        textInput.addEventListener("keypress", function(event) {
            if (event.key === "Enter" && textInput === document.activeElement && textInput.value !== "") {
                event.preventDefault();
                document.getElementById("button-addon2").click();
            }
        });
    }

    switch (document.title) {
        case "Profile":
            activateNavLink(0);
            break;
        case "Contacts":
            activateNavLink(1);
            break;
        case "Find Users":
            activateNavLink(2);
            break;
        case "Requests":
            activateNavLink(3);
            break;
        case "Settings":
            activateNavLink(4);
    }
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
    play();
    const message = JSON.parse(msg.body);
    console.log(message.activity);

    if(type === 'chat'){
        var messageContainer = document.getElementById('message-container');
        var chatSubDiv = document.getElementById('chat-sub-div');
        var div2 = document.createElement('div');
        var div3 = document.createElement('div');
        var div4 = document.createElement('div');
        var div5 = document.createElement('div');

        div2.setAttribute('id', 'contact-nested-div');
        div2.classList.add('d-flex', 'justify-content-start');
        div4.classList.add('message-text');
        div4.innerText = message.messageText;
        div5.classList.add('message-time', 'd-flex', 'justify-content-end');
        div5.innerText = message.messageTime;

        var fragment = document.createDocumentFragment();
        var divs = fragment
          .appendChild(document.createElement('div'))
          .appendChild(div2)
          .appendChild(div3)
          .appendChild(div4);

        div3.appendChild(div5);
        messageContainer.insertBefore(fragment, chatSubDiv);
        scrollToBottom();
    }

    if(type === 'connection' && message.activity === "RECEIVED_REQUEST"){
        alert("You have a new connection request from " + message.senderName);
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

    var time = moment().format('DD-MM HH:mm')
    var messageContainer = document.getElementById('message-container');
    var chatSubDiv = document.getElementById('chat-sub-div');
    var div2 = document.createElement('div');
    var div3 = document.createElement('div');
    var div4 = document.createElement('div');
    var div5 = document.createElement('div');

    div2.setAttribute('id', 'host-nested-div');
    div2.classList.add('d-flex', 'justify-content-end');
    div4.classList.add('message-text', 'd-flex', 'justify-content-start');
    div4.innerText = text;
    div5.classList.add('message-time', 'd-flex', 'justify-content-end');
    div5.innerText = time;

    var fragment = document.createDocumentFragment();
    var divs = fragment
      .appendChild(document.createElement('div'))
      .appendChild(div2)
      .appendChild(div3)
      .appendChild(div4);

    div3.appendChild(div5)
    messageContainer.insertBefore(fragment, chatSubDiv)
    scrollToBottom();

    stompClient.send("/app/chat", {}, JSON.stringify(chatDTO));
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

function stickyNav(){
    window.addEventListener('scroll', function() {
        if (window.scrollY > 50) {
            document.getElementById('navbar_top').classList.add('fixed-top');

            navbar_height = document.querySelector('.navbar').offsetHeight;
            document.body.style.paddingTop = navbar_height + 'px';
        } else {
            document.getElementById('navbar_top').classList.remove('fixed-top');
            document.body.style.paddingTop = '0';
        }
    });
}

function play() {
    var audio = new Audio('https://drive.google.com/uc?id=12eRadqgLXMkp2ITU7SzdpzoaKV6HdAI3');
    audio.play();
}

function activateNavLink(nav){
    var auth_nav_links = document.querySelectorAll(".auth-nav-link");

    for(let i = 0; i < auth_nav_links.length ; i++){
        auth_nav_links[i].classList.remove("active");
    }
    auth_nav_links[nav].classList.add("active");
}

function uploadImage(){
}