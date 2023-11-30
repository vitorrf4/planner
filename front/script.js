//DOM Elements
const tarefasElement = document.getElementById("tarefas")
const sendBtn = document.getElementById("send")
sendBtn.addEventListener("click", adicionarTarefa, false)

const source = new EventSource('http://localhost:3000/connect');

// listeners
source.addEventListener('adicionado', function(e) {
    console.log(e);
    let data = JSON.parse(e.data);

    adicionaTarefa(data);

}, false);

source.addEventListener('replace', function (e) {
    console.log(e);
    let tarefas = JSON.parse(e.data);

    tarefasElement.innerHTML = "";

    for (const tarefa of tarefas) {
        adicionaTarefa(tarefa)
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

// funcoes
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

function adicionaTarefa(tarefa) {
    const li = criaElementoHTMLTarefa(tarefa);

    tarefasElement.appendChild(li);
}

function criaElementoHTMLTarefa(tarefa) {
    const li = document.createElement("li");
    li.id = tarefa.id;

    const dataFinal = new Date(tarefa.dataFinal);

    li.innerText = `Titulo: ${tarefa.titulo}
        Descrição: ${tarefa.descricao} 
        Data limite: ${dataFinal.toLocaleDateString()} ${dataFinal.getHours()}:${dataFinal.getMinutes()}`;

    const excluirBtn = document.createElement("button");

    excluirBtn.innerHTML = "EXCLUIR";
    excluirBtn.onclick = () => excluirTarefa(tarefa.id);
    li.append(excluirBtn);

    return li;
}

async function excluirTarefa(id) {
    console.log(id);
    await fetch(`http://localhost:3000/deletar/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        }
    });
}
