package payrolldeployment;


import payrolldeployment.HRuser;
import payrolldeployment.PayrollMgmt;
import payrolldeployment.TestControl;

import io.ciera.runtime.summit.application.ApplicationExecutor;
import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.ILogger;
import io.ciera.runtime.summit.application.tasks.GenericExecutionTask;
import io.ciera.runtime.summit.application.tasks.HaltExecutionTask;
import io.ciera.runtime.summit.components.IComponent;
import io.ciera.runtime.summit.exceptions.XtumlException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@SpringBootApplication
public class PayrollDeploymentApplication implements IApplication {

	private static PayrollDeploymentApplication singleton;
    private IComponent<?>[] components;
    private ApplicationExecutor[] executors;

    public PayrollDeploymentApplication() {
        components = new IComponent<?>[3];
        executors = new ApplicationExecutor[1];
        singleton = this;
        setup( null, null );
        initialize();
    }

    @Override
    public void run() {
        for (ApplicationExecutor executor : executors) {
            executor.run();
        }
    }

    @Override
    public void setup( String[] args, ILogger logger ) {
        for ( int i = 0; i < executors.length; i++ ) {
            if ( null != logger ) {
                executors[i] = new ApplicationExecutor( "PayrollDeploymentApplicationExecutor" + i, args, logger );
            }
            else {
                executors[i] = new ApplicationExecutor( "PayrollDeploymentApplicationExecutor" + i, args );
            }
        }
        components[2] = new TestControl(this, executors[0], 2);
        components[0] = new HRuser(this, executors[0], 0);
        components[1] = new PayrollMgmt(this, executors[0], 1);
//        ((HRuser)components[0]).Payroll().satisfy(((PayrollMgmt)components[1]).USER());
//        ((PayrollMgmt)components[1]).USER().satisfy(((HRuser)components[0]).Payroll());
    }

    public TestControl TestControl() {
        return (TestControl)components[2];
    }
    public HRuser HRuser() {
        return (HRuser)components[0];
    }
    public PayrollMgmt PayrollMgmt() {
        return (PayrollMgmt)components[1];
    }

    @Override
    public void initialize() {
        for ( IComponent<?> component : components ) {
            component.getRunContext().execute( new GenericExecutionTask() {
                @Override
                public void run() throws XtumlException {
                    component.initialize();
                }
            });
        }
    }

    @Override
    public void start() {
        if (1 == executors.length) {
            executors[0].run();
        }
        else {
            for ( ApplicationExecutor executor : executors ) {
                executor.start();
            }
        }
    }

    @Override
    public void printVersions() {
        io.ciera.runtime.Version.printVersion();
        for ( IComponent<?> c : components ) {
            System.out.printf("%s: %s (%s)", c.getClass().getName(), c.getVersion(), c.getVersionDate());
            System.out.println();
        }
    }

    @Override
    public void stop() {
        for ( ApplicationExecutor executor : executors ) {
            executor.execute(new HaltExecutionTask());
        }
    }

    public static void main( String[] args ) {
        SpringApplication.run( PayrollDeploymentApplication.class, args );
        singleton.start();
    }

}
