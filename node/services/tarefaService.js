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

    excluirTarefa(id) {
        const indexDelete = this.tarefas.findIndex(t => t.id === id);
        this.tarefas.splice(indexDelete, 1);
    }

    atualizarTarefa(tarefa) {
        const indexTarefa = this.tarefas.findIndex(t => t.id === tarefa.id);
        this.tarefas[indexTarefa] = tarefa;
    }
}

module.exports = new TarefaService();
