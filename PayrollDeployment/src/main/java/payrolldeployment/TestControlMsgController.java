package payrolldeployment;

import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.application.tasks.GenericExecutionTask;
import io.ciera.runtime.summit.application.tasks.HaltExecutionTask;
import io.ciera.runtime.summit.classes.IModelInstance;
import io.ciera.runtime.summit.components.Component;
import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.XtumlException;
import io.ciera.runtime.summit.interfaces.IMessage;
import io.ciera.runtime.summit.interfaces.Message;
import io.ciera.runtime.summit.util.LOG;
import io.ciera.runtime.summit.util.impl.LOGImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import interfaces.timecontrol.AdvanceTimeMsg;         // AdvanceTime message class
import interfaces.timecontrol.SetTimeMsg;             // SetTime message class
import interfaces.timecontrol.CurrentDateTimeMsg;     // Current date and time message class
import interfaces.timecontrol.GetTimeMsg;             // GetTime message class
import payrolldeployment.TestControl;            // Shell component

// The Spring framework arranges for an instance of this class to be
// created, passing an instance of SimpMessagingTemplate as an argument,
// which enables messages to be sent to JavaScript clients.
@Controller
public class TestControlMsgController {
	private static TestControlMsgController singleton;
	
	private SimpMessagingTemplate template;  
	
	@Autowired
	public TestControlMsgController( SimpMessagingTemplate template ) {
		singleton = this;
		this.template = template;
	}
	
	public static TestControlMsgController Singleton() {
		return singleton;
	}
	
	// Begin outgoing (from this component) messages.
	// Each of the following methods is invoked when the (JavaScript) client sends 
	// a message to the corresponding message-broker topic, "/app/<messageName>".
	// For example, when the JavaScript client sends a message to "/app/AdvanceTime",
	// the method annoated with @MessageMapping( "/AdvanceTime" ) is invoked, and
	// an instance of the message is passed to it as a parameter.
    @MessageMapping( "/AdvanceTime" )
    public void AdvanceTime( AdvanceTimeMsg message ) throws Exception {
    	int hours, minutes;
    	try {
    	  hours = Integer.parseInt( message.getHours() );
    	  minutes = Integer.parseInt( message.getMinutes() );
    	  TestControl.Singleton().AdvanceTime( hours, minutes );	
    	}
    	catch ( Exception e ) {
      	  System.out.printf( "Exception, %s, in AdvanceTime()\n", e );    			
    	}
    }
    
    @MessageMapping( "/SetTime" )
    public void SetTime( SetTimeMsg message ) {
        int year = Integer.parseInt( message.getYear() );
        int month = Integer.parseInt( message.getMonth() );
        int day = Integer.parseInt( message.getDay() );
        int hour = Integer.parseInt( message.getHour() );
        int minute = Integer.parseInt( message.getMinute() );
        TestControl.Singleton().SetTime( year, month, day, hour, minute );
    }

    @MessageMapping( "/GetTime" )
    public void GetTime( GetTimeMsg message ) {
    	TestControl.Singleton().GetTime();
    }


    // End of outgoing messages.
    
    // Incoming (to this component) messages.
    // The following methods forward incoming messages to the (JavaScript) client which
    // subscribes to a location-specific message-broker topic.
    
    public void SendCurrentDateTimeMessage ( String CurrentDateTime ) throws Exception {
    	CurrentDateTimeMsg msg = new CurrentDateTimeMsg( "CurrentDateTime", CurrentDateTime );
        String topic = "/topic/TestControl";
        this.template.convertAndSend( topic, msg );
    }
    // End of incoming messages.
}
