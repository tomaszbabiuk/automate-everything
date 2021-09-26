package eu.automateeverything.rest;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ClassGsonTypeAdapter extends TypeAdapter<Class> {

    @Override
    public Class read(final JsonReader in) throws IOException {
        String clazz = in.nextString();
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(final JsonWriter out, final Class clazz) throws IOException {
        out.value(clazz.getSimpleName());
    }
}
