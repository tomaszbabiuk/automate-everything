package com.geekhome.common.hardwaremanager;

import com.geekhome.common.localization.Resource;

import java.util.ArrayList;

public interface IHardwareManagerAdapterFactory {

    ArrayList<IHardwareManagerAdapter> createAdapters();
    Resource getName();
}