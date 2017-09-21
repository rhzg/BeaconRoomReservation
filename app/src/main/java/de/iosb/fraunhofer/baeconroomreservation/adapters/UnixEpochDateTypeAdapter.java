package de.iosb.fraunhofer.baeconroomreservation.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Viseslav Sako
 */

public class UnixEpochDateTypeAdapter extends TypeAdapter<Date> {

    private static final TypeAdapter<Date> unixEpochDateTypeAdapter = new UnixEpochDateTypeAdapter();

    private UnixEpochDateTypeAdapter() {
    }

    public static TypeAdapter<Date> getUnixEpochDateTypeAdapter()
    {
        return unixEpochDateTypeAdapter;
    }

    @Override
    public Date read(final JsonReader in) throws IOException
    {
        return new Date(in.nextLong());
    }

    @Override
    @SuppressWarnings("resource")
    public void write(final JsonWriter out, final Date value) throws IOException
    {
        out.value(value.getTime());
    }

}