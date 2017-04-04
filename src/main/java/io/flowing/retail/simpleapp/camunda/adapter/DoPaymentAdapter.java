package io.flowing.retail.simpleapp.camunda.adapter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DoPaymentAdapter implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    System.out.println("Now we would do payment using a REST webservice...");
  }

}
