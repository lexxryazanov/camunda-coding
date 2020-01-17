package com.nexign.sample.service;

import com.nexign.sample.controller.TaskNotFoundException;
import com.nexign.sample.dao.Status;
import com.nexign.sample.dao.Task;
import com.nexign.sample.dao.TaskAnalysis;
import com.nexign.sample.dao.TestingResult;
import com.nexign.sample.process.ManualTaskDefinitionKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Сервис для работы с бизнес-сущностями
 */
@Component("coding-task-service")
public class TaskService {

    @Autowired
    private CamundaService camundaService;

    private Map<String, Task> tasks = new HashMap<>();

    private Map<String, String> task2process = new HashMap<>();
    private Map<String, Task> process2task = new HashMap<>();

    public Task getTask(String id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException();
        }

        return getTaskWithStatus(tasks.get(id));
    }

    public Task getTaskForProcess(String processId) {
        if (!process2task.containsKey(processId)) {
            throw new TaskNotFoundException();
        }

        return getTaskWithStatus(process2task.get(processId));
    }

    public Task startTask(String taskName) {
        String processId = camundaService.onTaskStarted(taskName);

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName(taskName);

        tasks.put(task.getId(), task);
        task2process.put(task.getId(), processId);
        process2task.put(processId, task);

        return getTaskWithStatus(task);
    }

    public Task analyzeTask(String taskId, TaskAnalysis analysis) {
        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException();
        }

        String processId = task2process.get(taskId);
        camundaService.onTaskAnalysed(processId);

        Task task = tasks.get(taskId);
        task.setAnalysis(analysis);

        return getTaskWithStatus(task);
    }

    public Task codeTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException();
        }

        String processId = task2process.get(taskId);
        camundaService.onTaskImplemented(processId);

        return getTaskWithStatus(tasks.get(taskId));
    }

    public Task testTask(String taskId, TestingResult testingResult) {
        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException();
        }

        String processId = task2process.get(taskId);
        camundaService.onTaskTested(processId, testingResult.getIssuesCount());

        Task task = tasks.get(taskId);
        task.setTestingResult(testingResult);
        return getTaskWithStatus(task);
    }

    public Task resolveConflicts(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new TaskNotFoundException();
        }

        String processId = task2process.get(taskId);
        camundaService.onConflictsResolved(processId);

        Task task = tasks.get(taskId);
        task.setAmountOfConflicts(0);
        return getTaskWithStatus(task);
    }

    private Task getTaskWithStatus(Task task) {
        String processId = task2process.get(task.getId());
        List<String> tasks = camundaService.getCurrentTasks(processId);
        task.setExpectedActions(tasks);
        if (tasks.isEmpty()) {
            task.setStatus(Status.DONE);
        } else {
            String taskName = tasks.get(0);
            switch (taskName) {
                case ManualTaskDefinitionKeys.DEF_KEY_TASK_ANALYSIS:
                    task.setStatus(Status.ASSIGNED);
                    break;
                case ManualTaskDefinitionKeys.DEF_KEY_IMPLEMENTATION:
                case ManualTaskDefinitionKeys.DEF_KEY_MANUAL_TESTING:
                    task.setStatus(Status.IN_PROGRESS);
                    break;
                case ManualTaskDefinitionKeys.DEF_KEY_RESOLVE_CONFLICTS:
                    task.setStatus(Status.IN_REVIEW);
                    break;
                default:
                    task.setStatus(null);
            }
        }
        return task;
    }
}
