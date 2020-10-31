package com.geekhome.common;

import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.localization.ILocalizationProvider;

public class CodeInvalidException extends Exception {
    private final String _message;
    private final IDevice _device;

    public CodeInvalidException(IDevice device, ILocalizationProvider localizationProvider) {
        _message = String.format(localizationProvider.getValue("C:InvalidCodeError"), device.getName().getName());
        _device = device;
    }

    @Override
    public String getMessage() {
        return _message;
    }

    public IDevice getDevice() { return _device; }
}
