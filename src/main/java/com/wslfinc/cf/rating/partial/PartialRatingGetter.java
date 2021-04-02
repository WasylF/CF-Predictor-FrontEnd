package com.wslfinc.cf.rating.partial;

import com.github.wslf.utils.web.WebReader;

import java.io.IOException;

import static com.wslfinc.cf.Constants.GITHUB_RATING_URL;

public class PartialRatingGetter {
    private PartialRatingGetter() {
        webReader = new WebReader();
        cache = new PartialRatingChangesCache();
    }

    private static final PartialRatingGetter instance = new PartialRatingGetter();

    public static PartialRatingGetter getInstance() {
        return instance;
    }

    public String getPartialRating(int contestId, Iterable<String> handles) {
        // If rating already stored in github there is no need to query backend for it.
        String gitHubUrl = GITHUB_RATING_URL + "/nextRating/contest_" + contestId + ".html";
        if (webReader.isExists(gitHubUrl)) {
            try {
                return webReader.read(gitHubUrl);
            } catch (IOException ex) {
                System.err.println("Couldn't get next rating from github: " + ex.getMessage());
            }
        }

        return cache.getValue(contestId, handles);
    }

    private final PartialRatingChangesCache cache;
    private final WebReader webReader;
}
