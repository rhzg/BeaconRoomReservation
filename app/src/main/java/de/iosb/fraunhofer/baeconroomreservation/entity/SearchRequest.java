package de.iosb.fraunhofer.baeconroomreservation.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Viseslav Sako
 */

public class SearchRequest
{
    @SerializedName("query")
    private String query;

    public SearchRequest(String query)
    {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
