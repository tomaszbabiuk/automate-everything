package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FieldValidationResult {

    @SerializedName("valid")
    private boolean _isValid;

    @SerializedName("reasons")
    private List<Resource> _reasons;

    public FieldValidationResult(boolean isValid, List<Resource> reasons) {
        _isValid = isValid;
        _reasons = reasons;
    }

    public boolean isValid() {
        return _isValid;
    }

    public List<Resource> getReasons() {
        return _reasons;
    }
}
