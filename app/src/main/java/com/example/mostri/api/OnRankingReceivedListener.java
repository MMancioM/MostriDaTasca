package com.example.mostri.api;

import com.example.mostri.model.RankingResponse;

import java.util.List;

public interface OnRankingReceivedListener {
    void onRankingReceived(List<RankingResponse> rankingResponses);
}
