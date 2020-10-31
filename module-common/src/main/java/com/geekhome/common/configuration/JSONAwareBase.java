package com.geekhome.common.configuration;

import com.geekhome.common.PersistableReflector;
import org.json.simple.JSONAware;

public class JSONAwareBase implements JSONAware{

    @Override
    public String toJSONString() {
        return PersistableReflector.reflectToJson(this);
    }
}
