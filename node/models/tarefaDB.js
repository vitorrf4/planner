class Tarefas {
    tarefas = [];

    getTarefas() {
        return this.tarefas;
    }

    adicionarTarefa(tarefa) {
        this.tarefas.push(tarefa);
    }

    replaceTarefas(tarefas) {
        this.tarefas = tarefas;
    }
}

module.exports = new Tarefas();