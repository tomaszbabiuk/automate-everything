package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

import java.util.ArrayList;

public class Block extends NamedObject {
    private String _conditionsIds;
    private String _targetId;

    @Persistable(name="ConditionsIds")
    public String getConditionsIds() {
        return _conditionsIds;
    }

    public void setConditionsIds(String value) {
        _conditionsIds = value;
    }

    @Persistable(name="TargetId")
    public String getTargetId() {
        return _targetId;
    }

    public void setTargetId(String value) {
        _targetId = value;
    }

    public Block(DescriptiveName name, String targetId, String conditionsIds) {
        super(name);
        setTargetId(targetId);
        setConditionsIds(conditionsIds);
    }

    public ArrayList<String> getUniqueConditionsIds() {
        ArrayList<String> uniqueIds = new ArrayList<>();
        for (String conditionId : getConditionsIds().split(",")) {
            if (conditionId.startsWith("!")) {
                conditionId = conditionId.substring(1);
            }

            if (!uniqueIds.contains(conditionId)) {
                uniqueIds.add(conditionId);
            }
        }

        return uniqueIds;
    }
}
