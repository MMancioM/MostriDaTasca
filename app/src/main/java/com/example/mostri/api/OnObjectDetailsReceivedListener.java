package com.example.mostri.api;

import com.example.mostri.model.ObjectDetailsResponse;
import com.example.mostri.model.ObjectsResponse;

import java.util.List;

public interface OnObjectDetailsReceivedListener {
    void onObjectDetailsReceived(ObjectDetailsResponse objectDetailsResponse);
}
