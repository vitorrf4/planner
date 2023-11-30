const express = require("express");
const sse = require("better-sse")
const service = require("../services/tarefaService")

const router = express.Router();

const channel = sse.createChannel();
let webSession;
let appSession;

router.get("/web/connect", async (req, res) => {
    webSession = await sse.createSession(req, res);
    channel.register(webSession);

    webSession.push(service.getTarefas(), "replace");
});

router.get("/app/connect", async (req, res) => {
    appSession = await sse.createSession(req, res);
    channel.register(appSession);
});

router.post("/web/add-tarefa", service.addTarefa, (req, res) => {
    channel.broadcast(res.locals.tarefa, "adicionado");
});

router.post("/app/add-tarefa", service.addTarefa, (req, res) => {
    webSession.push(res.locals.tarefa, "adicionado");
});

router.post("/app/replace", service.replaceTarefas, (req, res) => {
    webSession.push(service.getTarefas(), "replace");

    res.status(200).json("Tarefas sincronizadas");
});

channel.on("session-registered", () => {
    console.log(`New session registered ${channel.sessionCount}`);
});
channel.on("session-deregistered", () => {
    console.log(`Session deregistered ${channel.sessionCount}`);
});

module.exports = router;