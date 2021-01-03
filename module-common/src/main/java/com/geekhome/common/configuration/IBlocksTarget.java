package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.List;

public interface IBlocksTarget extends INamedObject {
    List<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider);
}
