package eu.geekhome.services.automation;

import java.util.ArrayList;

public class Block {
    private String _conditionsIds;
    private String _targetId;

    public String getConditionsIds() {
        return _conditionsIds;
    }

    public void setConditionsIds(String value) {
        _conditionsIds = value;
    }

    public String getTargetId() {
        return _targetId;
    }

    public void setTargetId(String value) {
        _targetId = value;
    }

    public Block(String targetId, String conditionsIds) {
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
