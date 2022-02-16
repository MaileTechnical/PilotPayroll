var stompClient = null;

var vm = new Vue({
	el: '#main-content',
	data: {
	    ShowMsgs: false,
	    AvailableDisabled: true,
	    PayrollRequestDisabled: true,
	    CancelDisabled: true,
	    UpdateDisabled: true,
	    ApproveDisabled: true,
	    SubmitDisabled: true,
		department: "",
	    notification: ""
	}
})

function initialize() {
        vm.AvailableDisabled = true;
	    vm.PayrollRequestDisabled = true;
	    vm.CancelDisabled = true;
	    vm.UpdateDisabled = true;
	    vm.ApproveDisabled = true;
	    vm.SubmitDisabled = true;
		vm.department = "";
	    vm.notification = "";
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        $("#payrollentries").hide();
    }
    else {
        $("#conversation").hide();
        initialize();
    }
    $("#replies").html("");
    $("#entries").html("");
}

// When connecting, subscribe to a topic to receive
// messages sent from the server.
function connect() {
    var socket = new SockJS('/PilotPayroll-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/HRuser/', function (message) {
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

function sendAvailablePayrolls() {
    stompClient.send( "/app/AvailablePayrolls", {}, 
      JSON.stringify( {'messageName': "AvailablePayrolls" } ) );
}

function sendRetrievePayrollForReview() {
    stompClient.send( "/app/RetrievePayrollForReview", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
}

function sendSubmitPayrollApproval() {
    vm.AvailableDisabled = true;
    vm.UpdateDisabled = true;
    vm.ApproveDisabled = true;
    stompClient.send( "/app/SubmitPayrollApproval", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
      $("#payrollentries").hide();
}

function sendUpdates() {
    // send a SubmitItemHold message for any changed hold status
    // then indicate all updates sent
    stompClient.send("/app/UpdatesSent", {}, 
      JSON.stringify( {'department': $("#department").val(), 'count': "0" } ) );
}

function sendSubmitToFinance() {
    stompClient.send("/app/SubmitToFinance", {}, 
      JSON.stringify( {'department': $("#department").val()} ) );
      vm.SubmitDisabled = true;
      vm.UpdateDisabled = true;
      vm.PayrollRequestDisabled = true;
}

// Support functions for incoming message handling

function availablePayroll ( payload ) {
    var dept = JSON.parse( payload ).Department;
    $("#payrolls").append("<tr><td>" + dept + "</td></tr>");
    $("#payrolldepts").show();
 }

function payeeDataMsg( payload ) {
    // accept a payee data message - now carries set of payment elements
    var ename = JSON.parse( payload ).EmployeeFirstName + " " + JSON.parse( payload ).EmployeeLastName;
    var payments = JSON.parse( payload ).Payments;

    var paymentset = JSON.parse( payments ).PaymentSet;
    var pentry = "";
    var pelemt = "";
    var sep = "    ";
    for (var i=0;i<paymentset.length;i++) {
      pelemt = JSON.parse( paymentset[i] ).label + " " + JSON.parse( paymentset[i] ).amount;
      pentry = pentry + sep + pelemt;
      sep = ";   ";
    } 
    pentry = ename + ":  " + pentry;
    $("#entries").append("<tr><td>" + pentry + "</td></tr>");
    $("#payrollentries").show();
}

// Display a message received from the server.

var msgs = { 'imminent': "Payroll generation imminent for department ",
		     'generating': "Draft payroll being generated for department ",
		     'generated': "Draft payroll has been generated for department ",
		     'delivered': "Payroll has been delivered for ",
		     'reviewed': "Payroll has been reviewed for department ",
		     'unapproved': "Payroll has NOT been approved for department ",
		     'approved': "Payroll has been approved for department ",
		     'submitted': "Payroll has been submitted for department ",
		     'accepted': "Payroll has been accepted by Finance ",
             'overdue': "Payroll submission overdue for department " };

function showReply( message ) {
    $("#replies").append("<tr><td>" + message + "</td></tr>");
    var messageName = JSON.parse( message.body ).messageName;
    var payload = JSON.parse( message.body ).payload;
    if ( messageName == "Notification" ) {
        var msgident = JSON.parse( payload ).Ident;
        var content = JSON.parse( payload ).Content;
        vm.notification = msgs[ msgident ] + content;
        if ( msgident == "generated" ) {
            vm.AvailableDisabled = false;
        }
        if ( msgident == "approved" ) {
            vm.AvailableDisabled = true;
            vm.UpdateDisabled = true;
            vm.ApproveDisabled = true;
            vm.SubmitDisabled = false;
        }
        if ( msgident == "unapproved" ) {
            vm.AvailableDisabled = false;
            vm.UpdateDisabled = false;
            vm.ApproveDisabled = false;
            vm.SubmitDisabled = true;
        }
    } else if ( messageName == "PayrollAvailable" ) {
           availablePayroll( payload );
    } else if ( messageName == "PayeeData" ) {
           payeeDataMsg( payload );
    } else if ( messageName == "DataSent" ) {
           var dataident = JSON.parse( payload ).Ident;
           if ( dataident == "available" ) {
               if ( JSON.parse( payload ).Count != "0" ) {
                   vm.PayrollRequestDisabled = false;
               }
           }   
           if ( dataident == "payroll" ) {
               vm.PayrollRequestDisabled = true;
               vm.UpdateDisabled = false;
               vm.ApproveDisabled = false;
               $("#payrollentries").show();
           }
            if ( dataident == "approved" ) {
               vm.UpdateDisabled = true;
               vm.ApproveDisabled = true;
           }
    }
}

// Map buttons to functions.
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#available" ).click(function() { sendAvailablePayrolls(); });
    $( "#payroll" ).click(function() { sendRetrievePayrollForReview(); });
    $( "#cancel" ).click(function() { cancel(); });
    $( "#update" ).click(function() { sendUpdates(); });
    $( "#approve" ).click(function() { sendSubmitPayrollApproval(); });
    $( "#finance" ).click(function() { sendSubmitToFinance(); });
    $( "#msgdisplay" ).click(function() { toggleMsgs(); });
});