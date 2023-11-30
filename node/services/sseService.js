const sse = require("better-sse");
const channel = sse.createChannel();
const tarefaService = require("../services/tarefaService")

class SseService {
    constructor(channel) {
        this.channel = channel;
        this.appSession = null;
        this.webSession = null;
    }

    async connect(req, res, tarefas) {
        if (this.isRequestFromWeb(req)) {
            this.webSession = await sse.createSession(req, res);
            this.channel.register(this.webSession);
            this.webSession.push(tarefas, "replace");

            return;
        }

        this.appSession = await sse.createSession(req, res);
        this.channel.register(this.appSession);
    }

    emitEvent(request, data, eventName) {
        if (this.isRequestFromWeb(request)) {
            this.channel.broadcast(data, eventName);
            return;
        }

        this.webSession.push(data, eventName);
    }

    isRequestFromWeb(req) {
        return req.headers["user-agent"].includes("Mozilla");
    }

    adicionarTarefa(req) {
        const tarefa = tarefaService.criarTarefa(req.body);

        if (this.isRequestFromWeb(req)) {
            this.appSession.push(tarefa, "adicionar");

            return;
        }

        tarefaService.addTarefa(tarefa);

        this.webSession.push(tarefa, "adicionado");
    }

    excluirTarefa(id) {
        tarefaService.excluirTarefa(id);
    }

    sendTarefasToWeb() {
        this.webSession.push(tarefaService.getTarefas(), "replace");
    }
}

channel.on("session-registered", () => {
    console.log(`Session registered ${channel.sessionCount}`);
});

channel.on("session-deregistered", () => {
    console.log(`Session deregistered ${channel.sessionCount}`);
});

module.exports = new SseService(channel);
