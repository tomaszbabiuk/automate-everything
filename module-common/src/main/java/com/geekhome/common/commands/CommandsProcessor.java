package com.geekhome.common.commands;

import com.geekhome.common.CodeInvalidException;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.configuration.ChangeStateCommand;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.configuration.ReadValueCommand;
import com.geekhome.common.automation.ControlMode;
import com.geekhome.common.automation.IDeviceAutomationUnit;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.automation.MultistateDeviceAutomationUnit;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommandsProcessor {
    private ILogger _logger = LoggingService.getLogger();
    private SystemInfo _systemInfo;
    private MasterConfiguration _masterConfiguration;
    private MasterAutomation _masterAutomation;
    private ILocalizationProvider _localizationProvider;


    public CommandsProcessor(SystemInfo systemInfo, MasterConfiguration masterConfiguration,
                             MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) {
        _systemInfo = systemInfo;
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _localizationProvider = localizationProvider;
    }

    public CommandResult processCommand(Date receivedDate, String fromUser, String content, boolean addFooter) throws Exception {
        _logger.info(String.format("Processing command '%s' from '%s'", content, fromUser));
        StringBuilder response = new StringBuilder();
        if (_systemInfo.getOperationMode() != OperationMode.Automatic) {
            response.append(_localizationProvider.getValue("C:SystemNotInAutomationMode"));
            return new CommandResult(response.toString());
        } else {
            for (ReadValueCommand readValueCommand : _masterConfiguration.getReadValueCommands().values()) {
                if (readValueCommand.matches(content)) {
                    return processReadValueCommand(readValueCommand, addFooter);
                }
            }

            for (ChangeStateCommand changeStateCommand : _masterConfiguration.getChangeStateCommands().values()) {
                String code = extractCodeFromContent(content);
                String command = extractCommandFromContent(content);
                if (changeStateCommand.matches(command)) {
                    if (receivedDate.getTime() + 60000 < Calendar.getInstance().getTimeInMillis()) {
                        _logger.info(String.format("Ignoring command '%s' from '%s'", content, fromUser));
                        response.append(String.format(_localizationProvider.getValue("C:CommandExpired"), command));
                        return new CommandResult(response.toString());
                    } else {
                        return processChangeStateCommand(fromUser, changeStateCommand, code, addFooter);
                    }
                }
            }

            response.append(_localizationProvider.getValue(content == null ? "C:NoCommand" : "C:UnknownCommand"));
            return new CommandResult(response.toString());
        }
    }

    private String composeResponseFooter(boolean footerEnabled) {
        return footerEnabled ? String.format("\r\n\r\n%s", _localizationProvider.getValue("C:CommandFooter")) : "";
    }

    private CommandResult processReadValueCommand(ReadValueCommand command, boolean addFooter) throws Exception {
        try {
            ArrayList<IDeviceAutomationUnit> automationUnits = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (String deviceId : command.getDevicesIds().split(",")) {
                IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(deviceId);
                sb.append(String.format("%s - %s\r\n", deviceUnit.getName(), deviceUnit.buildEvaluationResult().getInterfaceValue()));
                automationUnits.add(deviceUnit);
            }
            sb.append(composeResponseFooter(addFooter));

            return new CommandResult(sb.toString(), automationUnits);
        } catch (Exception ex) {
            String output = String.format(_localizationProvider.getValue("C:UnknownErrorProcessingCommand"), command.getName());
            return new CommandResult(output);
        }
    }

    private CommandResult processChangeStateCommand(String fromUser, ChangeStateCommand command, String code,  boolean addFooter) throws Exception {
        try {
            ArrayList<IDeviceAutomationUnit> automationUnits = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            MultistateDeviceAutomationUnit deviceUnit = (MultistateDeviceAutomationUnit) _masterAutomation.findDeviceUnit(command.getDeviceId());
            deviceUnit.changeState(command.getStateId(), ControlMode.Manual, code, fromUser);
            sb.append(String.format(_localizationProvider.getValue("C:DeviceStateChanged"), deviceUnit.getName(), deviceUnit.buildEvaluationResult().getInterfaceValue()));
            sb.append(composeResponseFooter(addFooter));
            automationUnits.add(deviceUnit);
            return new CommandResult(sb.toString(), automationUnits);
        } catch (CodeInvalidException cie) {
            return new CommandResult(_localizationProvider.getValue("C:InvalidCodeProcessingCommand"), true, cie.getDevice());
        } catch (Exception ex) {
            String output = String.format(_localizationProvider.getValue("C:UnknownErrorProcessingCommand"), command.getName());
            return new CommandResult(output);
        }
    }

    private String extractCodeFromContent(String content) {
        if (content.indexOf('/') >= 0) {
            String[] contentSplitted = content.split("/");
            return contentSplitted[1];
        }

        return "";
    }

    private String extractCommandFromContent(String content) {
        if (content.indexOf('/') >= 0) {
            String[] contentSplitted =  content.split("/");
            return contentSplitted[0];
        }

        return content;
    }
}
