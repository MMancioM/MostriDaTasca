package com.example.mostri.api;

import com.example.mostri.model.ObjectsResponse;

import java.util.List;

public interface OnObjectsReceivedListener {
    void onObjectsReceived(List<ObjectsResponse> objectsResponseList);
}
