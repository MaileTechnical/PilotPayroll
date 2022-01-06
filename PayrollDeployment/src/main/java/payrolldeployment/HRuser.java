package payrolldeployment;


import payrolldeployment.HRuserMsgController;
import payrolldeployment.hruser.HRuserPayroll;
import payrolldeployment.hruser.clientdata.Payment;
import payrolldeployment.hruser.clientdata.PaymentSet;
import payrolldeployment.hruser.clientdata.impl.PaymentImpl;
import payrolldeployment.hruser.clientdata.impl.PaymentSetImpl;


import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.classes.IModelInstance;
import io.ciera.runtime.summit.components.Component;
import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.XtumlException;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


public class HRuser extends Component<HRuser> {

    private Map<String, Class<?>> classDirectory;
    private static HRuser singleton;

    public HRuser(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);
        Payment_extent = new PaymentSetImpl();
        classDirectory = new TreeMap<>();
        classDirectory.put("Payment", PaymentImpl.class);
        singleton = this;
    }
    
    public static HRuser Singleton() {
    	return singleton;
    }

    // domain functions
    public void Notification( final String p_Ident, final String p_Content ) throws XtumlException {
    	try {
            HRuserMsgController.Singleton().SendNotificationMsg( p_Ident, p_Content );
      	} catch ( Exception e ) {}
    }

    public void PayeeData( final String p_Department, final int p_EmployeeID, final String p_EmployeeFirstName, final String p_EmployeeLastName, final PaymentSet p_Payments ) throws XtumlException {
    	try {
            HRuserMsgController.Singleton().SendPayeeDataMsg( p_Department, p_EmployeeID, p_EmployeeFirstName, p_EmployeeLastName, p_Payments );
      	} catch ( Exception e ) {}
    }

    public void DataSent( final String p_Ident, final int p_Count ) throws XtumlException {
       	try {
            HRuserMsgController.Singleton().SendDataSentMsg( p_Ident, p_Count );
      	} catch ( Exception e ) {}
    }

    public void PayrollAvailable( final String p_Department ) throws XtumlException {
       	try {
            HRuserMsgController.Singleton().SendPayrollAvailableMsg( p_Department );
      	} catch ( Exception e ) {}
    }

    // relates and unrelates


    // instance selections
    private PaymentSet Payment_extent;
    public PaymentSet Payment_instances() {
        return Payment_extent;
    }


    // relationship selections


    // ports
    private HRuserPayroll HRuserPayroll;
    public HRuserPayroll Payroll() {
        if ( null == HRuserPayroll ) HRuserPayroll = new HRuserPayroll( this, null );
        return HRuserPayroll;
    }


    // utilities


    // component initialization function
    @Override
    public void initialize() throws XtumlException {

    }

    @Override
    public String getVersion() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("HRuserProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("HRuserProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version_date", "Unknown");
    }

    @Override
    public boolean addInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot add empty instance to population." );
        if ( instance instanceof Payment ) return Payment_extent.add( (Payment)instance );

        return false;
    }

    @Override
    public boolean removeInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot remove empty instance from population." );
        if ( instance instanceof Payment ) return Payment_extent.remove( (Payment)instance );

        return false;
    }

    @Override
    public HRuser context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
