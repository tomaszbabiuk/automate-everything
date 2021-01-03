package com.geekhome.common.configuration;

import java.util.List;

public interface IMasterDependenciesChecker {
    void checkDependencyInAllDependenciesCheckers(Object obj, List<Dependency> dependencies, int level);
}