package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public interface Validator<T> {
    Resource getReason();
    boolean validate(T fieldValue);
}

/*
interface Validator<T> {

    @get:StringRes
    var reason: Int

    fun validate(fieldData: T) : Boolean
}

 */
