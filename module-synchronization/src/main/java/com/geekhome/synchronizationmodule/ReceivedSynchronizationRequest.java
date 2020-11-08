package com.geekhome.synchronizationmodule;

import com.google.gson.annotations.SerializedName;

public class ReceivedSynchronizationRequest extends SynchronizationRequest {

    @SerializedName("from")
    private String _from;

    @SerializedName("id")
    private Long _id;

    public ReceivedSynchronizationRequest(String from, Long id, SynchronizationRequest synchronizationRequest) {
        super(synchronizationRequest.getType(), synchronizationRequest.getParameter(), synchronizationRequest.getValue(), synchronizationRequest.getCode());
        _from = from;
        _id = id;
    }

    public String getFrom() {
        return _from;
    }

    public Long getId() {
        return _id;
    }
}
