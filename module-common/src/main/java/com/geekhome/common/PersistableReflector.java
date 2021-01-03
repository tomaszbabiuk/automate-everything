package com.geekhome.common;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.logging.LoggingService;

import java.io.IOException;
import java.lang.reflect.Method;

public class PersistableReflector {

    public static String reflectToJson(Object obj) {
        return null;
//        final JSONObject json = new JSONObject();
//        reflect(obj, new IObjectReflectedDelegate() {
//            @Override
//            public void execute(String persistableName, boolean readOnly, Object persistableValue) {
//                json.put(persistableName, persistableValue);
//            }
//        });
//
//        return json.toString();
    }

    public interface IObjectReflectedDelegate {
        void execute(String persistableName, boolean readOnly, Object persistableValue) throws IOException;
    }

    public static void reflect(Object obj, IObjectReflectedDelegate objectReflectedDelegate) {
        Class c = obj.getClass();

        for (Method f : c.getMethods()) {
            Persistable persistable = f.getAnnotation(Persistable.class);
            if (persistable != null) {
                try {
                    Object value = f.invoke(obj);
                    objectReflectedDelegate.execute(persistable.name(), persistable.readOnly(), value);
                } catch (Exception e) {
                    LoggingService.getLogger().error("Cannot reflect: " + persistable.name() + " of " + c.getName(), e);
                }
            }
        }
    }
}
