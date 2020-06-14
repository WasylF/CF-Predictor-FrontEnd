package com.wslfinc.cf.rating.partial;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.gson.Gson;
import com.wslfinc.cf.JsonReader;
import com.wslfinc.cf.rating.RatingChangesMap;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.wslfinc.cf.Constants.BACK_END_URL;
import static com.wslfinc.cf.Constants.JSON_FAIL_STRING;

public class PartialRatingChanges {

    private int timeToRefreshSeconds;
    LoadingCache<Integer, RatingChangesMap> cache;


    public PartialRatingChanges() {
        this.timeToRefreshSeconds = 100;
        initCache();
    }

    private void initCache() {
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(timeToRefreshSeconds, TimeUnit.SECONDS)
                .build(new PartialRatingChangesReloader());
    }

    public String getValue(int contestId, Iterable<String> handles) {
        try {
            var ratingChangesMap = cache.get(contestId);
            var ratingChangesList = ratingChangesMap.getContestantResultList(handles);
            return gson.toJson(ratingChangesList);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.err.println("Couldn't get next rating, because of " + e);
            return JSON_FAIL_STRING;
        }
    }

    public Set<Integer> getCachedKeys() {
        return cache.asMap().keySet();
    }

    public void remove(int contestId) {
        cache.invalidate(contestId);
    }

    private final Gson gson = new Gson();
}
