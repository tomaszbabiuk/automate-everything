package eu.geekhome.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import com.geekhome.common.localization.Language;
import com.geekhome.common.localization.Resource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonMessageBodyHandler implements MessageBodyWriter<Object>,
        MessageBodyReader<Object> {

    private static final String UTF_8 = "UTF-8";
    private HashMap<Language, Gson> _gsons = new HashMap<>();

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
        throw new UnsupportedOperationException();
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

        Language language = matchLanguage(httpHeaders);

        OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
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

    private Language matchLanguage(MultivaluedMap<String, Object> httpHeaders) {
        if (requestHeaders.getAcceptableLanguages().size() > 0) {
            Locale firstLocale = requestHeaders.getAcceptableLanguages().get(0);
            for (Language language : Language.values()) {
                if (language.name().toLowerCase().equals(firstLocale.getCountry().toLowerCase())) {
                    return language;
                }
            }
        }

        //fallback
        return Language.EN;
    }
}

