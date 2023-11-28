const express = require("express");
const router = express.Router();
const Tarefa = require("../models/tarefa");
const tarefasDB = require("../models/tarefaDB");

let subscribers = [];

router.get("/connect", (req, res) => {
    const headers = {
        'Content-Type': 'text/event-stream',
        'Connection': 'keep-alive',
        'Cache-Control': 'no-cache'
    };
    res.writeHead(200, headers);
    res.write(`data: []\n\n`);

    const idCliente = Date.now();
    const novoCliente = {
        id: idCliente,
        response: res
    };

    subscribers.push(novoCliente);

    req.on('close', () => {
        console.log(`${idCliente} fechou a conexão`);
        subscribers = subscribers.filter(client => client.id !== idCliente);
    });
});

router.post("/add-tarefa", (req, res) => {
    const body = req.body;
    const novaTarefa = new Tarefa(body.titulo, body.descricao, body.dataFinal);

    res.json({"tarefa" : novaTarefa});

    tarefasDB.adicionarTarefa(novaTarefa)

    return enviarEventosAosInscritos(novaTarefa);

});

function enviarEventosAosInscritos(mensagem) {
    subscribers.forEach(sub => {
        sub.response.write(`data: ${JSON.stringify(mensagem)}\n\n`)
    });
}

module.exports = router;