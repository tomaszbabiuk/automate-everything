package eu.geekhome.rest;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.geekhome.domain.automation.RawJson;
import eu.geekhome.domain.localization.Language;

import java.io.IOException;

public class RawJsonTypeAdapter extends TypeAdapter<RawJson> {

    private final Language _language;

    public RawJsonTypeAdapter(Language language) {
        _language = language;
    }

    @Override
    public RawJson read(final JsonReader in) throws IOException {
        throw new IOException("Not implemented, RawJson can only be passed one-way server -> client");
    }

    @Override
    public void write(final JsonWriter out, final RawJson input) throws IOException {
        String outputJson = input.getTemplateFunction().invoke(_language);
        out.jsonValue(outputJson);
    }
}
