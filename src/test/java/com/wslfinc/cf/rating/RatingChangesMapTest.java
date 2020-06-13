package com.wslfinc.cf.rating;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingChangesMapTest {

    @Test
    void successCase() {
        String json = "{" +
                "\"status\":\"OK\"," +
                "\"result\":" +
                "[" +
                "{\"oldRating\":1400,\"newRating\":1440,\"seed\":33.5,\"rank\":49,\"handle\":\"some_handle_1\"}," +
                "{\"oldRating\":1400,\"newRating\":1500,\"seed\":33.5,\"rank\":30,\"handle\":\"some_handle_2\"}" +
                "]" +
                "}";
        RatingChangesMap map = RatingChangesMap.createRatingChangesMap(json);
        var handle1List = map.getContestantResultList(List.of("some_handle_1"));
        assertEquals(handle1List.status, "OK");
        assertEquals(handle1List.result.size(), 1);
        assertEquals(handle1List.result.get(0).handle, "some_handle_1");
        assertEquals(handle1List.result.get(0).newRating, 1440);
    }

    @Test
    void failedToCreate() {
        String json = "{\"status\":\"ERROR\"}";
        assertThrows(IllegalArgumentException.class, () -> RatingChangesMap.createRatingChangesMap(json));
    }
}