package com.nexign.sample.delegate;

import com.nexign.sample.process.ProcessContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component("cloud")
public class TestingEnvironment {

    public void deploy(DelegateExecution execution) {
        execution.setVariable(ProcessContext.CONTEXT_VALUE_DEPLOYMENT_STATUS, "OK");
    }
}
