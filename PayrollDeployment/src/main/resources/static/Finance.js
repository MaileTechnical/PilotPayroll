var stompClient = null;

var vm = new Vue({
	el: '#main-content',
	data: {
	    ShowMsgs: false,
	    AcceptDisabled: true,
	    RejectDisabled: true,
		department: "",
		period: "",
	    notification: ""
	}
})

function initialize() {
	    vm.AcceptDisabled = true;
	    vm.RejectDisabled = true;
		vm.department = "";
		vm.period = "";
	    vm.notification = "";
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
        initialize();
    }
    $("#replies").html("");
}

// When connecting, subscribe to a topic to receive
// messages sent from the server.
function connect() {
    var socket = new SockJS('/PayrollFinance-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/PayrollFinance/Payroll', function (message) {
            showReply( message );
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function toggleMsgs() {
    vm.ShowMsgs = !vm.ShowMsgs;
}

// Client-to-server messages.

function sendAcceptPayroll() {
    stompClient.send( "/app/AcceptPayroll", {}, 
      JSON.stringify( {'department': vm.department, 'payPeriodStart': vm.period} ) );
      vm.notification = "";
      vm.AcceptDisabled = true;
}


// Support functions for incoming message handling


// Display a message received from the server.


function showReply( message ) {
    $("#replies").append("<tr><td>" + message + "</td></tr>");
    var messageName = JSON.parse( message.body ).messageName;
    var payload = JSON.parse( message.body ).payload;
    if ( messageName == "SubmitPayroll" ) {
        vm.notification = "Payroll submitted for Finance acceptance";
        vm.AcceptDisabled = false;
        vm.department = JSON.parse( payload ).Department;
        vm.period = JSON.parse( payload ).PayPeriodStart;
    }
}

// Map buttons to functions.
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#accept" ).click(function() { sendAcceptPayroll(); });
    $( "#msgdisplay" ).click(function() { toggleMsgs(); });
});
