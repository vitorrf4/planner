const ngrok = require("@ngrok/ngrok");
require("dotenv").config();

async function iniciaNgrok() {
    const ngrokConfig = {
        addr: 3000,
        authtoken: process.env.AUTH_TOKEN,
        domain: "stable-phoenix-worthy.ngrok-free.app"
    }

    return await ngrok.connect(ngrokConfig);
}

module.exports = iniciaNgrok();