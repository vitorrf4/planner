const Tarefa = require("../models/tarefa");

class TarefaService {
    tarefas = [];

    getTarefas() {
        return this.tarefas;
    }

    criarTarefa(body) {
        const data = new Date(Date.parse(body.dataFinal));

        return new Tarefa(body.id, body.titulo, body.descricao, data);
    }

    addTarefa(tarefa) {
        this.tarefas.push(tarefa);
    }

    replaceTarefas(novasTarefas) {
        this.tarefas = novasTarefas;
    }
}

module.exports = new TarefaService();
