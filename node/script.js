//DOM Elements
const myMessages = document.getElementById("messages")
const myInput = document.getElementById("message")
const sendBtn = document.getElementById("send")

sendBtn.addEventListener("click", sendMsg, false)

let source = new EventSource('http://localhost:3000/chat');

source.addEventListener('message', function(e) {
    console.log(e);
    // FIX json parsing, no words with commas allowed
    let data = JSON.parse(e.data).toString();
    data = data.split(",");

    for (const message of data) {
        msgGeneration(message)
    }
    // msgGeneration(e.data);
}, false);

source.addEventListener('open', function(e) {
    console.log(e);
    console.log("connection opened");
}, false);

source.addEventListener('error', function(e) {
    if (e.readyState === EventSource.CLOSED) {
        console.log("connection closed")
    }
}, false);

//Sending message from client
async function sendMsg() {
    const message = JSON.stringify({"message": myInput.value});

    await fetch("http://localhost:3000/message", {
            method: "POST",
            body: message,
            headers: {
                "Content-Type": "application/json",
            }});
}

//Creating DOM element to show received messages on browser page
function msgGeneration(msg) {
    const newMessage = document.createElement("h5");
    newMessage.innerText = `Message: ${msg}`;
    myMessages.appendChild(newMessage)
}