package com.geekhome.common.automation;

public interface IAutomationHook {
    void beforeAutomationLoop();
    void onAutomationLoop();
}