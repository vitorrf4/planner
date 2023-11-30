// imports
const express = require('express');
const cors = require('cors');
const ngrok = require("./ngrok");
const sseController = require("./controllers/sseController");

// configuracao do express
const app = express();
app.use(cors());
app.use(express.json());

const PORT = 3000;

// começa o programa
app.listen(PORT, () => {
    console.log(`Servidor iniciado em http://localhost:${PORT}`);
    ngrok.then(r => console.log(`Ngrok iniciado na url ${r.url()}`));
});

// middleware de debug
app.all("*", (req, res, next) => {
    console.log(`${req.method} ${req.url}`);

    if (req.method === "POST")
        console.log(req.body);


    next();
});

// rotas
app.use(sseController);

// tratamento de rota nao existente
app.all("*", (req, res) => {
    res.status(404);
    res.send(`${req.method} ${req.url} não é um endpoint válido`)
});

