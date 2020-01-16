package com.nexign.sample.delegate;

import com.nexign.sample.dao.Task;
import com.nexign.sample.process.ProcessContext;
import com.nexign.sample.service.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("git")
public class CodeRepository {

    @Autowired
    private TaskService taskService;

    private Random random = new Random();

    public void createPullRequest(DelegateExecution execution) {
        int amountOfConflicts = random.nextInt(2);
        execution.setVariable(ProcessContext.CONTEXT_VALUE_AMOUNT_OF_CONFLICTS, amountOfConflicts);

        String processId = execution.getProcessInstanceId();
        Task task = taskService.getTaskForProcess(processId);
        task.setAmountOfConflicts(amountOfConflicts);
    }
}
