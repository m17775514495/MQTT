//用户id
var clientUserId = $("#userId").val();
//用户名
var clientUserName = $("#userName").val();
//房间名
var roomName = $("#roomName").val();

var hostname = 'ip',
    port = 9001, //websocket 端口
    clientId = 'client-' +roomName+ clientUserId,
    timeout = 5,
    keepAlive = 50,
    cleanSession = false,
    ssl = false,
    userName = 'test',//mqtt 账户
    password = 'test',//mqtt 密码
    topic = 'test/enter/';//主题
client = new Paho.MQTT.Client(hostname, port, clientId);
//遗嘱
willMessage = new Paho.MQTT.Message("-1");
var destinationName="test/count/" + roomName;
willMessage.destinationName = destinationName;
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

    topic=topic+roomName;
    client.subscribe(topic);

    message = new Paho.MQTT.Message(clientUserName+"加入房间");
    message.destinationName = topic;
    client.send(message);

}

client.onConnectionLost = onConnectionLost;

//注册连接断开处理事件
client.onMessageArrived = onMessageArrived;

//注册消息接收处理事件
function onConnectionLost(responseObject) {
    console.log(responseObject);
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
        console.log("连接已断开");
        subtract(destinationName);
    }
}

/**收到消息*/
function onMessageArrived(message) {
    $("#text").append("<p>" + message.payloadString + "</p>")
}

/**发送消息*/
function send() {
    var s = document.getElementById("msg").value;
    if (s) {
        s=clientUserName+":"+s;
        message = new Paho.MQTT.Message(s);
        message.destinationName = topic;
        client.send(message);
    }
}

function exit() {
    message = new Paho.MQTT.Message(clientUserName+"离开房间");
    message.destinationName = topic;
    client.send(message);
    //发送disconnect报文后不能再通过该连接发送任何控制报文
    client.disconnect();
    window.location.href=document.referrer;
    subtract(destinationName);
}

// 连接失败的回调
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
        console.log("onConnectionLost:" + responseObject.errorMessage);
    }
}

//减少房间人数
function subtract(destinationName) {
    $.ajax({
        type: "POST",
        url: "/subtract-count",
        data:{"topic":destinationName,"roomName":roomName},
        async:false,
        success: function(data) {
        }
    });
}