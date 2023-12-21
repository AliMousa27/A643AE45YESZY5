package com.toothtrek.bookings.request.timeslot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.serializer.json.TimestampSerializer;
import com.toothtrek.bookings.view.Timeslot.TimeslotDentist;
import com.toothtrek.bookings.view.Timeslot.TimeslotDentistDto;

@Configuration
public class TimeslotGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    TimeslotRepository timeslotRepository;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void handle(MqttMessage request) {
        List<Office> allOffices = officeRepository.findAll();
        List<HashMap<String, Object>> officeMapList = new ArrayList<>();

        for (Office office : allOffices) {

            List<TimeslotDentist> timeslotDentistList = timeslotRepository
                    .findTimeslotsWithDentistsByOffice(office.getId());
            List<TimeslotDentistDto> timeslotDentistDtoList = new ArrayList<>();

            for (TimeslotDentist timeslotDentist : timeslotDentistList) {

                if (timeslotDentist.getTimeslot().getState() == Timeslot.State.cancelled) {
                    continue;
                }

                // If the timeslot is booked, skip it.
                if (timeslotRepository.isBooked(timeslotDentist.getTimeslot().getId())) {
                    continue;
                }

                // Convert the timeslotDentist to a data transfer object (DTO).
                TimeslotDentistDto timeslotDentistDto = TimeslotDentistDto.toDto(timeslotDentist);
                timeslotDentistDtoList.add(timeslotDentistDto);
            }

            HashMap<String, Object> officeMap = new HashMap<String, Object>();
            officeMap.put("office", officeRepository.findById(office.getId()).get());
            officeMap.put("timeslots", timeslotDentistDtoList);

            officeMapList.add(officeMap);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();
        String json = gson.toJson(officeMapList);

        responseHandler.reply(ResponseStatus.SUCCESS, json, request);
    }

}
