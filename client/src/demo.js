const atmosphere = require('atmosphere.js');

let _socket = null;
let _uuid = 0;

// takes store as its argument (destructured -> dispatch and getState)
const websocketConfig = {
    contentType: 'text/plain',
    transport: 'websocket',
    url: "ws://localhost:8080/websocket/",
    logLevel: 'debug',
    reconnectInterval: 1000,
    maxReconnectOnClose: 45,
    handleOnlineOffline: true,
    reconnectOnServerError: true,
    executeCallbackBeforeReconnect: true,
    heartbeat: { server: 60, client: 60 },
    uuid: _uuid,
    onOpen: response => {
        _uuid = response.request.uuid;
        console.log("open", response);
    },
    onClose: response => {
        console.log("close", response);
    },
    onMessage: response => {
        console.log("message", response);
    },
    onError: response => {
        console.log("error", response);
    },
    onReconnect: (request, response) => {
        console.log("reconnect", request, response);
    },
    onReopen: (request, response) => {
        console.log("reopen", request, response);
    },
    onClientTimeout: response => {
        console.log("client timeout", response);
    },
    onTransportFailure: response => {
        console.log("transport failure", response);
    },
    onFailureToReconnect: response => {
        console.log("failure to reconnect", response);``
    },
    onOpenAfterResume: response => {
        console.log("open after resume", response);
    }
};

const _cleanupSocket = () => {
    if (_socket) {
        // cleanup old socket and make _socket ref null so that it doesn't pick up errors from previous request
        let cleanupSocket = _socket;
        _socket = null;
        atmosphere.unsubscribe();
        cleanupSocket.close();
        cleanupSocket = null;
    }
};

// push messages
const push = (data) => {
    if (!_socket) {
        console.warn("not started");
        return;
    }
    _socket.push(JSON.stringify(data));
};

const stop = () => {
    if (!_socket) {
        console.warn("not started");
        return;
    }
    _cleanupSocket();
};

const start = () => {
    _cleanupSocket();
    const request = Object.assign(
        new atmosphere.AtmosphereRequest(),
        websocketConfig
    );
    _socket = atmosphere.subscribe(request);
};

start();
