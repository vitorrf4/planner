const Tarefa = require("../models/tarefa");
const tarefas = require("../models/tarefaDB");


class TarefaService {
    getTarefas() {
        return tarefas.getTarefas();
    }

    addTarefa(body) {
        const data = new Date(Date.parse(body.dataFinal));
        const novaTarefa = new Tarefa(body.titulo, body.descricao, data);

        tarefas.adicionarTarefa(novaTarefa);

        return novaTarefa;
    }

    replaceTarefas(novasTarefas) {
        tarefas.replaceTarefas(novasTarefas);
    }
}

module.exports = new TarefaService();
