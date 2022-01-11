package payrolldeployment;


import io.ciera.runtime.summit.application.IApplication;
import io.ciera.runtime.summit.application.IRunContext;
import io.ciera.runtime.summit.classes.IModelInstance;
import io.ciera.runtime.summit.classes.IRelationshipSet;
import io.ciera.runtime.summit.classes.Relationship;
import io.ciera.runtime.summit.classes.RelationshipSet;
import io.ciera.runtime.summit.components.Component;
import io.ciera.runtime.summit.exceptions.BadArgumentException;
import io.ciera.runtime.summit.exceptions.EmptyInstanceException;
import io.ciera.runtime.summit.exceptions.ModelIntegrityException;
import io.ciera.runtime.summit.exceptions.XtumlException;
import io.ciera.runtime.summit.types.StringUtil;
import io.ciera.runtime.summit.util.LOG;
import io.ciera.runtime.summit.util.TIM;
import io.ciera.runtime.summit.util.impl.LOGImpl;
import io.ciera.runtime.summit.util.impl.TIMImpl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import payrolldeployment.payrollmgmt.PayrollMgmtTEST;
import payrolldeployment.payrollmgmt.PayrollMgmtUSER;
import payrolldeployment.payrollmgmt.clientdata.Payment;
import payrolldeployment.payrollmgmt.clientdata.PaymentSet;
import payrolldeployment.payrollmgmt.clientdata.impl.PaymentImpl;
import payrolldeployment.payrollmgmt.clientdata.impl.PaymentSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.Department;
import payrolldeployment.payrollmgmt.payrollmgmt.DepartmentSet;
import payrolldeployment.payrollmgmt.payrollmgmt.Employee;
import payrolldeployment.payrollmgmt.payrollmgmt.EmployeePayment;
import payrolldeployment.payrollmgmt.payrollmgmt.EmployeePaymentElement;
import payrolldeployment.payrollmgmt.payrollmgmt.EmployeePaymentElementSet;
import payrolldeployment.payrollmgmt.payrollmgmt.EmployeePaymentSet;
import payrolldeployment.payrollmgmt.payrollmgmt.EmployeeSet;
import payrolldeployment.payrollmgmt.payrollmgmt.Grade;
import payrolldeployment.payrollmgmt.payrollmgmt.GradeSet;
import payrolldeployment.payrollmgmt.payrollmgmt.Job;
import payrolldeployment.payrollmgmt.payrollmgmt.JobSet;
import payrolldeployment.payrollmgmt.payrollmgmt.JobSpecification;
import payrolldeployment.payrollmgmt.payrollmgmt.JobSpecificationSet;
import payrolldeployment.payrollmgmt.payrollmgmt.PayPeriod;
import payrolldeployment.payrollmgmt.payrollmgmt.PayPeriodSet;
import payrolldeployment.payrollmgmt.payrollmgmt.PaymentElementSpec;
import payrolldeployment.payrollmgmt.payrollmgmt.PaymentElementSpecSet;
import payrolldeployment.payrollmgmt.payrollmgmt.PaymentStructureElement;
import payrolldeployment.payrollmgmt.payrollmgmt.PaymentStructureElementSet;
import payrolldeployment.payrollmgmt.payrollmgmt.Payroll;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollLog;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollLogEntry;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollLogEntrySet;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollLogSet;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollScheduleEntry;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollScheduleEntrySet;
import payrolldeployment.payrollmgmt.payrollmgmt.PayrollSet;
import payrolldeployment.payrollmgmt.payrollmgmt.Step;
import payrolldeployment.payrollmgmt.payrollmgmt.StepSet;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.DepartmentImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.DepartmentSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeeImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeePaymentElementImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeePaymentElementSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeePaymentImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeePaymentSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.EmployeeSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.GradeImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.GradeSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.JobImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.JobSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.JobSpecificationImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.JobSpecificationSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayPeriodImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayPeriodSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PaymentElementSpecImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PaymentElementSpecSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PaymentStructureElementImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PaymentStructureElementSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollLogEntryImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollLogEntrySetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollLogImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollLogSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollScheduleEntryImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollScheduleEntrySetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.PayrollSetImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.StepImpl;
import payrolldeployment.payrollmgmt.payrollmgmt.impl.StepSetImpl;
import payrolldeployment.payrollmgmt.timeutilities.TimeUtilities;
import payrolldeployment.payrollmgmt.timeutilities.TimeUtilitiesSet;
import payrolldeployment.payrollmgmt.timeutilities.impl.TimeUtilitiesImpl;
import payrolldeployment.payrollmgmt.timeutilities.impl.TimeUtilitiesSetImpl;
import payrolldeployment.payrollmgmt.utilities.DateTimeMonitor;
import payrolldeployment.payrollmgmt.utilities.DateTimeMonitorSet;
import payrolldeployment.payrollmgmt.utilities.impl.DateTimeMonitorImpl;
import payrolldeployment.payrollmgmt.utilities.impl.DateTimeMonitorSetImpl;


public class PayrollMgmt extends Component<PayrollMgmt> {

    private Map<String, Class<?>> classDirectory;
	private static PayrollMgmt singleton;
	public static PayrollMgmt Singleton() {
		return singleton;
	}

    public PayrollMgmt(IApplication app, IRunContext runContext, int populationId) {
        super(app, runContext, populationId);
        singleton = this;
        DateTimeMonitor_extent = new DateTimeMonitorSetImpl();
        Department_extent = new DepartmentSetImpl();
        Employee_extent = new EmployeeSetImpl();
        EmployeePayment_extent = new EmployeePaymentSetImpl();
        EmployeePaymentElement_extent = new EmployeePaymentElementSetImpl();
        Grade_extent = new GradeSetImpl();
        Job_extent = new JobSetImpl();
        JobSpecification_extent = new JobSpecificationSetImpl();
        PayPeriod_extent = new PayPeriodSetImpl();
        Payment_extent = new PaymentSetImpl();
        PaymentElementSpec_extent = new PaymentElementSpecSetImpl();
        PaymentStructureElement_extent = new PaymentStructureElementSetImpl();
        Payroll_extent = new PayrollSetImpl();
        PayrollLog_extent = new PayrollLogSetImpl();
        PayrollLogEntry_extent = new PayrollLogEntrySetImpl();
        PayrollScheduleEntry_extent = new PayrollScheduleEntrySetImpl();
        Step_extent = new StepSetImpl();
        TimeUtilities_extent = new TimeUtilitiesSetImpl();
        R100_Payment_contributes_to_Employee_extent = new RelationshipSet();
        R10_JobSpecification_has_limits_refined_by_Step_extent = new RelationshipSet();
        R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent = new RelationshipSet();
        R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent = new RelationshipSet();
        R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent = new RelationshipSet();
        R16_Job_is_an_instance_of_JobSpecification_extent = new RelationshipSet();
        R17_Job_is_current_work_for_Employee_extent = new RelationshipSet();
        R18_Job_has_been_work_for_Employee_extent = new RelationshipSet();
        R24_Payroll_has_been_generated_for_Department_extent = new RelationshipSet();
        R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent = new RelationshipSet();
        R28_EmployeePayment_is_on_hold_for_Payroll_extent = new RelationshipSet();
        R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent = new RelationshipSet();
        R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent = new RelationshipSet();
        R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent = new RelationshipSet();
        R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent = new RelationshipSet();
        R33_Payroll_is_for_PayPeriod_extent = new RelationshipSet();
        R34_Payroll_is_being_reviewed_for_Department_extent = new RelationshipSet();
        R35_EmployeePaymentElement_needs_approval_for_Payroll_extent = new RelationshipSet();
        R3_JobSpecification_is_valued_at_Grade_extent = new RelationshipSet();
        R4_PayrollScheduleEntry_is_scheduled_for_Department_extent = new RelationshipSet();
        R6_Employee_is_assigned_to_Department_extent = new RelationshipSet();
        R7_EmployeePayment_has_payments_for_Employee_extent = new RelationshipSet();
        R7_EmployeePayment_is_on_Payroll_extent = new RelationshipSet();
        R8_PayrollLog_records_generation_of_Payroll_extent = new RelationshipSet();
        R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent = new RelationshipSet();
        LOG = null;
        TIM = null;
        classDirectory = new TreeMap<>();
        classDirectory.put("DateTimeMonitor", DateTimeMonitorImpl.class);
        classDirectory.put("Department", DepartmentImpl.class);
        classDirectory.put("Employee", EmployeeImpl.class);
        classDirectory.put("EmployeePayment", EmployeePaymentImpl.class);
        classDirectory.put("EmployeePaymentElement", EmployeePaymentElementImpl.class);
        classDirectory.put("Grade", GradeImpl.class);
        classDirectory.put("Job", JobImpl.class);
        classDirectory.put("JobSpecification", JobSpecificationImpl.class);
        classDirectory.put("PayPeriod", PayPeriodImpl.class);
        classDirectory.put("Payment", PaymentImpl.class);
        classDirectory.put("PaymentElementSpec", PaymentElementSpecImpl.class);
        classDirectory.put("PaymentStructureElement", PaymentStructureElementImpl.class);
        classDirectory.put("Payroll", PayrollImpl.class);
        classDirectory.put("PayrollLog", PayrollLogImpl.class);
        classDirectory.put("PayrollLogEntry", PayrollLogEntryImpl.class);
        classDirectory.put("PayrollScheduleEntry", PayrollScheduleEntryImpl.class);
        classDirectory.put("Step", StepImpl.class);
        classDirectory.put("TimeUtilities", TimeUtilitiesImpl.class);
    }

    // domain functions
    public void AvailablePayrolls() throws XtumlException {
        DepartmentSet depts = context().Department_instances();
        int count = 0;
  	    System.out.printf( "Available payrolls in component... \n" );
        Department dept;
        for ( Iterator<Department> _dept_iter = depts.elements().iterator(); _dept_iter.hasNext(); ) {
            dept = _dept_iter.next();
            Payroll payroll = dept.R34_has_under_review_Payroll();
            if ( !payroll.isEmpty() ) {
                context().USER().PayrollAvailable( dept.getName() );
                count = count + 1;
            }
        }
        context().USER().DataSent( "available", count );
    }

    public void CreatePEIs() throws XtumlException {
        PaymentElementSpec payspec = PaymentElementSpecImpl.create( context() );
        payspec.setLabel("Monthly salary");
        PaymentElementSpec percent = PaymentElementSpecImpl.create( context() );
        percent.setLabel("Fractional bonus");
        context().relate_R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec( percent, payspec );
        payspec = PaymentElementSpecImpl.create( context() );
        payspec.setLabel("Travel allowance");
        payspec = PaymentElementSpecImpl.create( context() );
        payspec.setLabel("Fixed bonus");
        payspec = PaymentElementSpecImpl.create( context() );
        payspec.setLabel("Reimbursement");
        Department dept = DepartmentImpl.create( context() );
        dept.setName("IT");
        context().TIM().set_epoch( 1, 1, 2020 );
        context().TIM().set_time( 2020, 2, 1, 0, 0, 0, 0 );
        int now = context().TIM().current_seconds();
        PayrollScheduleEntry schedule = PayrollScheduleEntryImpl.create( context() );
        context().relate_R4_PayrollScheduleEntry_is_scheduled_for_Department( schedule, dept );
        schedule.setNotificationDate(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 13, 10 ));
        schedule.setDraftGenerationDate(schedule.getNotificationDate() + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 1, 0 ));
        schedule.setSubmittalDueDate(schedule.getDraftGenerationDate() + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 4, 0 ));
        PayPeriod period = PayPeriodImpl.create( context() );
        period.setStart(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 15, 0 ));
        period.setEnd(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 15, 0 ));
        context().relate_R32_PayrollScheduleEntry_schedules_for_PayPeriod( schedule, period );
        schedule.Initialize();
        schedule = PayrollScheduleEntryImpl.create( context() );
        context().relate_R4_PayrollScheduleEntry_is_scheduled_for_Department( schedule, dept );
        schedule.setNotificationDate(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 43, 10 ));
        schedule.setDraftGenerationDate(schedule.getNotificationDate() + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 1, 0 ));
        schedule.setSubmittalDueDate(schedule.getDraftGenerationDate() + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 4, 0 ));
        period = PayPeriodImpl.create( context() );
        period.setStart(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 16, 0 ));
        period.setEnd(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 46, 0 ));
        context().relate_R32_PayrollScheduleEntry_schedules_for_PayPeriod( schedule, period );
        schedule.Initialize();
        Employee emp = EmployeeImpl.create( context() );
        emp.setNationalID(10001);
        emp.setFirstName("John");
        emp.setLastName("Doe");
        context().relate_R6_Employee_is_assigned_to_Department( emp, dept );
        PaymentStructureElement struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Monthly salary"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 30, 0 ));
        struct.setNominalValue(1100);
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Fractional bonus"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 30, 0 ));
        struct.setNominalValue(0.1);
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Reimbursement"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 30, 0 ));
        struct.setNominalValue(100);
        emp = EmployeeImpl.create( context() );
        emp.setNationalID(10002);
        emp.setFirstName("Clive");
        emp.setLastName("Smith");
        context().relate_R6_Employee_is_assigned_to_Department( emp, dept );
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Monthly salary"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 30, 0 ));
        struct.setNominalValue(1200);
        emp = EmployeeImpl.create( context() );
        emp.setNationalID(10003);
        emp.setFirstName("Mike");
        emp.setLastName("Newman");
        context().relate_R6_Employee_is_assigned_to_Department( emp, dept );
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Monthly salary"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now);
        struct.setNominalValue(1300);
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Fractional bonus"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 5, 0 ));
        struct.setNominalValue(0.1);
        emp = EmployeeImpl.create( context() );
        emp.setNationalID(10004);
        emp.setFirstName("James");
        emp.setLastName("Newcar");
        context().relate_R6_Employee_is_assigned_to_Department( emp, dept );
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Monthly salary"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now - new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 30, 0 ));
        struct.setNominalValue(1400);
        struct = PaymentStructureElementImpl.create( context() );
        context().relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( struct, emp );
        payspec = context().PaymentElementSpec_instances().anyWhere(selected -> StringUtil.equality(((PaymentElementSpec)selected).getLabel(), "Travel allowance"));
        context().relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( struct, payspec );
        struct.setEffectiveDate(now + new TimeUtilitiesImpl.CLASS(context()).DayHourToSeconds( 3, 0 ));
        struct.setNominalValue(10);
    }

    public void MarshalPayroll( final String p_DeptName,  final EmployeeSet p_Employees,  final boolean p_HoldsOnly ) throws XtumlException {
        int employee_count = 0;
        EmployeeSet employees = p_Employees;
        Employee employee;
        for ( Iterator<Employee> _employee_iter = employees.elements().iterator(); _employee_iter.hasNext(); ) {
            employee = _employee_iter.next();
            EmployeePaymentElementSet elements = employee.R7_is_on_EmployeePayment().R15_is_composed_of_EmployeePaymentElement();
            EmployeePaymentElement element;
            for ( Iterator<EmployeePaymentElement> _element_iter = elements.elements().iterator(); _element_iter.hasNext(); ) {
                element = _element_iter.next();
                Payment payment = PaymentImpl.create( context() );
                context().relate_R100_Payment_contributes_to_Employee( payment, employee );
                PaymentElementSpec spec = element.R13_is_a_realization_of_PaymentStructureElement().R12_is_typed_by_PaymentElementSpec();
                payment.setLabel(spec.getLabel());
                payment.setAmount(element.getAmount());
                Payroll payroll = element.R29_is_on_hold_for_Payroll();
                if ( !payroll.isEmpty() ) {
                    payment.setOnhold(true);
                }
                payroll = element.R35_needs_approval_for_Payroll();
                if ( !payroll.isEmpty() ) {
                    payment.setUnapproved(true);
                }
            }
            PaymentSet payments = employee.R100_is_reimbursed__Payment();
            context().USER().PayeeData( p_DeptName, employee.getNationalID(), employee.getFirstName(), employee.getLastName(), payments );
            Payment payment;
            for ( Iterator<Payment> _payment_iter = payments.elements().iterator(); _payment_iter.hasNext(); ) {
                payment = _payment_iter.next();
                context().unrelate_R100_Payment_contributes_to_Employee( payment, employee );
                payment.delete();
            }
            employee_count = employee_count + 1;
        }
        context().USER().DataSent( "payroll", employee_count );
    }



    // relates and unrelates
    public void relate_R100_Payment_contributes_to_Employee( Payment form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R100_Payment_contributes_to_Employee_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR100_is_reimbursed__Payment(form);
            form.setR100_contributes_to_Employee(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R100_Payment_contributes_to_Employee( Payment form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R100_Payment_contributes_to_Employee_extent.remove( R100_Payment_contributes_to_Employee_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR100_is_reimbursed__Payment(form);
            form.setR100_contributes_to_Employee(EmployeeImpl.EMPTY_EMPLOYEE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R10_JobSpecification_has_limits_refined_by_Step( JobSpecification form, Step part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R10_JobSpecification_has_limits_refined_by_Step_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR10_refines_limits_for_JobSpecification(form);
            form.setR10_has_limits_refined_by_Step(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R10_JobSpecification_has_limits_refined_by_Step( JobSpecification form, Step part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R10_JobSpecification_has_limits_refined_by_Step_extent.remove( R10_JobSpecification_has_limits_refined_by_Step_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR10_refines_limits_for_JobSpecification(form);
            form.setR10_has_limits_refined_by_Step(StepImpl.EMPTY_STEP);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( PaymentStructureElement form, PaymentElementSpec part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR12_defines_behavior_of_PaymentStructureElement(form);
            form.setR12_is_typed_by_PaymentElementSpec(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R12_PaymentStructureElement_is_typed_by_PaymentElementSpec( PaymentStructureElement form, PaymentElementSpec part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent.remove( R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR12_defines_behavior_of_PaymentStructureElement(form);
            form.setR12_is_typed_by_PaymentElementSpec(PaymentElementSpecImpl.EMPTY_PAYMENTELEMENTSPEC);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement( EmployeePaymentElement form, PaymentStructureElement part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR13_is_template_for_EmployeePaymentElement(form);
            form.setR13_is_a_realization_of_PaymentStructureElement(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement( EmployeePaymentElement form, PaymentStructureElement part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent.remove( R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR13_is_template_for_EmployeePaymentElement(form);
            form.setR13_is_a_realization_of_PaymentStructureElement(PaymentStructureElementImpl.EMPTY_PAYMENTSTRUCTUREELEMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R15_EmployeePaymentElement_contributes_to_EmployeePayment( EmployeePaymentElement form, EmployeePayment part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR15_is_composed_of_EmployeePaymentElement(form);
            form.setR15_contributes_to_EmployeePayment(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R15_EmployeePaymentElement_contributes_to_EmployeePayment( EmployeePaymentElement form, EmployeePayment part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent.remove( R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR15_is_composed_of_EmployeePaymentElement(form);
            form.setR15_contributes_to_EmployeePayment(EmployeePaymentImpl.EMPTY_EMPLOYEEPAYMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R16_Job_is_an_instance_of_JobSpecification( Job form, JobSpecification part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R16_Job_is_an_instance_of_JobSpecification_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR16_specifies_Job(form);
            form.setR16_is_an_instance_of_JobSpecification(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R16_Job_is_an_instance_of_JobSpecification( Job form, JobSpecification part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R16_Job_is_an_instance_of_JobSpecification_extent.remove( R16_Job_is_an_instance_of_JobSpecification_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR16_specifies_Job(form);
            form.setR16_is_an_instance_of_JobSpecification(JobSpecificationImpl.EMPTY_JOBSPECIFICATION);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R17_Job_is_current_work_for_Employee( Job form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R17_Job_is_current_work_for_Employee_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR17_is_currently_performing_Job(form);
            form.setR17_is_current_work_for_Employee(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R17_Job_is_current_work_for_Employee( Job form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R17_Job_is_current_work_for_Employee_extent.remove( R17_Job_is_current_work_for_Employee_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR17_is_currently_performing_Job(JobImpl.EMPTY_JOB);
            form.setR17_is_current_work_for_Employee(EmployeeImpl.EMPTY_EMPLOYEE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R18_Job_has_been_work_for_Employee( Job form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R18_Job_has_been_work_for_Employee_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR18_has_previously_worked_Job(form);
            form.setR18_has_been_work_for_Employee(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R18_Job_has_been_work_for_Employee( Job form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R18_Job_has_been_work_for_Employee_extent.remove( R18_Job_has_been_work_for_Employee_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR18_has_previously_worked_Job(form);
            form.setR18_has_been_work_for_Employee(EmployeeImpl.EMPTY_EMPLOYEE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R24_Payroll_has_been_generated_for_Department( Payroll form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R24_Payroll_has_been_generated_for_Department_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR24_has_had_generated_Payroll(form);
            form.setR24_has_been_generated_for_Department(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R24_Payroll_has_been_generated_for_Department( Payroll form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R24_Payroll_has_been_generated_for_Department_extent.remove( R24_Payroll_has_been_generated_for_Department_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR24_has_had_generated_Payroll(form);
            form.setR24_has_been_generated_for_Department(DepartmentImpl.EMPTY_DEPARTMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( PaymentStructureElement form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR27_has_payment_contributions_specified_by_PaymentStructureElement(form);
            form.setR27_specifies_a_payment_contribution_for_Employee(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee( PaymentStructureElement form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent.remove( R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR27_has_payment_contributions_specified_by_PaymentStructureElement(form);
            form.setR27_specifies_a_payment_contribution_for_Employee(EmployeeImpl.EMPTY_EMPLOYEE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R28_EmployeePayment_is_on_hold_for_Payroll( EmployeePayment form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R28_EmployeePayment_is_on_hold_for_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR28_has_placed_on_hold_EmployeePayment(form);
            form.setR28_is_on_hold_for_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R28_EmployeePayment_is_on_hold_for_Payroll( EmployeePayment form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R28_EmployeePayment_is_on_hold_for_Payroll_extent.remove( R28_EmployeePayment_is_on_hold_for_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR28_has_placed_on_hold_EmployeePayment(form);
            form.setR28_is_on_hold_for_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R29_EmployeePaymentElement_is_on_hold_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR29_has_placed_on_hold_EmployeePaymentElement(form);
            form.setR29_is_on_hold_for_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R29_EmployeePaymentElement_is_on_hold_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent.remove( R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR29_has_placed_on_hold_EmployeePaymentElement(form);
            form.setR29_is_on_hold_for_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec( PaymentElementSpec form, PaymentElementSpec part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR30_is_modified_by_PaymentElementSpec(form);
            form.setR30_is_a_modifier_for_PaymentElementSpec(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec( PaymentElementSpec form, PaymentElementSpec part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent.remove( R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR30_is_modified_by_PaymentElementSpec(PaymentElementSpecImpl.EMPTY_PAYMENTELEMENTSPEC);
            form.setR30_is_a_modifier_for_PaymentElementSpec(PaymentElementSpecImpl.EMPTY_PAYMENTELEMENTSPEC);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R31_EmployeePaymentElement_has_been_edited_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR31_has_edited_EmployeePaymentElement(form);
            form.setR31_has_been_edited_for_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R31_EmployeePaymentElement_has_been_edited_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent.remove( R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR31_has_edited_EmployeePaymentElement(form);
            form.setR31_has_been_edited_for_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R32_PayrollScheduleEntry_schedules_for_PayPeriod( PayrollScheduleEntry form, PayPeriod part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR32_specifies_dates_for_PayrollScheduleEntry(form);
            form.setR32_schedules_for_PayPeriod(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R32_PayrollScheduleEntry_schedules_for_PayPeriod( PayrollScheduleEntry form, PayPeriod part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent.remove( R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR32_specifies_dates_for_PayrollScheduleEntry(PayrollScheduleEntryImpl.EMPTY_PAYROLLSCHEDULEENTRY);
            form.setR32_schedules_for_PayPeriod(PayPeriodImpl.EMPTY_PAYPERIOD);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R33_Payroll_is_for_PayPeriod( Payroll form, PayPeriod part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R33_Payroll_is_for_PayPeriod_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR33_specifies_dates_for_Payroll(form);
            form.setR33_is_for_PayPeriod(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R33_Payroll_is_for_PayPeriod( Payroll form, PayPeriod part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R33_Payroll_is_for_PayPeriod_extent.remove( R33_Payroll_is_for_PayPeriod_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR33_specifies_dates_for_Payroll(PayrollImpl.EMPTY_PAYROLL);
            form.setR33_is_for_PayPeriod(PayPeriodImpl.EMPTY_PAYPERIOD);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R34_Payroll_is_being_reviewed_for_Department( Payroll form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R34_Payroll_is_being_reviewed_for_Department_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR34_has_under_review_Payroll(form);
            form.setR34_is_being_reviewed_for_Department(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R34_Payroll_is_being_reviewed_for_Department( Payroll form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R34_Payroll_is_being_reviewed_for_Department_extent.remove( R34_Payroll_is_being_reviewed_for_Department_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.setR34_has_under_review_Payroll(PayrollImpl.EMPTY_PAYROLL);
            form.setR34_is_being_reviewed_for_Department(DepartmentImpl.EMPTY_DEPARTMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R35_EmployeePaymentElement_needs_approval_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R35_EmployeePaymentElement_needs_approval_for_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR35_has_unapproved_EmployeePaymentElement(form);
            form.setR35_needs_approval_for_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R35_EmployeePaymentElement_needs_approval_for_Payroll( EmployeePaymentElement form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R35_EmployeePaymentElement_needs_approval_for_Payroll_extent.remove( R35_EmployeePaymentElement_needs_approval_for_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR35_has_unapproved_EmployeePaymentElement(form);
            form.setR35_needs_approval_for_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R3_JobSpecification_is_valued_at_Grade( JobSpecification form, Grade part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R3_JobSpecification_is_valued_at_Grade_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR3_determines_pay_bounds_for_JobSpecification(form);
            form.setR3_is_valued_at_Grade(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R3_JobSpecification_is_valued_at_Grade( JobSpecification form, Grade part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R3_JobSpecification_is_valued_at_Grade_extent.remove( R3_JobSpecification_is_valued_at_Grade_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR3_determines_pay_bounds_for_JobSpecification(form);
            form.setR3_is_valued_at_Grade(GradeImpl.EMPTY_GRADE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R4_PayrollScheduleEntry_is_scheduled_for_Department( PayrollScheduleEntry form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R4_PayrollScheduleEntry_is_scheduled_for_Department_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR4_has_pending_PayrollScheduleEntry(form);
            form.setR4_is_scheduled_for_Department(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R4_PayrollScheduleEntry_is_scheduled_for_Department( PayrollScheduleEntry form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R4_PayrollScheduleEntry_is_scheduled_for_Department_extent.remove( R4_PayrollScheduleEntry_is_scheduled_for_Department_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR4_has_pending_PayrollScheduleEntry(form);
            form.setR4_is_scheduled_for_Department(DepartmentImpl.EMPTY_DEPARTMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R6_Employee_is_assigned_to_Department( Employee form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R6_Employee_is_assigned_to_Department_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR6_employs_Employee(form);
            form.setR6_is_assigned_to_Department(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R6_Employee_is_assigned_to_Department( Employee form, Department part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R6_Employee_is_assigned_to_Department_extent.remove( R6_Employee_is_assigned_to_Department_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR6_employs_Employee(form);
            form.setR6_is_assigned_to_Department(DepartmentImpl.EMPTY_DEPARTMENT);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R7_EmployeePayment_has_payments_for_Employee( EmployeePayment form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R7_EmployeePayment_has_payments_for_Employee_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR7_is_on_EmployeePayment(form);
            form.setR7_has_payments_for_Employee(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R7_EmployeePayment_has_payments_for_Employee( EmployeePayment form, Employee part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R7_EmployeePayment_has_payments_for_Employee_extent.remove( R7_EmployeePayment_has_payments_for_Employee_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR7_is_on_EmployeePayment(form);
            form.setR7_has_payments_for_Employee(EmployeeImpl.EMPTY_EMPLOYEE);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R7_EmployeePayment_is_on_Payroll( EmployeePayment form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R7_EmployeePayment_is_on_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR7_has_payments_for_EmployeePayment(form);
            form.setR7_is_on_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R7_EmployeePayment_is_on_Payroll( EmployeePayment form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R7_EmployeePayment_is_on_Payroll_extent.remove( R7_EmployeePayment_is_on_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR7_has_payments_for_EmployeePayment(form);
            form.setR7_is_on_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R8_PayrollLog_records_generation_of_Payroll( PayrollLog form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R8_PayrollLog_records_generation_of_Payroll_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR8_records_generation_in_PayrollLog(form);
            form.setR8_records_generation_of_Payroll(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R8_PayrollLog_records_generation_of_Payroll( PayrollLog form, Payroll part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R8_PayrollLog_records_generation_of_Payroll_extent.remove( R8_PayrollLog_records_generation_of_Payroll_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR8_records_generation_in_PayrollLog(form);
            form.setR8_records_generation_of_Payroll(PayrollImpl.EMPTY_PAYROLL);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }
    public void relate_R9_PayrollLogEntry_is_sequenced_in_PayrollLog( PayrollLogEntry form, PayrollLog part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot relate empty instances." );
        // TODO cardinality check
        if ( R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent.add( new Relationship( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.addR9_records_payment_generation_in_PayrollLogEntry(form);
            form.setR9_is_sequenced_in_PayrollLog(part);
        }
        else throw new ModelIntegrityException( "Instances could not be related." );
    }

    public void unrelate_R9_PayrollLogEntry_is_sequenced_in_PayrollLog( PayrollLogEntry form, PayrollLog part ) throws XtumlException {
        if ( null == form || null == part ) throw new BadArgumentException( "Null instances passed." );
        if ( form.isEmpty() || part.isEmpty() ) throw new EmptyInstanceException( "Cannot unrelate empty instances." );
        if ( R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent.remove( R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent.get( form.getInstanceId(), part.getInstanceId() ) ) ) {
            part.removeR9_records_payment_generation_in_PayrollLogEntry(form);
            form.setR9_is_sequenced_in_PayrollLog(PayrollLogImpl.EMPTY_PAYROLLLOG);
        }
        else throw new ModelIntegrityException( "Instances could not be unrelated." );
    }


    // instance selections
    private DateTimeMonitorSet DateTimeMonitor_extent;
    public DateTimeMonitorSet DateTimeMonitor_instances() {
        return DateTimeMonitor_extent;
    }
    private DepartmentSet Department_extent;
    public DepartmentSet Department_instances() {
        return Department_extent;
    }
    private EmployeePaymentElementSet EmployeePaymentElement_extent;
    public EmployeePaymentElementSet EmployeePaymentElement_instances() {
        return EmployeePaymentElement_extent;
    }
    private EmployeePaymentSet EmployeePayment_extent;
    public EmployeePaymentSet EmployeePayment_instances() {
        return EmployeePayment_extent;
    }
    private EmployeeSet Employee_extent;
    public EmployeeSet Employee_instances() {
        return Employee_extent;
    }
    private GradeSet Grade_extent;
    public GradeSet Grade_instances() {
        return Grade_extent;
    }
    private JobSpecificationSet JobSpecification_extent;
    public JobSpecificationSet JobSpecification_instances() {
        return JobSpecification_extent;
    }
    private JobSet Job_extent;
    public JobSet Job_instances() {
        return Job_extent;
    }
    private PayPeriodSet PayPeriod_extent;
    public PayPeriodSet PayPeriod_instances() {
        return PayPeriod_extent;
    }
    private PaymentElementSpecSet PaymentElementSpec_extent;
    public PaymentElementSpecSet PaymentElementSpec_instances() {
        return PaymentElementSpec_extent;
    }
    private PaymentStructureElementSet PaymentStructureElement_extent;
    public PaymentStructureElementSet PaymentStructureElement_instances() {
        return PaymentStructureElement_extent;
    }
    private PaymentSet Payment_extent;
    public PaymentSet Payment_instances() {
        return Payment_extent;
    }
    private PayrollLogEntrySet PayrollLogEntry_extent;
    public PayrollLogEntrySet PayrollLogEntry_instances() {
        return PayrollLogEntry_extent;
    }
    private PayrollLogSet PayrollLog_extent;
    public PayrollLogSet PayrollLog_instances() {
        return PayrollLog_extent;
    }
    private PayrollScheduleEntrySet PayrollScheduleEntry_extent;
    public PayrollScheduleEntrySet PayrollScheduleEntry_instances() {
        return PayrollScheduleEntry_extent;
    }
    private PayrollSet Payroll_extent;
    public PayrollSet Payroll_instances() {
        return Payroll_extent;
    }
    private StepSet Step_extent;
    public StepSet Step_instances() {
        return Step_extent;
    }
    private TimeUtilitiesSet TimeUtilities_extent;
    public TimeUtilitiesSet TimeUtilities_instances() {
        return TimeUtilities_extent;
    }


    // relationship selections
    private IRelationshipSet R100_Payment_contributes_to_Employee_extent;
    public IRelationshipSet R100_Payment_contributes_to_Employees() throws XtumlException {
        return R100_Payment_contributes_to_Employee_extent;
    }
    private IRelationshipSet R10_JobSpecification_has_limits_refined_by_Step_extent;
    public IRelationshipSet R10_JobSpecification_has_limits_refined_by_Steps() throws XtumlException {
        return R10_JobSpecification_has_limits_refined_by_Step_extent;
    }
    private IRelationshipSet R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent;
    public IRelationshipSet R12_PaymentStructureElement_is_typed_by_PaymentElementSpecs() throws XtumlException {
        return R12_PaymentStructureElement_is_typed_by_PaymentElementSpec_extent;
    }
    private IRelationshipSet R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent;
    public IRelationshipSet R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElements() throws XtumlException {
        return R13_EmployeePaymentElement_is_a_realization_of_PaymentStructureElement_extent;
    }
    private IRelationshipSet R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent;
    public IRelationshipSet R15_EmployeePaymentElement_contributes_to_EmployeePayments() throws XtumlException {
        return R15_EmployeePaymentElement_contributes_to_EmployeePayment_extent;
    }
    private IRelationshipSet R16_Job_is_an_instance_of_JobSpecification_extent;
    public IRelationshipSet R16_Job_is_an_instance_of_JobSpecifications() throws XtumlException {
        return R16_Job_is_an_instance_of_JobSpecification_extent;
    }
    private IRelationshipSet R17_Job_is_current_work_for_Employee_extent;
    public IRelationshipSet R17_Job_is_current_work_for_Employees() throws XtumlException {
        return R17_Job_is_current_work_for_Employee_extent;
    }
    private IRelationshipSet R18_Job_has_been_work_for_Employee_extent;
    public IRelationshipSet R18_Job_has_been_work_for_Employees() throws XtumlException {
        return R18_Job_has_been_work_for_Employee_extent;
    }
    private IRelationshipSet R24_Payroll_has_been_generated_for_Department_extent;
    public IRelationshipSet R24_Payroll_has_been_generated_for_Departments() throws XtumlException {
        return R24_Payroll_has_been_generated_for_Department_extent;
    }
    private IRelationshipSet R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent;
    public IRelationshipSet R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employees() throws XtumlException {
        return R27_PaymentStructureElement_specifies_a_payment_contribution_for_Employee_extent;
    }
    private IRelationshipSet R28_EmployeePayment_is_on_hold_for_Payroll_extent;
    public IRelationshipSet R28_EmployeePayment_is_on_hold_for_Payrolls() throws XtumlException {
        return R28_EmployeePayment_is_on_hold_for_Payroll_extent;
    }
    private IRelationshipSet R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent;
    public IRelationshipSet R29_EmployeePaymentElement_is_on_hold_for_Payrolls() throws XtumlException {
        return R29_EmployeePaymentElement_is_on_hold_for_Payroll_extent;
    }
    private IRelationshipSet R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent;
    public IRelationshipSet R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpecs() throws XtumlException {
        return R30_PaymentElementSpec_is_a_modifier_for_PaymentElementSpec_extent;
    }
    private IRelationshipSet R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent;
    public IRelationshipSet R31_EmployeePaymentElement_has_been_edited_for_Payrolls() throws XtumlException {
        return R31_EmployeePaymentElement_has_been_edited_for_Payroll_extent;
    }
    private IRelationshipSet R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent;
    public IRelationshipSet R32_PayrollScheduleEntry_schedules_for_PayPeriods() throws XtumlException {
        return R32_PayrollScheduleEntry_schedules_for_PayPeriod_extent;
    }
    private IRelationshipSet R33_Payroll_is_for_PayPeriod_extent;
    public IRelationshipSet R33_Payroll_is_for_PayPeriods() throws XtumlException {
        return R33_Payroll_is_for_PayPeriod_extent;
    }
    private IRelationshipSet R34_Payroll_is_being_reviewed_for_Department_extent;
    public IRelationshipSet R34_Payroll_is_being_reviewed_for_Departments() throws XtumlException {
        return R34_Payroll_is_being_reviewed_for_Department_extent;
    }
    private IRelationshipSet R35_EmployeePaymentElement_needs_approval_for_Payroll_extent;
    public IRelationshipSet R35_EmployeePaymentElement_needs_approval_for_Payrolls() throws XtumlException {
        return R35_EmployeePaymentElement_needs_approval_for_Payroll_extent;
    }
    private IRelationshipSet R3_JobSpecification_is_valued_at_Grade_extent;
    public IRelationshipSet R3_JobSpecification_is_valued_at_Grades() throws XtumlException {
        return R3_JobSpecification_is_valued_at_Grade_extent;
    }
    private IRelationshipSet R4_PayrollScheduleEntry_is_scheduled_for_Department_extent;
    public IRelationshipSet R4_PayrollScheduleEntry_is_scheduled_for_Departments() throws XtumlException {
        return R4_PayrollScheduleEntry_is_scheduled_for_Department_extent;
    }
    private IRelationshipSet R6_Employee_is_assigned_to_Department_extent;
    public IRelationshipSet R6_Employee_is_assigned_to_Departments() throws XtumlException {
        return R6_Employee_is_assigned_to_Department_extent;
    }
    private IRelationshipSet R7_EmployeePayment_has_payments_for_Employee_extent;
    public IRelationshipSet R7_EmployeePayment_has_payments_for_Employees() throws XtumlException {
        return R7_EmployeePayment_has_payments_for_Employee_extent;
    }
    private IRelationshipSet R7_EmployeePayment_is_on_Payroll_extent;
    public IRelationshipSet R7_EmployeePayment_is_on_Payrolls() throws XtumlException {
        return R7_EmployeePayment_is_on_Payroll_extent;
    }
    private IRelationshipSet R8_PayrollLog_records_generation_of_Payroll_extent;
    public IRelationshipSet R8_PayrollLog_records_generation_of_Payrolls() throws XtumlException {
        return R8_PayrollLog_records_generation_of_Payroll_extent;
    }
    private IRelationshipSet R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent;
    public IRelationshipSet R9_PayrollLogEntry_is_sequenced_in_PayrollLogs() throws XtumlException {
        return R9_PayrollLogEntry_is_sequenced_in_PayrollLog_extent;
    }


    // ports
    private PayrollMgmtTEST PayrollMgmtTEST;
    public PayrollMgmtTEST TEST() {
        if ( null == PayrollMgmtTEST ) PayrollMgmtTEST = new PayrollMgmtTEST( this, null );
        return PayrollMgmtTEST;
    }
    private PayrollMgmtUSER PayrollMgmtUSER;
    public PayrollMgmtUSER USER() {
        if ( null == PayrollMgmtUSER ) {
            PayrollMgmtUSER = PayrollMgmtUSER.Singleton();
        }
        return PayrollMgmtUSER;
    }


    // utilities
    private LOG LOG;
    public LOG LOG() {
        if ( null == LOG ) LOG = new LOGImpl<>( this );
        return LOG;
    }
    private TIM TIM;
    public TIM TIM() {
        if ( null == TIM ) TIM = new TIMImpl<>( this );
        return TIM;
    }


    // component initialization function
    @Override
    public void initialize() throws XtumlException {
        CreatePEIs();
    }

    @Override
    public String getVersion() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("PayrollMgmtProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version", "Unknown");
    }
    @Override
    public String getVersionDate() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("PayrollMgmtProperties.properties"));
        } catch (IOException e) { /* do nothing */ }
        return prop.getProperty("version_date", "Unknown");
    }

    @Override
    public boolean addInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot add empty instance to population." );
        if ( instance instanceof DateTimeMonitor ) return DateTimeMonitor_extent.add( (DateTimeMonitor)instance );
        else if ( instance instanceof Department ) return Department_extent.add( (Department)instance );
        else if ( instance instanceof Employee ) return Employee_extent.add( (Employee)instance );
        else if ( instance instanceof EmployeePayment ) return EmployeePayment_extent.add( (EmployeePayment)instance );
        else if ( instance instanceof EmployeePaymentElement ) return EmployeePaymentElement_extent.add( (EmployeePaymentElement)instance );
        else if ( instance instanceof Grade ) return Grade_extent.add( (Grade)instance );
        else if ( instance instanceof Job ) return Job_extent.add( (Job)instance );
        else if ( instance instanceof JobSpecification ) return JobSpecification_extent.add( (JobSpecification)instance );
        else if ( instance instanceof PayPeriod ) return PayPeriod_extent.add( (PayPeriod)instance );
        else if ( instance instanceof Payment ) return Payment_extent.add( (Payment)instance );
        else if ( instance instanceof PaymentElementSpec ) return PaymentElementSpec_extent.add( (PaymentElementSpec)instance );
        else if ( instance instanceof PaymentStructureElement ) return PaymentStructureElement_extent.add( (PaymentStructureElement)instance );
        else if ( instance instanceof Payroll ) return Payroll_extent.add( (Payroll)instance );
        else if ( instance instanceof PayrollLog ) return PayrollLog_extent.add( (PayrollLog)instance );
        else if ( instance instanceof PayrollLogEntry ) return PayrollLogEntry_extent.add( (PayrollLogEntry)instance );
        else if ( instance instanceof PayrollScheduleEntry ) return PayrollScheduleEntry_extent.add( (PayrollScheduleEntry)instance );
        else if ( instance instanceof Step ) return Step_extent.add( (Step)instance );
        else if ( instance instanceof TimeUtilities ) return TimeUtilities_extent.add( (TimeUtilities)instance );
        return false;
    }

    @Override
    public boolean removeInstance( IModelInstance<?,?> instance ) throws XtumlException {
        if ( null == instance ) throw new BadArgumentException( "Null instance passed." );
        if ( instance.isEmpty() ) throw new EmptyInstanceException( "Cannot remove empty instance from population." );
        if ( instance instanceof DateTimeMonitor ) return DateTimeMonitor_extent.remove( (DateTimeMonitor)instance );
        else if ( instance instanceof Department ) return Department_extent.remove( (Department)instance );
        else if ( instance instanceof Employee ) return Employee_extent.remove( (Employee)instance );
        else if ( instance instanceof EmployeePayment ) return EmployeePayment_extent.remove( (EmployeePayment)instance );
        else if ( instance instanceof EmployeePaymentElement ) return EmployeePaymentElement_extent.remove( (EmployeePaymentElement)instance );
        else if ( instance instanceof Grade ) return Grade_extent.remove( (Grade)instance );
        else if ( instance instanceof Job ) return Job_extent.remove( (Job)instance );
        else if ( instance instanceof JobSpecification ) return JobSpecification_extent.remove( (JobSpecification)instance );
        else if ( instance instanceof PayPeriod ) return PayPeriod_extent.remove( (PayPeriod)instance );
        else if ( instance instanceof Payment ) return Payment_extent.remove( (Payment)instance );
        else if ( instance instanceof PaymentElementSpec ) return PaymentElementSpec_extent.remove( (PaymentElementSpec)instance );
        else if ( instance instanceof PaymentStructureElement ) return PaymentStructureElement_extent.remove( (PaymentStructureElement)instance );
        else if ( instance instanceof Payroll ) return Payroll_extent.remove( (Payroll)instance );
        else if ( instance instanceof PayrollLog ) return PayrollLog_extent.remove( (PayrollLog)instance );
        else if ( instance instanceof PayrollLogEntry ) return PayrollLogEntry_extent.remove( (PayrollLogEntry)instance );
        else if ( instance instanceof PayrollScheduleEntry ) return PayrollScheduleEntry_extent.remove( (PayrollScheduleEntry)instance );
        else if ( instance instanceof Step ) return Step_extent.remove( (Step)instance );
        else if ( instance instanceof TimeUtilities ) return TimeUtilities_extent.remove( (TimeUtilities)instance );
        return false;
    }

    @Override
    public PayrollMgmt context() {
        return this;
    }

    @Override
    public Class<?> getClassByKeyLetters(String keyLetters) {
        return classDirectory.get(keyLetters);
    }

}
