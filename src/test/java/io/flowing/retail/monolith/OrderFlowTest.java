package io.flowing.retail.monolith;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.*;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class OrderFlowTest {

  @Rule
  @ClassRule
  public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

  @Mock
  private ProcessScenario orderFlow;

  static {
    LogFactory.useSlf4jLogging(); // MyBatis
  }

  @Before
  public void setup() {
    init(rule.getProcessEngine());
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Just tests if the process definition is deployable.
   */
  @Test
  @Deployment(resources = { "Order.bpmn", "RiskyOrder.dmn" })
  public void testParsingAndDeployment() {
    // nothing is done here, as we just want to check for exceptions during
    // deployment
  }

  @Test
  @Deployment(resources = { "Order.bpmn", "RiskyOrder.dmn" })
  public void testHappyPath() {
    when(orderFlow.waitsAtUserTask("UserTask_ApproveOrder")) //
      .thenReturn((task) -> task.complete(Variables.putValue("approved", true)));
    
    when(orderFlow.waitsAtReceiveTask("ReceiveTask_WaitForDelivery")) // 
      .thenReturn((messageSubscription) -> messageSubscription.receive());

    Scenario.run(orderFlow).startByKey( //
        "OrderGraphic", //
        Variables.putValue("customerCategory", "unkown").putValue("orderAmount", 500)).execute();
    
    // "then" part of the test
    verify(orderFlow).hasFinished("EndEvent_OrderShipped");
  }

}
