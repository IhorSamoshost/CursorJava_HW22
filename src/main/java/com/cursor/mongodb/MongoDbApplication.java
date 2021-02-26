package com.cursor.mongodb;

import com.cursor.mongodb.models.Alarm;
import com.cursor.mongodb.models.DayOfWeek;
import com.cursor.mongodb.services.AlarmDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class MongoDbApplication {

    private final AlarmDaoImpl alarmDao;

    @Autowired
    public MongoDbApplication(AlarmDaoImpl alarmDao) {
        this.alarmDao = alarmDao;
    }

    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplication.class, args);
    }

    @PostConstruct
    private void createOneAlarmForBegin() {
        alarmDao.add(new Alarm(LocalTime.of(13, 20),
                List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)));
    }
}
