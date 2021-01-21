package eu.geekhome.services.configurable;

import com.google.gson.annotations.SerializedName;
import eu.geekhome.services.localization.Resource;

import java.util.List;

public class FieldValidationResult {

    @SerializedName("valid")
    private final boolean _isValid;

    @SerializedName("reasons")
    private final List<Resource> _reasons;

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
