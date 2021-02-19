package eu.geekhome.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.geekhome.services.localization.Language;
import eu.geekhome.services.localization.Resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

public class GsonMessageBodyHandler implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {

    private final HashMap<Language, Gson> _gsons = new HashMap<>();

    public GsonMessageBodyHandler() {
        for (Language language : Language.values()) {
            _gsons.put(language, createGson(language));
        }
    }

    private Gson createGson(Language language) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.disableHtmlEscaping()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Resource.class, new ResourceGsonTypeAdapter(language))
                .registerTypeAdapter(Class.class, new ClassGsonTypeAdapter())
                .registerTypeAdapter(RawJson.class, new RawJsonTypeAdapter(language))
                .setFieldNamingStrategy(f -> f.getName().replaceAll("_", ""))
                .create();
    }

    @Context
    HttpHeaders requestHeaders;


    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              java.lang.annotation.Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType,
                           Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8);
            Type jsonType;
            if (type.equals(genericType)) {
                jsonType = type;
            } else {
                jsonType = genericType;
            }
            return _gsons.get(Language.EN).fromJson(streamReader, jsonType);
        } finally {
            try {
                streamReader.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException,
            WebApplicationException {

        Language language = matchLanguage();

        OutputStreamWriter writer = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8);
        try {
            Type jsonType;
            if (type.equals(genericType)) {
                jsonType = type;
            } else {
                jsonType = genericType;
            }
            _gsons.get(language).toJson(object, jsonType, writer);
        } finally {
            writer.close();
        }
    }

    private Language matchLanguage() {
        if (requestHeaders.getAcceptableLanguages().size() > 0) {
            Locale firstLocale = requestHeaders.getAcceptableLanguages().get(0);
            for (Language language : Language.values()) {
                if (language.name().equalsIgnoreCase(firstLocale.getLanguage())) {
                    return language;
                }
            }
        }

        //fallback
        return Language.EN;
    }
}

