package com.nexign.sample.process;

/**
 * Константы связанные с описанием бизнес-процесса в файле coding-process.bpmn
 */
public class ProcessContext {

    /**
     * Уникальный ключ процесса 
     */
    public static final String PROCESS_DEFINITION_KEY = "taskDevelopmentProcess";


    /**
     * Переменные использующиеся в контексте процесса
     */
    public static final String CONTEXT_VALUE_DEPLOYMENT_STATUS = "deploymentStatus";
    public static final String CONTEXT_VALUE_ISSUES_COUNT = "issuesCount";
    public static final String CONTEXT_VALUE_AMOUNT_OF_CONFLICTS = "amountOfConflicts";
}
