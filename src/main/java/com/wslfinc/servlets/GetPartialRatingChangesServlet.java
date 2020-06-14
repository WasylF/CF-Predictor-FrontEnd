package com.wslfinc.servlets;

import com.wslfinc.cf.rating.partial.PartialRatingGetter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;

public class GetPartialRatingChangesServlet extends HttpServlet {

    String getPartialRatingChanges(HttpServletRequest request) {
        String contestIdString = request.getParameter("contestId");
        if (contestIdString != null) {
            int contestId = Integer.parseInt(contestIdString);
            String handlesString = request.getParameter("handles");
            if (handlesString != null) {
                var handles = Arrays.asList(handlesString.split(","));
                return PartialRatingGetter.getInstance().getPartialRating(contestId, handles);
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter out = response.getWriter()) {
            try {
                String ratingChanges = getPartialRatingChanges(request);

                response.setStatus(200);
                out.write(ratingChanges);
                out.flush();
            } catch (Exception ex) {
                response.setStatus(500);
                out.write("{ \"status\": \"FAIL\" }");
                out.flush();
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
