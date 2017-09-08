package iosb.fraunhofer.de.baeconroomreservation.entity;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by sakovi on 08.09.2017.
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
