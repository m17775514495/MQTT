//用户id
var clientUserId = $("#userId").val();
//房间名
var roomName = $("#roomName").val();

var hostname = 'ip',
    port = 9001, //websocket 端口
    clientId = 'client-count-' +roomName+ clientUserId,
    timeout = 5,
    keepAlive = 50,
    cleanSession = false,
    ssl = false,
    userName = 'test',//mqtt 账户
    password = 'test',//mqtt 密码
    topic = "test/count/" + roomName;//主题
client = new Paho.MQTT.Client(hostname, port, clientId);
//遗嘱
willMessage = new Paho.MQTT.Message("-1");
willMessage.destinationName = topic;
//建立客户端实例
var options = {
    invocationContext: {
        host: hostname,
        port: port,
        path: client.path,
        clientId: clientId
    },
    willMessage:willMessage,
    timeout: timeout,
    keepAliveInterval: keepAlive,
    cleanSession: cleanSession,
    useSSL: ssl,
    userName: userName,
    password: password,
    onSuccess: onConnect,
    onFailure: function (e) {
        console.log(e);
    }
};

client.connect(options);


//连接服务器并注册连接成功处理事件
function onConnect() {
    client.subscribe(topic);
    add();
}

client.onConnectionLost = onConnectionLost;

//注册连接断开处理事件
client.onMessageArrived = onMessageArrived;

//注册消息接收处理事件
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
        console.log("连接已断开");
    }
}

/**收到消息*/
function onMessageArrived(message) {
    var value = message.payloadString;
    if(Number(value)!=-1){
        $('#count', window.parent.document).html(value);
    }
}

/**发送消息*/
function send() {
    message = new Paho.MQTT.Message("");
    message.destinationName = topic;
    client.send(message);
}

function exit() {
    //发送disconnect报文后不能再通过该连接发送任何控制报文
    client.disconnect();
    window.location.href=document.referrer;
}

// 连接失败的回调
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

function add() {
    $.ajax({
        type: "POST",
        url: "/add-count",
        data:{"topic":topic,"roomName":roomName},
        async:false,
        success: function(data) {
        }
    });
}

