package payrolldeployment.payrollmgmt;


import interfaces.IHumanResources;

import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.XtumlException;
import io.ciera.runtime.summit.interfaces.IMessage;
import io.ciera.runtime.summit.interfaces.Message;
import io.ciera.runtime.summit.interfaces.IPort;
import io.ciera.runtime.summit.interfaces.Port;
import io.ciera.runtime.summit.types.BooleanUtil;
import io.ciera.runtime.summit.types.IntegerUtil;
import io.ciera.runtime.summit.types.RealUtil;
import io.ciera.runtime.summit.types.StringUtil;

import java.util.Iterator;

import payrolldeployment.PayrollMgmt;
import payrolldeployment.payrollmgmt.clientdata.PaymentSet;
import payrolldeployment.payrollmgmt.clientdata.impl.PaymentSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.Department;
import payrolldeployment.payrollmgmt.payrollmgmt.Payroll;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.DepartmentImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollImpl;

//import payrolldeployment.HRuserMsgController;
import payrolldeployment.SpringMsg;
import payrolldeployment.PayrollDeploymentApplication;

import org.json.JSONObject;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import interfaces.humanresources.AvailablePayrollsMsg;

@Controller
public class PayrollMgmtUSER extends Port<PayrollMgmt> implements IHumanResources {
	private SimpMessagingTemplate template;
	private static PayrollMgmtUSER singleton;
	
	public static PayrollMgmtUSER Singleton() {
		return singleton;
	}
	@Autowired
    public PayrollMgmtUSER( SimpMessagingTemplate template ) {
		super ( PayrollMgmt.Singleton(), null );
		singleton = this;
        this.template = template;
  	    System.out.printf( "Port created by Spring.. \n"); 
	}

    
    public PayrollMgmtUSER( PayrollMgmt context, IPort<?> peer ) {
        super( context, peer );
   	    System.out.printf( "Port created.. \n"); 
   	    int value = 1/0;
   }

    // inbound messages
    public void SubmitItemApproval( final String p_Department,  final int p_EmployeeID,  final String p_PaymentLabel ) throws XtumlException {

        Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
        if ( !dept.isEmpty() ) {
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                payroll.ApproveItem( p_EmployeeID, p_PaymentLabel );
            }
            else {
                context().LOG().LogFailure( "Payroll not found when updating item approval" );
            }
        }
    }

    public void SubmitPayApproval( final String p_Department,  final int p_EmployeeID,  final String p_PaymentLabel,  final double p_PaymentAmount ) throws XtumlException {

    }

    public void UpdatesSent( final String p_Department,  final int p_Count ) throws XtumlException {

        Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
  	    System.out.printf( "Updates_sent received in Port... %s\n", p_Department );
        if ( !dept.isEmpty() ) {
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                context().generate(new PayrollImpl.Reviewed(getRunContext(), context().getId()).to(payroll));
            }
            else {
                context().LOG().LogFailure( "Payroll not found when updating payroll review status" );
            }
        }
        else {
            context().LOG().LogFailure( "Department not found when updating payroll review status" );
        }
    }

    public void SubmitItemHold( final String p_Department,  final int p_EmployeeID,  final String p_PaymentLabel,  final boolean p_HoldStatus ) throws XtumlException {

        Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
        if ( !dept.isEmpty() ) {
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                payroll.UpdateItemHold( p_EmployeeID, p_PaymentLabel, p_HoldStatus );
            }
            else {
                context().LOG().LogFailure( "Payroll not found when updating item approval" );
            }
        }
    }

    public void Accepted( final String p_Department ) throws XtumlException {

    }

    public void RetrievePayrollForReview( final String p_Department,  final boolean p_HoldsOnly ) throws XtumlException {

  	    System.out.printf( "Retrieve payroll in Port... %s\n", p_Department );
       Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
        if ( !dept.isEmpty() ) {
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                payroll.SendPayrollForReview( p_HoldsOnly );
            }
        }
    }

    public void SubmitItemUpdate( final String p_Department,  final int p_EmployeeID,  final String p_PaymentLabel,  final double p_PaymentAmount ) throws XtumlException {

    }

    public void AvailablePayrolls() throws XtumlException {
  	    System.out.printf( "Available payrolls in Port... \n" );
        context().AvailablePayrolls();
    }
    public void SubmitPayrollApproval( final String p_Department ) throws XtumlException {

  	    System.out.printf( "Approval received in Port... %s\n", p_Department );
        Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
        if ( !dept.isEmpty() ) {
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                payroll.AssessForApproval();
            }
            else {
                context().LOG().LogFailure( "Payroll not found for approval" );
            }
        }
    }

    public void SubmitPayHold( final String p_Department,  final int p_EmployeeID,  final String p_PaymentLabel,  final double p_PaymentAmount ) throws XtumlException {

    }

    public void SubmitToFinance( final String p_Department ) throws XtumlException {

        Department dept = context().Department_instances().anyWhere(selected -> StringUtil.equality(((Department)selected).getName(), p_Department));
        if ( dept.isEmpty() ) {
            context().LOG().LogFailure( "Department not found for final submission" );
            return;
        }
        Payroll payroll = dept.R34_has_under_review_Payroll();
        payroll.AssessForSubmittal();
    }

    
    // outbound messages
    public void Notification( final String p_Ident,  final String p_Content ) throws XtumlException {
        if ( true ) send(new IHumanResources.Notification(p_Ident, p_Content));
        else { 
        };
    }
    public void PayeeData( final String p_Department,  final int p_EmployeeID,  final String p_EmployeeFirstName,  final String p_EmployeeLastName,  final payrolldeployment.clientdata.PaymentSet q_p_Payments ) throws XtumlException {
        PaymentSet p_Payments = (PaymentSet) q_p_Payments;
       if ( true ) send(new IHumanResources.PayeeData(p_Department, p_EmployeeID, p_EmployeeFirstName, p_EmployeeLastName, p_Payments));
       else { 
       }
    }
    public void DataSent( final String p_Ident,  final int p_Count ) throws XtumlException {
        if ( true ) send(new IHumanResources.DataSent(p_Ident, p_Count));
        else { 
        }
    }
    public void PayrollAvailable( final String p_Department ) throws XtumlException {
        if ( true ) send(new IHumanResources.PayrollAvailable(p_Department));
        else { 
        }
    }

    @Override 
    public void send(IMessage message) throws XtumlException {
//    	 HRuserMsgController.Singleton().socketsend( message );
    	
    	String msgname = message.getName();
   	    System.out.printf( "Sending in PayrollMgmtUser... msgname: %s \n", msgname ); 
     	String payload = (String)message.getParms().toString();
    	SpringMsg springmsg = new SpringMsg ( msgname, payload );
        String topic = "/topic/HRuser/";
    	this.template.convertAndSend( topic, springmsg );
    }


    @Override
    public void deliver( IMessage message ) throws XtumlException {
        if ( null == message ) throw new BadArgumentException( "Cannot deliver null message." );
        switch ( message.getId() ) {
            case IHumanResources.SIGNAL_NO_SUBMITITEMAPPROVAL:
                SubmitItemApproval(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)), StringUtil.deserialize(message.get(2)));
                break;
            case IHumanResources.SIGNAL_NO_SUBMITPAYAPPROVAL:
                SubmitPayApproval(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)), StringUtil.deserialize(message.get(2)), RealUtil.deserialize(message.get(3)));
                break;
            case IHumanResources.SIGNAL_NO_UPDATESSENT:
                UpdatesSent(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)));
                break;
            case IHumanResources.SIGNAL_NO_SUBMITITEMHOLD:
                SubmitItemHold(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)), StringUtil.deserialize(message.get(2)), BooleanUtil.deserialize(message.get(3)));
                break;
            case IHumanResources.SIGNAL_NO_ACCEPTED:
                Accepted(StringUtil.deserialize(message.get(0)));
                break;
            case IHumanResources.SIGNAL_NO_RETRIEVEPAYROLLFORREVIEW:
                RetrievePayrollForReview(StringUtil.deserialize(message.get(0)), BooleanUtil.deserialize(message.get(1)));
                break;
            case IHumanResources.SIGNAL_NO_SUBMITITEMUPDATE:
                SubmitItemUpdate(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)), StringUtil.deserialize(message.get(2)), RealUtil.deserialize(message.get(3)));
                break;
            case IHumanResources.SIGNAL_NO_AVAILABLEPAYROLLS:
//                AvailablePayrolls();
                break;
            case IHumanResources.SIGNAL_NO_SUBMITPAYROLLAPPROVAL:
                SubmitPayrollApproval(StringUtil.deserialize(message.get(0)));
                break;
            case IHumanResources.SIGNAL_NO_SUBMITPAYHOLD:
                SubmitPayHold(StringUtil.deserialize(message.get(0)), IntegerUtil.deserialize(message.get(1)), StringUtil.deserialize(message.get(2)), RealUtil.deserialize(message.get(3)));
                break;
            case IHumanResources.SIGNAL_NO_SUBMITTOFINANCE:
                SubmitToFinance(StringUtil.deserialize(message.get(0)));
                break;
        default:
            throw new BadArgumentException( "Message not implemented by this port." );
        }
    }



    @Override
    public String getName() {
        return "USER";
    }

}
