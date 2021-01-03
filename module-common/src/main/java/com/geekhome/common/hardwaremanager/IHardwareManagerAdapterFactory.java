package com.geekhome.common.hardwaremanager;

import java.util.ArrayList;

public interface IHardwareManagerAdapterFactory {
    ArrayList<? extends IHardwareManagerAdapter> createAdapters();
}