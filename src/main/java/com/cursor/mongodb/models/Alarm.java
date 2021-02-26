package com.cursor.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "my-alarms")
public class Alarm implements Serializable {
    @Id
    private ObjectId alarmId;
    private LocalTime alarmTime;
    private List<DayOfWeek> alarmSchedule;

    public Alarm(LocalTime alarmTime, List<DayOfWeek> alarmSchedule) {
        this.alarmTime = alarmTime;
        this.alarmSchedule = alarmSchedule;
    }
}
