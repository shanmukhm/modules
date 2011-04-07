/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2011 Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, nor its respective contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA AND ITS CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION USA OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.motechproject.server.appointmentreminder;

import org.motechproject.metrics.MetricsAgent;
import org.motechproject.model.MotechEvent;
import org.motechproject.server.appointmentreminder.service.AppointmentReminderService;
import org.motechproject.server.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles Remind Appointment Events
 * 
 * @author Igor
 * 
 */
@Component
public class ReminderCallCompleteEventHandler implements EventListener {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    public final static String HANDLER_ID = "CallReminderCompleted";

    public final static String APPOINTMENT_ID_KEY = "AppointmentID";
    public final static String CALL_DATE_KEY = "CallDate";

    @Autowired
    AppointmentReminderService appointmentReminderService;

    @Autowired
    private MetricsAgent metricsAgent;

	@Override
	public void handle(MotechEvent event) {

        String appointmentId = null;
        Date callDate = null;
        try {
            appointmentId = (String) event.getParameters().get(APPOINTMENT_ID_KEY);
        } catch (ClassCastException e) {
            String errorMessage = "Can not handle call complete. Event: " + event + ". The event is invalid " +
                    APPOINTMENT_ID_KEY + " parameter is not a String";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (appointmentId == null) {
            String errorMessage = "Can not handle call complete. Event: " + event +
                     ". The event is invalid - missing the " +
                    APPOINTMENT_ID_KEY + " parameter";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        try {
            callDate = (Date) event.getParameters().get(CALL_DATE_KEY);
        } catch (ClassCastException e) {
            String errorMessage = "Can not handle the call complete. Event: " + event + ". The event is invalid " +
                    CALL_DATE_KEY + " parameter is not a Date";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (callDate == null) {
            String errorMessage = "Can not handle the call complete. Event: " + event +
                     ". The event is invalid - missing the " +
                    CALL_DATE_KEY + " parameter";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        appointmentReminderService.reminderCallCompleted(appointmentId, callDate);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("appointmentId", appointmentId);
        metricsAgent.logEvent("motech.appointment-reminder.call.complete", parameters);
    }

	@Override
	public String getIdentifier() {
		return HANDLER_ID;
	}

}
