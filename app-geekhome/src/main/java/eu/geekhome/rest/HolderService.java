package eu.geekhome.rest;

import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;

@Service
public class HolderService<T> {

    private T _instance;

    public T getInstance() {
        return _instance;
    }

    public HolderService(Application app, Class<T> type) throws Exception {
        for (Object singleton : app.getSingletons()) {
            if (type.isAssignableFrom(singleton.getClass())) {
                _instance = (T) singleton;
                break;
            }
        }
        if (_instance == null) {
            throw new Exception("There's no singleton of type " + type.getName() +" in the app singletons container");
        }
    }
}