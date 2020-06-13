package com.wslfinc.cf.rating;

import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

public class RatingChangesMap {

    private RatingChangesMap(ContestantResultList contestantResultList) {
        this.ratingChanges = new TreeMap<>();
        for (var contestantResult : contestantResultList.result) {
            ratingChanges.put(contestantResult.handle, contestantResult);
        }
    }

    ContestantResultList getContestantResultList(Iterable<String> handles) {
        var resultList = new ContestantResultList();
        resultList.status = "OK";
        for (var handle : handles) {
            if (ratingChanges.containsKey(handle)) {
                resultList.result.add(ratingChanges.get(handle));
            }
        }
        return resultList;
    }

    public static RatingChangesMap createRatingChangesMap(String jsonRatingChanges) {
        Gson gson = new Gson();
        ContestantResultList resultList = gson.fromJson(jsonRatingChanges, ContestantResultList.class);
        if (!resultList.status.equals("OK")) {
            throw new IllegalArgumentException("Rating changes status isn't OK: " + resultList.status);
        }
        return new RatingChangesMap(resultList);
    }

    private Map<String, ContestantResult> ratingChanges;
}
