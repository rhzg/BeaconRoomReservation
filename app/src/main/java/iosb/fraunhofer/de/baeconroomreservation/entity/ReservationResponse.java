package iosb.fraunhofer.de.baeconroomreservation.entity;

/**
 * Created by sakovi on 14.08.2017.
 */

public class ReservationResponse
{
    private boolean success;

    public ReservationResponse(boolean success)
    {
        this.success = success;
    }

    public ReservationResponse() {
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
