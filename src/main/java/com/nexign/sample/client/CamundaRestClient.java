package com.nexign.sample.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDto;
import org.camunda.bpm.engine.rest.dto.runtime.StartProcessInstanceDto;
import org.camunda.bpm.engine.rest.dto.task.CompleteTaskDto;
import org.camunda.bpm.engine.rest.dto.task.TaskDto;

import java.util.List;

public interface CamundaRestClient {

    @Headers("Content-Type: application/json")
    @RequestLine("POST /process-definition/key/{key}/start")
    ProcessDefinitionDto startProcess(@Param("key") String key, StartProcessInstanceDto request);

    @Headers("Content-Type: application/json")
    @RequestLine("GET /task?processInstanceId={processInstanceId}")
    List<TaskDto> getTasks(@Param("processInstanceId") String processInstanceId);

    @Headers("Content-Type: application/json")
    @RequestLine("GET /task?processInstanceId={processInstanceId}&taskDefinitionKey={taskDefinitionKey}")
    List<TaskDto> getTasks(@Param("processInstanceId") String processInstanceId, @Param("taskDefinitionKey") String taskDefinitionKey);

    @Headers("Content-Type: application/json")
    @RequestLine("POST /task/{taskId}/complete")
    void completeTask(@Param("taskId") String taskId, CompleteTaskDto request);
}
