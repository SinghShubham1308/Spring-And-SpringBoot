package com.spring_springboot.task_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring_springboot.task_management.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
