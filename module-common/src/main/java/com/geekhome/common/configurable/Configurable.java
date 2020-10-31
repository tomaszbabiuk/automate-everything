package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

import java.util.List;

public interface Configurable {
    List<FieldDefinition<?>> getFieldDefinitions();
    Class<? extends Configurable> getParent();
    Resource getAddNewRes();
    Resource getTitleRes();
    String getIconName();
}

/*
interface Configurable {

    fun getFields() : List<Field<*>>
    val attachableTo : List<KClass<out Configurable>>
    var persistenceId: Long

    @get:StringRes
    val addNewItemResId: Int

    @get:DrawableRes
    val iconResId: Int
}
*/
