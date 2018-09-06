package com.wslfinc.cf;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.json.JSONObject;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.wslfinc.cf.Constants.BACK_END_URL;
import static com.wslfinc.cf.Constants.JSON_FAIL_STRING;

/**
 * @author Wsl_F
 */
public class RatingChanges {


  final ExecutorService executor = Executors.newFixedThreadPool(2);

  private int timeToRefreshSeconds;
  LoadingCache<Integer, String> cache;


  public RatingChanges() {
    this.timeToRefreshSeconds = 5;
    initCache();
  }

  private void initCache() {
    cache = CacheBuilder.newBuilder()
      .refreshAfterWrite(timeToRefreshSeconds, TimeUnit.SECONDS)
      .build(
        new CacheLoader<>() {
          @Override
          public ListenableFuture<String> reload(Integer contestId, String oldValue) {
            ListenableFutureTask<String> task = ListenableFutureTask.create(() -> {
              System.out.println("Updating rating prediction for contest " + contestId + " time " + System.currentTimeMillis());
              String requestURL = BACK_END_URL + "/GetNextRatingServlet?contestId=" + contestId;
              try {
                JSONObject json = JsonReader.read(requestURL);
                return json.toString();
              } catch (Exception ex) {
                System.err.println("Couldn't get request results " + requestURL
                  + "\n" + ex.getMessage());
              }

              return JSON_FAIL_STRING;
            });
            executor.execute(task);
            return task;
          }

          @Override
          public String load(Integer contestId) {
            return JSON_FAIL_STRING;
          }
        });
  }

  public String getValue(int contestId) {
    try {
      return cache.get(contestId);
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
}
