package com.nexign.sample.dao;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private String id;
    private String name;
    private TaskAnalysis analysis;
    private TestingResult testingResult;
    private Integer amountOfConflicts;
    private Status status;
}
