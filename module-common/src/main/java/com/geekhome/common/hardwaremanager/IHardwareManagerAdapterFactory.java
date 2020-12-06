package com.geekhome.common.hardwaremanager;

import org.pf4j.ExtensionPoint;

import java.util.ArrayList;

public interface IHardwareManagerAdapterFactory extends ExtensionPoint {
    ArrayList<? extends IHardwareManagerAdapter> createAdapters();
}