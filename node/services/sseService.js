const sse = require("better-sse");
const tarefaService = require("../services/tarefaService")

class SseService {
    constructor() {
        this.channel = sse.createChannel();
        this.appSession = null;
        this.webSession = null;


        this.channel.on("session-registered", () => {
            console.log(`Session registered ${this.channel.sessionCount}`);
        });

        this.channel.on("session-deregistered", () => {
            this.emitirEstadoDoApp()
        });
    }

    emitirEstadoDoApp() {
        if (this.appSession && this.webSession && this.appSession.isConnected) {
            this.webSession.push("aberta", "conexao-app");
            return;
        }

        if (this.webSession) {
            this.webSession.push("fechada", "conexao-app");
        }
    }

    async connect(req, res) {
        if (this.isRequestFromWeb(req)) {
            this.webSession = await sse.createSession(req, res);
            this.channel.register(this.webSession);

            this.sendTarefasToWeb();

            this.emitirEstadoDoApp();
            return;
        }

        this.appSession = await sse.createSession(req, res);
        this.channel.register(this.appSession);
        this.emitirEstadoDoApp();
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

    excluirTarefa(req) {
        const id = req.params.id;

        if (this.isRequestFromWeb(req)) {
            this.appSession.push(id, "excluir");
            return;
        }

        tarefaService.excluirTarefa(id);
    }

    atualizarTarefa(req) {
        const tarefa = req.body;

        if (this.isRequestFromWeb(req)) {
            this.appSession.push(tarefa, "atualizar");
            return;
        }

        tarefaService.atualizarTarefa(tarefa);
    }

    replaceTarefas(req) {
        tarefaService.replaceTarefas(req.body);
        this.webSession.push(tarefaService.getTarefas(), "replace");
    }

    sendTarefasToWeb() {
        this.webSession.push(tarefaService.getTarefas(), "replace");
    }
}

module.exports = new SseService();
