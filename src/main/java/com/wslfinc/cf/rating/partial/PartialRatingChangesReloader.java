package com.wslfinc.cf.rating.partial;

import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.wslfinc.cf.JsonReader;
import com.wslfinc.cf.rating.RatingChangesMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wslfinc.cf.Constants.BACK_END_URL;

public class PartialRatingChangesReloader extends CacheLoader<Integer, RatingChangesMap> {

    @Override
    public ListenableFuture<RatingChangesMap> reload(Integer contestId, RatingChangesMap oldValue) {
        ListenableFutureTask<RatingChangesMap> task = ListenableFutureTask.create(() -> {
            System.out.println("Updating rating prediction for contest " + contestId + " time " + System.currentTimeMillis());
            String requestURL = BACK_END_URL + "/GetNextRatingServlet?contestId=" + contestId;
            try {
                String jsonResponse = JsonReader.doGet(requestURL);
                RatingChangesMap ratingChangesMap = RatingChangesMap.createRatingChangesMap(jsonResponse);
                System.out.printf("Parsed rating changes for %d contestants in contest %d.\n", ratingChangesMap.size(), contestId);
                return ratingChangesMap;
            } catch (Exception ex) {

                if (!oldValue.isEmpty()) {
                    return oldValue;
                }
            }
            return new RatingChangesMap();
        });

        executor.execute(task);
        return task;
    }

    @Override
    public RatingChangesMap load(Integer contestId) {
        return new RatingChangesMap();
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
}

