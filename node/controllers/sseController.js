const express = require("express");
const service = require("../services/tarefaService")
const sessionHandler = require("../services/sessionHandler");

const router = express.Router();

router.get("/connect", async (req, res) => {
    await sessionHandler.connect(req, res, service.getTarefas());
});

router.post("/add-tarefa", (req, res) => {
    const novaTarefa = service.addTarefa(req.body);
    sessionHandler.emitEvent(req, novaTarefa, "adicionado");
});

router.post("/replace", (req, res) => {
    service.replaceTarefas(req.body);
    sessionHandler.emitEvent(req, service.getTarefas(), "replace");

    res.status(200).json("Tarefas sincronizadas");
});

module.exports = router;