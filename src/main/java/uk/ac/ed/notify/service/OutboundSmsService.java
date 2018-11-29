package uk.ac.ed.notify.service;

import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.entity.Notification;

/**
 * Interface for a service bean responsible for sending SMS messages.  There may be one
 * implementation of this interface in the earliest days of the solution, and another once we
 * integrate with a Notification Hub.
 */
public interface OutboundSmsService {


    String US_DIALING_PREFIX = "+1";

    /**
     * Takes a String that contains a complete US phone number (including area code) in some format
     * and removes any non-digit characters.  The returned String will be exactly 10 characters
     * long.  This method throws an exception if it doesn't end up with what it expects.
     */
    default String normalizePhoneNumber(String phoneNumber) {
        final String rslt = phoneNumber.replaceAll("[\\D]", "");
        if (!rslt.matches("^[0-9]{10}$")) {
            throw new IllegalArgumentException("Not a valid US phone number:  " + rslt);
        }
        return rslt;
    }

    /**
     * Implementations <em>should</em> pass the <code>phoneNumber</code> to
     * {@link #normalizePhoneNumber(String)} before using it.
     */
    void send(DeliveryAddress deliveryAddress, Notification notification);

}
