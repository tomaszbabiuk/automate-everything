package com.geekhome.common.configuration;

public class GroupCondition extends ConditionBase {
    private String _conditionIds;
    private GroupMatch _match;

    @Persistable(name="ConditionsIds")
    public String getConditionsIds() {
        return _conditionIds;
    }

    public void setConditionsIds(String value) {
        _conditionIds = value;
    }

    @Persistable(name="Match")
    public GroupMatch getMatch() {
        return _match;
    }

    public void setMatch(GroupMatch value) {
        _match = value;
    }

    public GroupCondition(DescriptiveName name, String conditionsIds, GroupMatch match) {
        super(name);
        setConditionsIds(conditionsIds);
        setMatch(match);
    }
}
