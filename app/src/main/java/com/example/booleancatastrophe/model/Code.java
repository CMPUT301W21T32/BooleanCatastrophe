package com.example.booleancatastrophe.model;

public class Code {
    private String codeString;
    private String experimentId;
    private Trial trial;

    public Code(){}

    public Code(String codeString, String experimentId, Trial trial) {
        this.codeString = codeString;
        this.experimentId = experimentId;
        this.trial = trial;
    }

    public String getCodeString() {
        return codeString;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public Trial getTrial() {
        return trial;
    }
}
