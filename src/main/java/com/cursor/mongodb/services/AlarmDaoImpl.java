package com.cursor.mongodb.services;

import com.cursor.mongodb.models.Alarm;
import com.cursor.mongodb.models.DayOfWeek;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AlarmDaoImpl {

    private final MongoCollection<Document> myAlarms;

    @Autowired
    public AlarmDaoImpl(MongoCollection<Document> myAlarms) {
        this.myAlarms = myAlarms;
    }

    public FindIterable<Document> add(Alarm alarmParam) {
        Document newAlarm = new Document();
        newAlarm.put("alarmTimeHour", alarmParam.getAlarmTime().getHour());
        newAlarm.put("alarmTimeMinute", alarmParam.getAlarmTime().getMinute());
        newAlarm.put("alarmSchedule", alarmParam.getAlarmSchedule()
                .stream().map(Enum::name).collect(Collectors.toList()));
        myAlarms.insertOne(newAlarm);
        return myAlarms.find(new Document(newAlarm));
    }

    public void editAlarmTime(Alarm alarmParam) {
        myAlarms.updateOne(new Document("_id", alarmParam.getAlarmId()),
                new Document(Map.of("$set",
                        new Document(Map.of("alarmTimeHour", alarmParam.getAlarmTime().getHour(),
                                "alarmTimeMinute", alarmParam.getAlarmTime().getMinute())))));
    }

    public void editAlarmSchedule(Alarm alarmParam) {
        myAlarms.updateOne(Filters.eq("_id", alarmParam.getAlarmId()),
                Updates.set("alarmSchedule", alarmParam.getAlarmSchedule().stream().map(DayOfWeek::name).collect(Collectors.toList())));
    }

    public Document deleteById(String id) {
        return myAlarms.findOneAndDelete(new Document("_id", new ObjectId(id)));
    }

    public FindIterable<Document> getById(String id) {
        return myAlarms.find(new Document("_id", new ObjectId(id)));
    }

    public FindIterable<Document> getAllAlarms() {
        return myAlarms.find();
    }
}
