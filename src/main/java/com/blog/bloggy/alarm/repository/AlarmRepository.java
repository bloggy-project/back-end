package com.blog.bloggy.alarm.repository;

import com.blog.bloggy.alarm.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {

}
