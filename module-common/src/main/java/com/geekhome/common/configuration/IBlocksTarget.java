package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;
import com.geekhome.common.localization.ILocalizationProvider;

public interface IBlocksTarget extends INamedObject {
    JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider);
}
