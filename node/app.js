// imports
const express = require('express');
const cors = require('cors');
const ngrok = require("@ngrok/ngrok");
require("dotenv").config();

// configuracao do express
const app = express();
app.use(cors());
app.use(express.json());

const PORT = 3000;

//ngrok
async function iniciaNgrok() {
    const ngrokConfig = {
        addr: 3000,
        authtoken: process.env.AUTH_TOKEN,
        domain: "stable-phoenix-worthy.ngrok-free.app"
    }

    return await ngrok.connect(ngrokConfig);
}

// começa o programa
app.listen(PORT, () => {
    console.log(`Servidor iniciado no http://localhost:${PORT}`)
    iniciaNgrok().then(l => console.log(`Ngrok iniciado na URL ${l.url()}`));
});

// middleware de debug
app.all("*", (req, res, next) => {
    console.log(`${req.method} ${req.url}`);
    if (req.method === "POST") console.log(req.body);

    next();
});

// rotas
app.get("/", (req, res) => res.send("API está funcionando"));
app.get('/chat', eventsHandler);
app.post('/message', adicionarMensagem);

app.all("*", (req, res) => {
    res.status(404);
    res.send(`${req.method} ${req.url} não é um endpoint válido`)
});

// funcoes
let subscribers = [];
let messages = [];

function eventsHandler(req, res) {
    const headers = {
        'Content-Type': 'text/event-stream',
        'Connection': 'keep-alive',
        'Cache-Control': 'no-cache'
    };
    res.writeHead(200, headers);

    const data = `data: ${JSON.stringify(messages)}\n\n`;
    res.write(data);

    const idCliente = Date.now();
    const novoCliente = {
        id: idCliente,
        response: res
    };

    subscribers.push(novoCliente);

    req.on('close', () => {
        console.log(`${idCliente} fechou a conexão`);
        subscribers = subscribers.filter(client => client.id !== idCliente);
    });
}

function enviarEventosAosInscritos(newFact) {
    subscribers.forEach(client => client.response.write(`data: ${JSON.stringify(newFact)}\n\n`))
}

function adicionarMensagem(req, res) {
    const novaMensagem = req.body.message;
    messages.push(novaMensagem);
    res.json({"message" : novaMensagem});

    return enviarEventosAosInscritos(novaMensagem);
}
