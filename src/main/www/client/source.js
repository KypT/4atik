var ws;
var $login;
var $chatik;
var $userlist;
var $history;
var username = 'anonymous';
var $message;

var sendLoginMessage = function() { sendMessage({"command":"connect", "message": ""}) };
var sendChatikMessage = function(msg) { sendMessage({"command":"send", "message": msg})};
var askForUserlist = function() { sendMessage({"command":"getUserlist", "message": ""})};

function sendMessage(msg)
{
    msg.sender = username;
    ws.send(JSON.stringify(msg));
}

function onLogIn()
{
    $login.fadeTo('slow', 0);
    $chatik.fadeTo('slow', 100);
}

function onMessage(event)
{
    var msg = JSON.parse(event.data);

    if (msg.command == "connect")
    {
        onLogIn();
        displayMessage(msg);
    }

    if (msg.command == "send")
    {
        displayMessage(msg);
        //$history.animate({'scrollTop' : $history.height()}, "slow")
        $history[0].scrollTop = $history[0].scrollHeight;
    }

    if (msg.command == "getUserlist")
        addToUserlist(msg.message);

    if (msg.command == "userConnected")
        addToUserlist(msg.message);

    if (msg.command == "userDisconnected")
        removeFromUserlist(msg.message);
}

function displayMessage(msg)
{
    message =
        "<li class = 'list-group-item'>" +
            "<h4>" +
                "<small>" +
                    msg.received +
                "</small>" +
                "<span class = 'User'>" +
                    msg.sender +
                "</span>" +
            "</h4>" +
            msg.message +
        "</li>";
    msg = $(message);
    $history.append(msg);

}

function addToUserlist(username)
{
    if (document.getElementById(username) != null)
        return;

    var user = document.createElement("button");
    user.className = "btn btn-success";
    user.type = "button";

    user.id = username;
    user.appendChild(document.createTextNode(username));
    $userlist[0].appendChild(user);
}

function removeFromUserlist(username)
{
    var user = document.getElementById(username);
    user.parentNode.removeChild(user);
}

$(function()
{
    ws = new WebSocket('ws://localhost:8025');
    ws.onopen = askForUserlist;
    ws.onmessage = onMessage;

    $chatik = $('#chatik');
    $login = $('#login');
    $history = $('#history');
    $userlist = $('#userlist');

    $message = $('#messageInput')[0];
    $chatik.fadeTo(0, 0);

    $login.css('visibility', 'visible').fadeTo('slow', 100);
    $("#loginButton").click(function()
    {
        username = $('#user')[0].value.trim();
        if (username != '')
        {
            sendLoginMessage(username);
            askForUserlist();
        }
    });

    $('#sendButton').click(function()
    {
        $msg = $message.value.trim();
        if ($msg != '')
            sendChatikMessage($msg);
        $message.value = '';
    });

   $($message).keydown(function (e)
   {
     if (e.ctrlKey && e.keyCode == 13)
        $('#sendButton').click();
   });

    $(window).on("beforeunload", function()
    {
        ws.close();
    })
});