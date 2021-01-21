package eu.geekhome.rest;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.geekhome.services.localization.Language;
import eu.geekhome.services.localization.Resource;

import java.io.IOException;

public class ResourceGsonTypeAdapter extends TypeAdapter<Resource> {

    private final Language _language;

    public ResourceGsonTypeAdapter(Language language) {
        _language = language;
    }

    @Override
    public Resource read(final JsonReader in) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final JsonWriter out, final Resource resource) throws IOException {
        if (resource == null) {
            out.nullValue();
        } else {
            out.value(resource.getValue(_language));
        }
    }
}
