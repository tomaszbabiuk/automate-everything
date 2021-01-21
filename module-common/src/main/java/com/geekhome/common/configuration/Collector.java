package com.geekhome.common.configuration;

import com.geekhome.common.IInvalidateCacheListener;
import com.geekhome.common.INamedObject;
import com.geekhome.common.IdPool;

import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Collector {
    private final IdPool _pool;
    protected boolean _isModified;
    private Runnable _modifiedListener;
    private IInvalidateCacheListener _invalidateCacheListener;

    public IdPool getPool() {
        return _pool;
    }

    protected abstract ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections();

    public abstract void clear();

    public abstract String getPoolPrefix();

    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
    }

    public void addConditionsCollectors(ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
    }

    public void setModifiedListener(Runnable listener) {
        _modifiedListener = listener;
    }

    public void setInvalidateCacheListener(IInvalidateCacheListener listener) {
        _invalidateCacheListener = listener;
    }

    protected Collector(IdPool pool) {
        _pool = pool;
    }

    public void createDynamicObjects() {
    }

    protected void onModified() throws Exception {
        setIsModified(true);
        if (_modifiedListener != null) {
            _modifiedListener.run();
        }
    }

    protected void onInvalidateCache(String what) {
        if (_invalidateCacheListener != null) {
            _invalidateCacheListener.invalidate(what);
        }
    }

    public INamedObject findAny(String uniqueId) {
        for (CollectorCollection<? extends INamedObject> hashtable : buildAllCollections()) {
            if (hashtable.containsKey(uniqueId)) {
                return hashtable.get(uniqueId);
            }
        }

        return null;
    }

    public boolean removeSingleObject(String id) throws Exception {
        for (Hashtable collection : buildAllCollections()) {
            if (collection.containsKey(id)) {
                collection.remove(id);
                onInvalidateCache("all");
                setIsModified(true);
                return true;
            }
        }

        return false;
    }

    public void setIsModified(boolean value) {
        _isModified = value;
    }

    protected String poolUniqueIdIfEmpty(String uniqueId) {
        if (uniqueId.equals("")) {
            return incrementPoolNumber();
        }

        return uniqueId;
    }

    protected String incrementPoolNumber() {
        return _pool.next(getPoolPrefix());
    }

    public boolean isModified() {
        return _isModified;
    }
}