# PilotPayroll

An in-development pilot of a simple payroll application.

This project originated as a tutorial-cum-demonstration during an engagement with a client with  ambitious goals for the development of an in-house suite of business software. Tutorial, because, while the client contact was enthusiastic about using xtUML, the development team had little experience; the tutorial was to complement a training course. Demonstration, because it was felt an early working prototype of some segment of the suite might avert any skepticism up the chain of command.

Only a subset of the capabilities were made operational, the class model of job specification, for instance, served only as a ‘strawman’ rendition of some ill-defined requirements. Sufficient functionality was implemented to support several of the provided use cases.

While a convincing demonstration required browser-based client interactions with the model executing as a web server, experience dictated that automated regression testing is of great benefit to a developing project of any complexity. The latter was achieved by making use of a stand-alone xtUML component which had evolved from earlier application-specific testing developed for this purpose. A “testbench” project provides subject matter-specific stimuli and expected outcomes, and uses the generic test framework to exercise the sequencing of  ‘scripted’ test scenarios against the application under test.

For interactive demonstration purposes, the Spring framework provided a means to wrap the Java-compiled pilot application ( using the Ciera xtUML-Java model compiler ) as a servlet using a JSON messaging protocol to communicate via WebSocket with GUI pages rendered with HTML/JavaScript. Initially, this required some measure of hand-written Java code to bridge between Spring messaging and xtUML interfaces. But, with experience gained, code generation options to greatly automate these client-server paths were added to the Java model compiler  that built the web-based demonstration pilot.

As projects do, too often, the ambitious effort that initiated this analysis evaporated over time. But, some challenges that arose in its evolution spurred work on enhancement of the xtUML toolset offering. Ths project became a test bed for development of some capabilities.

As employee payment line item data is of interest to both the core application and the user interactions which review it, the testbench component had an interest in that data structure. The BridgePoint xtUML editor offered a mechanism - “Package Reference” - which supported sharing data structure definitions across projects, but the xtUML interpreter - “Verifier” - needed to be updated to support that capability. While it was then possible to share a class definition, replication of an instance across an interface required passing the values of data members individually. Furthermore, as one of the data members of a payment is actually a collection of line item instances, it is attractive to support passing not only single instances as parameter data values across an interface, but also entire instance collections. After adding these capabilities to Verifier, it seemed a natural progression to add them to the Ciera Java model compiler. 

A subsequent discussion on concurrency in xtUML prompted a realization that the computation of individual employee payments are independent and could potentially be evaluated concurrently. A minor change to the implementation moved the computations to state machine instances which realize that concurrency inherent in the application.



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
