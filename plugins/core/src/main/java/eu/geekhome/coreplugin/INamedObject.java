package eu.geekhome.coreplugin;

import eu.geekhome.services.localization.Resource;

public interface INamedObject
{
    String getId();
    Resource getName();
    Resource getDescription();
}