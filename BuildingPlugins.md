# This section describes how to add new plugins
in order to add new plugin
1) Create new folder on your drive
2) Go to this new folder
3) Clone automate-everything
4) Create new folder in plugins directory i.e. 'my-super-plugin'
5) Create build.gradle in 'plugins/my-super-plugin'. You can use this template:
```groovy
plugins {
    id 'org.jetbrains.kotlin.jvm'
    id 'org.jetbrains.kotlin.kapt'
}

dependencies {
    //projects
    shadow project(':modules:data')
    shadow project(':modules:domain')

    //pf4j
    shadow "org.pf4j:pf4j:$pf4jVersion"
    kapt "org.pf4j:pf4j:$pf4jVersion"
    annotationProcessor "org.pf4j:pf4j:$pf4jVersion"

    //kotlin
    shadow "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    shadow "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion"
}
```

6) Create src folder
7) Decide the name of your package name (i.e. com.my-super.website)
8) Create main class for the plugin (in plugins/my-super-plugin/src/com/my-super/website/MySuperPlugin.kt)
//TODO:

## Troubleshooting
### I've added new plugin, but it's not visible by Automate Everything
Make sure the pluginId in gradle.properties file is not taken by any other plugin.
For more troubleshooting put a breakpoint in 'loadPlugins' method of AbstractPluginManager.java class and debug it.