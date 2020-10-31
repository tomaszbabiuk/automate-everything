package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;

import java.util.ArrayList;

public abstract class DependenciesCheckerModule {
    private IMasterDependenciesChecker _masterDependenciesChecker;

    public abstract void checkDependency(Object obj, ArrayList<Dependency> dependencies, int level);

    protected DependenciesCheckerModule(IMasterDependenciesChecker masterDependenciesChecker) {
        _masterDependenciesChecker = masterDependenciesChecker;
    }

    protected void addDependency(ArrayList<Dependency> dependencies, DependencyType type, INamedObject obj, int level) {
        Dependency newDependency = new Dependency(type, obj.getName(), level);
        if (!dependencies.contains(newDependency)) {
            dependencies.add(newDependency);
        }
        _masterDependenciesChecker.checkDependencyInAllDependenciesCheckers(obj, dependencies, level);
    }
}
