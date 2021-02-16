package eu.geekhome.services.localization;

public class ResourceWithId extends Resource {

    private final String _id;

    public ResourceWithId(String id, String englishValue, String polishValue) {
        super(englishValue, polishValue);
        _id = id;
    }

    public ResourceWithId(String id, Resource resource) {
        super(resource.getValue(Language.EN), resource.getValue(Language.PL));
        _id = id;
    }

    public String getId() {
        return _id;
    }
}
