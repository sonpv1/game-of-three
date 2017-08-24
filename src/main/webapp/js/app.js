var ws = null;
var host = document.location.hostname + ":" + document.location.port;
console.log(host);

function setConnected(connected) {
    document.getElementById('connect').disabled                 = connected;
    document.getElementById('disconnect').disabled              = !connected;
    document.getElementById('from').disabled                    = connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML               = '';
}

function connect() {
    var from = document.getElementById('from').value;
    ws = new WebSocket("ws://" + host + "/join/" + from);
    ws.onopen = function () {
        setConnected(true);
        console.log('Connected: ' + from);
    };
    ws.onmessage = function (evt) {
        var received_msg = evt.data;
        showMessageOutput(received_msg);
    };

    ws.onclose = function () {
        showMessageOutput("Connection is closed...");
        setConnected(false);
    };
}

function disconnect() {
    if (ws != null) {
        ws.disconnect();
    }
    setConnected(false);
    showMessageOutput("Disconnected");
}

function sendMessage(txt) {
    ws.send(txt);
    showMessageOutput("YOUR CHOOSE: " + txt);
}

function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    response.textContent = response.textContent + "\n" + messageOutput;
    response.scrollTop = response.scrollHeight;
}