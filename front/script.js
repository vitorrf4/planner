//DOM Elements
const tarefasElement = document.getElementById("tarefas")
const sendBtn = document.getElementById("send")
sendBtn.addEventListener("click", adicionarTarefa, false)

const source = new EventSource('http://localhost:3000/connect');

source.addEventListener('adicionado', function(e) {
    console.log(e);
    let data = JSON.parse(e.data);

    criarElementoHTMLdaTarefa(data);

}, false);

source.addEventListener('replace', function(e) {
    console.log(e);
    let tarefas = JSON.parse(e.data);

    tarefasElement.innerHTML = "";

    for (const tarefa of tarefas) {
        criarElementoHTMLdaTarefa(tarefa)
    }

}, false);

source.addEventListener('open', function(e) {
    console.log("Connection opened")
}, false);

source.addEventListener('error', function(e) {
    if (e.readyState === EventSource.CLOSED) {
        console.log("Connection closed")
    }
}, false);

//Sending message from client
async function adicionarTarefa() {
    const titulo = document.getElementById("titulo").value
    const descricao = document.getElementById("descricao").value
    const dataFinal = document.getElementById("dataFinal").value

    const tarefa = JSON.stringify({
        id: 0,
        titulo: titulo,
        descricao: descricao,
        dataFinal: dataFinal
    });

    await fetch("http://localhost:3000/adicionar", {
            method: "POST",
            body: tarefa,
            headers: {
                "Content-Type": "application/json",
            }});
}

//Creating DOM element to show received messages on browser page
function criarElementoHTMLdaTarefa(tarefa) {
    const newMessage = document.createElement("p");
    const dataFinal = new Date(tarefa.dataFinal);

    newMessage.innerText = `Titulo: ${tarefa.titulo}, 
        Descrição: ${tarefa.descricao} 
        Data limite: ${dataFinal.toLocaleDateString()} ${dataFinal.getHours()}:${dataFinal.getMinutes()}`;
    tarefasElement.appendChild(newMessage);
}
