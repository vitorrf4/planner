const express = require("express");
const Tarefa = require("../models/tarefa");
const tarefas = require("../models/tarefaDB")
// const tarefas = new tarefasDb();
const router = express.Router();

router.get("/", (req, res) => {
    res.json(tarefas.getTarefas());
});

router.post("/", (req, res) => {
    const body = req.body;
    const data = new Date(Date.parse(body.dataFinal));
    const tarefa = new Tarefa(body.titulo, body.descricao, data)
    tarefas.adicionarTarefa(tarefa);

    res.status(201).json(tarefa);
});

router.post("/replace", (req, res) => {
    tarefas.replaceTarefas(req.body);

    res.status(201).json("Success");
});

module.exports = router;
