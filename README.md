# PilotPayroll

An in-development pilot of a simple payroll application.

##License
This is Open Source Software (OSS) licensed under Apache 2.0 (http://www.apache.org/licenses/LICENSE-2.0). 

Documentation is licensed under Creative Commons (https://creativecommons.org/).
## Configurations
The following configurations exist:
1. Automated Testing.  Employs a modeled test bench, enabling:
 - Self-checking testing with Verifier
 - Ciera-generated code using `pom-testbench.xml`.
2. Browser-based Clients.  Leverages browser-based clients repesenting a user with payroll review authority.
 - Use `pom-clients.xml` for this configuration.
## Populating a Workspace
Instructions for populating a workspace for each configuration are provided below.  In all cases, the "Payroll" project referred to below is the xtUML project by that name, not the top-level directory which happens to have the same name.  The import wizard lists each project in the repository as "Payroll/\<projectName>", so the "Payroll" project referred to below is shown as "Payroll/Payroll".

The built-in external entities provided by BridgePoint 7 (or later) can be added to a workspace as follows:
1. Create an xtUML project named *BuiltInEEs*.
2. Add a folder named *EEs* to the *BuiltInEEs* project.
3. Select the *EEs* folder and select the context menu entry (right-click) for "Add Built-in External Entities".
## Importing for Automated Testing - Ciera

1. Add the built-in external entities provided by BridgePoint 7 or later.
2. Import the TestFramework project from the [TestFramework repository.](https://github.com/MaileTechnical/TestFramework)
3. Import the following projects from this repository:
- Payroll
- AutomatedTestbench
4. Build with pom-testbench.xml.
5. Execute (bash) run-testbench.sh; test cases should run automatically.
## Importing for Browser-based Clients - Ciera
1. Add the built-in external entities provided by BridgePoint 7 or later.
2. Import the following projects from this repository:
 - Payroll
 - HRuser
 - TestControl
3. Build with pom-clients.xml.
4. Execute (bash) run-clients-servlet.sh.
5. In a browser, open two consoles: localhost:8080/TestControl.html & localhost:8080/HRuser.html; hit 'connect' in both.
6. In TestControl, Set Time to day 14, hour 11 of month 2 in 2020. In HumanResources, payroll generation will be imminent.
7. Advance time by 24 hours. Payroll will be generated. Proceed to Request available, Submit updates etc.
## Run for Automated Testing (Verifier)
1. Create a debug configuration of type "xtUML eXecute Application" and name it Payroll_test
2. Enable "Log model execution activity"
3. Disable "Run deterministically"
4. Disable "Enable simulated time"
5. Select the components in SimulationConfiguration within the AutomatedTesting project.
6. Run Verifier using this debug configuration
7. Execute function RunNominalCases in TestFunctions in AutomatedTesting - all test cases should pass.
