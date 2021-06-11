package eu.geekhome.domain.configurable;

import java.util.List;

public interface ConfigurationPersistingService {
    void save(List<FieldValue<?>> fields, Long objectId, Long parentId, String className);
    List<Configurable> loadConfigurablesWithNoParent();
    List<Configurable> loadChildren();
    Configurable loadInstance(Long instanceId);
}

/*
interface ConfigurationPersistingService {

    fun save(fields: List<Field<*>>, objectId: Long, parentId: Long, className: String)
    fun loadConfigurablesWithNoParent() : List<Configurable>
    fun loadChildren(parentId: Long): List<Configurable>
    fun loadInstance(instanceId: Long): Configurable
}
 */
