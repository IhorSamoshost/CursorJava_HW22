package com.cursor.mongodb.controllers;

import com.cursor.mongodb.models.Alarm;
import com.cursor.mongodb.services.AlarmDaoImpl;
import com.cursor.mongodb.models.DayOfWeek;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.FindIterable;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sun.istack.NotNull;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/")
public class AlarmController {

    private final AlarmDaoImpl alarmDao;

    @Autowired
    public AlarmController(AlarmDaoImpl alarmDao) {
        this.alarmDao = alarmDao;
    }

    @GetMapping
    public String getAllAlarms() {
        return findIterableDocumentToString(alarmDao.getAllAlarms());
    }

    @GetMapping("{id}")
    public String getAlarmById(@PathVariable("id") String id) {
        return findIterableDocumentToString(alarmDao.getById(id));
    }

    @PostMapping
    public String createNewAlarm(@NotNull @RequestBody AlarmRequest newAlarm) {
        return findIterableDocumentToString(alarmDao.add(newAlarm.toAlarm()));
    }

    @PutMapping("/time/{id}")
    public String updateAlarmTime(@PathVariable("id") String id,
                                  @NotNull @RequestBody TimeUpdateRequest updatedTime) {
        Alarm changedAlarm = updatedTime.toAlarm();
        changedAlarm.setAlarmId(new ObjectId(id));
        alarmDao.editAlarmTime(changedAlarm);
        return findIterableDocumentToString(alarmDao.getById(id));
    }

    @PutMapping("/schedule/{id}")
    public String updateAlarmSchedule(@PathVariable("id") String id,
                                      @NotNull @RequestBody ScheduleUpdateRequest updatedSchedule) {
        Alarm changedAlarm = updatedSchedule.toAlarm();
        changedAlarm.setAlarmId(new ObjectId(id));
        alarmDao.editAlarmSchedule(changedAlarm);
        return findIterableDocumentToString(alarmDao.getById(id));
    }

    @DeleteMapping("{id}")
    public String deleteAlarm(@PathVariable("id") String id) {
        Document deletedAlarm = alarmDao.deleteById(id);
        return deletedAlarm != null ? alarmDao.deleteById(id).toJson() : "";
    }

    private String findIterableDocumentToString(FindIterable<Document> documentFromMongo) {
        return StreamSupport.stream(documentFromMongo.spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    @Data
    private static class AlarmRequest {
        private int alarmHours;
        private int alarmMinutes;
        private String[] alarmDays;

        private Alarm toAlarm() {
            return new Alarm(LocalTime.of(alarmHours, alarmMinutes),
                    Arrays.stream(alarmDays).map(DayOfWeek::valueOf).collect(Collectors.toList()));
        }
    }

    @Data
    private static class TimeUpdateRequest {
        private int alarmHours;
        private int alarmMinutes;

        private Alarm toAlarm() {
            return new Alarm(LocalTime.of(alarmHours, alarmMinutes), null);
        }
    }

    @Data
    private static class ScheduleUpdateRequest {
        private String[] alarmDays;

        private Alarm toAlarm() {
            return new Alarm(null,
                    Arrays.stream(alarmDays).map(DayOfWeek::valueOf).collect(Collectors.toList()));
        }
    }
}
