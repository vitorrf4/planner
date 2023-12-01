//DOM Elements
const tarefasElement = document.getElementById("tarefas")
const sendBtn = document.getElementById("send")
sendBtn.addEventListener("click", adicionarTarefa, false)

const source = new EventSource('http://localhost:3000/connect');

// listeners
source.addEventListener('adicionado', function(e) {
    console.log(e);
    const data = JSON.parse(e.data);

    adicionaTarefa(data);

}, false);

source.addEventListener('replace', function (e) {
    console.log(e);
    const tarefas = JSON.parse(e.data);

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

    // Adiciona digito adicional caso o tempo só tenha um digito
    const adicionaSegundoDigito = (value) => (value < 10 ? `0${value}` : value);
    const horaFormatada = adicionaSegundoDigito(dataFinal.getHours());
    const minutosFormatado = adicionaSegundoDigito(dataFinal.getMinutes());

    li.innerText = `Titulo: ${tarefa.titulo}
        Descrição: ${tarefa.descricao} 
        Data limite: ${dataFinal.toLocaleDateString()} ${horaFormatada}:${minutosFormatado}
        Status: ${tarefa.status}`;

    adicionaBotoes(li, tarefa);

    return li;
}

function adicionaBotoes(li, tarefa) {
    const excluirBtn = document.createElement("button");
    excluirBtn.innerHTML = "EXCLUIR";
    excluirBtn.onclick = () => excluirTarefa(tarefa.id);

    const statusBtn = document.createElement("button");
    statusBtn.innerHTML = "MUDAR STATUS";
    statusBtn.onclick = () => atualizarTarefa(tarefa);

    li.append(statusBtn, excluirBtn);
}

async function excluirTarefa(id) {
    await fetch(`http://localhost:3000/deletar/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        }
    });
}

async function atualizarTarefa(tarefa) {
    mudarStatus(tarefa);

    tarefa.dataFinal = new Date(Date.parse(tarefa.dataFinal.toString()));

    const tarefaJson = JSON.stringify(tarefa);

    console.log(tarefaJson);

    await fetch(`http://localhost:3000/atualizar`, {
        method: "PUT",
        body: tarefaJson,
        headers: {
            "Content-Type": "application/json",
        }
    });
}

function mudarStatus(tarefa) {
    if (tarefa.status === "PENDENTE") {
        tarefa.status = "COMPLETA";
    } else {
        tarefa.status = "PENDENTE";
    }
}