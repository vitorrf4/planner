const express = require("express");
const router = express.Router();
const Tarefa = require("../models/tarefa");
const tarefasDB = require("../models/tarefaDB");
const betterSse = require("better-sse")

const channel = betterSse.createChannel();

router.get("/connect", async (req, res) => {
    const session = await betterSse.createSession(req, res);

    channel.register(session);

    console.log("A user has joined: ", channel.sessionCount);

    // verifica se o request está vindo do browser
    if (req.headers["user-agent"].includes("Mozilla")) {
        session.push(tarefasDB.getTarefas());
        return;
    }

    session.push("[]");
});

router.post("/add-tarefa", async (req, res) => {
    const body = req.body;
    const novaTarefa = new Tarefa(body.titulo, body.descricao, body.dataFinal);

    tarefasDB.adicionarTarefa(novaTarefa);

    return channel.broadcast(novaTarefa);
});

router.get("/sessions", (req, res) => {
    res.json({channels: channel.activeSessions.length});
})


module.exports = router;