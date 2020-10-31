package com.geekhome.common.configuration;

import com.geekhome.common.configuration.Dependency;

import java.util.ArrayList;

public interface IMasterDependenciesChecker {
    void checkDependencyInAllDependenciesCheckers(Object obj, ArrayList<Dependency> dependencies, int level);
}