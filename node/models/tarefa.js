const Tarefa = class {
    constructor(titulo, descricao, dataFinal) {
        this.id = 0;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataFinal = dataFinal;
    }
}

module.exports = Tarefa