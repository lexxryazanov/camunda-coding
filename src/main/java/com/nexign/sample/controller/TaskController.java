package com.nexign.sample.controller;

import com.nexign.sample.dao.Task;
import com.nexign.sample.dao.TaskAnalysis;
import com.nexign.sample.dao.TestingResult;
import com.nexign.sample.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coding")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public Task getTask(
        @PathVariable(value = "id") String id
    ) {
        return taskService.getTask(id);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public Task startTask(
        @RequestParam(value = "name", required = false) String name
    ) {
        return taskService.startTask(name);
    }

    @RequestMapping(value = "/task/{id}/analyze", method = RequestMethod.POST)
    public Task analyzeTask(
        @PathVariable(value = "id") String id,
        @RequestBody TaskAnalysis analysis
    ) {
        return taskService.analyzeTask(id, analysis);
    }

    @RequestMapping(value = "/task/{id}/code", method = RequestMethod.POST)
    public Task testTask(
        @PathVariable(value = "id") String id
    ) {
        return taskService.codeTask(id);
    }

    @RequestMapping(value = "/task/{id}/test", method = RequestMethod.POST)
    public Task testTask(
        @PathVariable(value = "id") String id,
        @RequestBody TestingResult testingResult
    ) {
        return taskService.testTask(id, testingResult);
    }


    @RequestMapping(value = "/task/{id}/resolve", method = RequestMethod.POST)
    public Task resolveConflicts(
        @PathVariable(value = "id") String id
    ) {
        return taskService.resolveConflicts(id);
    }
}
