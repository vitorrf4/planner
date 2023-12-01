const Tarefa = class {
    status;

    constructor(id, titulo, descricao, dataFinal) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataFinal = dataFinal;

        this.status = "PENDENTE";
    }
}

module.exports = Tarefa