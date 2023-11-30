const express = require("express");
const tarefaService = require("../services/tarefaService")
const sseService = require("../services/sseService");

const router = express.Router();

router.get("/connect", async (req, res) => {
    try {
        await sseService.connect(req, res, tarefaService.getTarefas());
    } catch (e) {
        console.log("Erro:", e);
    }
});

router.post("/adicionar", async (req, res) => {
    try {
        sseService.adicionarTarefa(req);

        res.status(200).json("Adicionado");
    } catch(e) {
        console.log("Erro:", e);
    }
});

router.post("/replace", (req, res) => {
    try {
        tarefaService.replaceTarefas(req.body);
        sseService.emitEvent(req, tarefaService.getTarefas(), "replace");

        res.status(200).json("Tarefas sincronizadas");
    } catch (e) {
        console.log("Erro:", e);
    }
});

module.exports = router;