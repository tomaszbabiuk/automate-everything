package com.geekhome.common.configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.ArrayList;

public class JSONArrayList<T> extends ArrayList<T> implements JSONAware {

    @Override
    public String toJSONString() {
        JSONArray json = new JSONArray();
        for(T item : this) {
            json.add(item);
        }
        return json.toJSONString();
    }
}
