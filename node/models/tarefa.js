const Tarefa = class {
    constructor(id, titulo, descricao, dataFinal) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataFinal = dataFinal;
    }
}

module.exports = Tarefa