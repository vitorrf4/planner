const Tarefa = require("../models/tarefa");
const tarefas = require("../models/tarefaDB");


class TarefaService {
    getTarefas() {
        return tarefas.getTarefas();
    }

    addTarefa(req, res, next) {
        const body = req.body;
        const data = new Date(Date.parse(body.dataFinal));
        const novaTarefa = new Tarefa(body.titulo, body.descricao, data);

        tarefas.adicionarTarefa(novaTarefa);

        res.locals.tarefa = novaTarefa;
        next();
    }

    replaceTarefas(req ,res, next) {
        const novasTarefas = req.body;
        tarefas.replaceTarefas(novasTarefas);

        next();
    }
}

module.exports = new TarefaService();
