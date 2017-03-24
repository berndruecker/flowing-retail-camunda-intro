package io.flowing.retail.monolith;

import javax.swing.text.StyleContext.SmallAttributeSet;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.variable.Variables;

public class SampleCode {

  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    Class clazz = SampleCode.class;

    {
      ProcessEngine engine = new StandaloneInMemProcessEngineConfiguration().buildProcessEngine();
      engine.getRuntimeService().startProcessInstanceByKey("Order", Variables.putValue("a", 5));
    }
    
    {
      DmnEngine engine = new DefaultDmnEngineConfiguration().buildEngine();
      DmnDecision decision = engine.parseDecisions(clazz.getResourceAsStream("")).get(0);
      engine.evaluateDecision(decision, Variables.putValue("a", 5));
    }

    
  }
}
