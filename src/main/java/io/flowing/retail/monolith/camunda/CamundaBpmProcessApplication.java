package io.flowing.retail.monolith.camunda;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.model.bpmn.Bpmn;

import com.camunda.consulting.util.LicenseHelper;
import com.camunda.consulting.util.UserGenerator;

import io.flowing.retail.monolith.camunda.adapter.DoPaymentAdapter;
import io.flowing.retail.monolith.camunda.adapter.InitiateDeliveryAdapter;

/**
 * Process Application exposing this application's resources the process engine. 
 */
@ProcessApplication
public class CamundaBpmProcessApplication extends ServletProcessApplication {
  
  public void createDeployment(String processArchiveName, DeploymentBuilder deploymentBuilder) {
    
    deploymentBuilder
        .addModelInstance("order.bpmn", Bpmn.createProcess("Order").executable()
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
  }
  
  @PostDeploy
  public void setupEnvironmentForDemo(ProcessEngine engine) {
    LicenseHelper.setLicense(engine);
    UserGenerator.createDefaultUsers(engine);
  }
   
}
