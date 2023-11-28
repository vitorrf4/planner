//DOM Elements
const myMessages = document.getElementById("messages")
const sendBtn = document.getElementById("send")
sendBtn.addEventListener("click", sendMsg, false)

let source = new EventSource('http://localhost:3000/chat');

source.addEventListener('message', function(e) {
    console.log(e);
    // FIX json parsing, no words with commas allowed
    let data = JSON.parse(e.data);

    if (Array.isArray(data)) {
        for (const message of data) {
            msgGeneration(message)
        }
        return;
    }
    msgGeneration(data);
}, false);

source.addEventListener('open', function(e) {
    console.log(e.type);

    getTarefas().then();
}, false);

source.addEventListener('error', function(e) {
    if (e.readyState === EventSource.CLOSED) {
        console.log("connection closed")
    }
}, false);

//Sending message from client
async function sendMsg() {
    const titulo = document.getElementById("titulo").value
    const descricao = document.getElementById("descricao").value
    const dataFinal = document.getElementById("dataFinal").value

    const tarefa = JSON.stringify({
        titulo: titulo,
        descricao: descricao,
        dataFinal: dataFinal
    });

    await fetch("http://localhost:3000/message", {
            method: "POST",
            body: tarefa,
            headers: {
                "Content-Type": "application/json",
            }});
}

//Creating DOM element to show received messages on browser page
function msgGeneration(msg) {
    const newMessage = document.createElement("p");
    newMessage.innerText = `Titulo: ${msg.titulo}, Desc: ${msg.descricao}, Data: ${msg.dataFinal}`;
    myMessages.appendChild(newMessage)
}

async function getTarefas() {
    const req = await fetch("http://localhost:3000/tarefas");
    const tarefas = await req.json();
    console.log(tarefas);

    for (let tarefa of tarefas) {
        msgGeneration(tarefa);
    }
}