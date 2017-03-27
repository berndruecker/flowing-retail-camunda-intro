# Simple flowing-retail application using Camunda 

The [https://github.com/flowing/flowing-retail] sample system is the "big brother" of this application and implements an overall order system using a couple of collaborating microservices. 
This application uses the same business story, but implements it as one simple Maven project, forming a Camunda process application, deployable on a container running Camunda (e.g. Tomcat or WildFly). 

# How to run

```
mvn install
cp target/flowing-retail-camunda-intro.war %DEPLOYMENT_FOLDER%/
```

Goto 

![Camunda tasklist](http://localhost:8080/camunda) and start a new process instance from there. 

# What does it demonstrate?

## Flows

You can define [long running flows](https://blog.bernd-ruecker.com/what-are-long-running-processes-b3ee769f0a27#.wpw8hrmux) easily, either [by code](https://github.com/berndruecker/flowing-retail-camunda-intro/blob/master/src/main/java/io/flowing/retail/simpleapp/camunda/CamundaBpmProcessApplication.java#L24):

```
    deploymentBuilder
        .addModelInstance("order.bpmn", Bpmn.createExecutableProcess("Order")
          .startEvent()
              .camundaFormField().camundaId("customerCategory").camundaType("string").camundaLabel("Customer category").camundaFormFieldDone()
              .camundaFormField().camundaId("orderAmount").camundaType("long").camundaLabel("Order amount").camundaFormFieldDone()
          .businessRuleTask().name("Determine risk of fraud")
              .camundaDecisionRef("RiskyOrder")
              .camundaResultVariable("riskyOrder").camundaMapDecisionResult("singleEntry")        
          .serviceTask().name("Do payment")
              .camundaClass(DoPaymentAdapter.class.getName()).camundaAsyncBefore()
          .sendTask().name("Initiate delivery")
              .camundaClass(InitiateDeliveryAdapter.class.getName())
          .receiveTask().name("Wait for delivery")
              .message("MessageDeliveryDone")
          .endEvent()
          .done()
        );
```

or by using the graphical notation BPMN:

![Process model](docs/Order.png)

## Decision tables

You can also leverage decision tables in DMN using Camunda:

![Decision table](docs/dmn.png)
