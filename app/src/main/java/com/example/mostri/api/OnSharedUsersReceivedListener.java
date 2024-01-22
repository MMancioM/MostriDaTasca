package com.example.mostri.api;

import com.example.mostri.model.UsersShareResponse;

import java.util.List;

public interface OnSharedUsersReceivedListener {
    void onSharedUsersReceived(List<UsersShareResponse> usersShareResponses);
}
