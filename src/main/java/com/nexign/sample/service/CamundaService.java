package com.nexign.sample.service;

import com.nexign.sample.client.CamundaRestClient;
import com.nexign.sample.process.ProcessContext;
import com.nexign.sample.process.ManualTaskDefinitionKeys;
import org.camunda.bpm.engine.rest.dto.VariableValueDto;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис, который с помощью REST-клиента оповещает Camunda о завершении задач
 */
@Component("camunda-helper-service")
public class CamundaService {

    @Autowired
    private CamundaRestClient client;

    public List<String> getCurrentTasks(String processId) {
        List<TaskDto> tasks = client.getTasks(processId);
        return tasks.stream().map(TaskDto::getTaskDefinitionKey).collect(Collectors.toList());
    }

    public String onTaskStarted(String name) {
        StartProcessInstanceDto request = new StartProcessInstanceDto();
        request.setBusinessKey(name);
        ProcessDefinitionDto response = client.startProcess(ProcessContext.PROCESS_DEFINITION_KEY, request);
        return response.getId();
    }

    public void onTaskAnalysed(String processId) {
        List<TaskDto> tasks = client.getTasks(processId, ManualTaskDefinitionKeys.DEF_KEY_TASK_ANALYSIS);
        tasks.stream().findFirst().ifPresent(task -> client.completeTask(task.getId(), new CompleteTaskDto()));
    }

    public void onTaskImplemented(String processId) {
        List<TaskDto> tasks = client.getTasks(processId, ManualTaskDefinitionKeys.DEF_KEY_IMPLEMENTATION);
        tasks.stream().findFirst().ifPresent(task -> client.completeTask(task.getId(), new CompleteTaskDto()));
    }

    public void onTaskTested(String processId, Long issuesCount) {
        List<TaskDto> tasks = client.getTasks(processId, ManualTaskDefinitionKeys.DEF_KEY_MANUAL_TESTING);
        tasks.stream().findFirst().ifPresent(task -> {
            CompleteTaskDto request = new CompleteTaskDto();
            HashMap<String, VariableValueDto> variables = new HashMap<>();
            VariableValueDto value = new VariableValueDto();
            value.setValue(issuesCount);
            variables.put(ProcessContext.CONTEXT_VALUE_ISSUES_COUNT, value);
            request.setVariables(variables);
            client.completeTask(task.getId(), request);
        });
    }

    public void onConflictsResolved(String processId) {
        List<TaskDto> tasks = client.getTasks(processId, ManualTaskDefinitionKeys.DEF_KEY_RESOLVE_CONFLICTS);
        tasks.stream().findFirst().ifPresent(task -> client.completeTask(task.getId(), new CompleteTaskDto()));
    }

}