package de.iosb.fraunhofer.baeconroomreservation.entity;



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
