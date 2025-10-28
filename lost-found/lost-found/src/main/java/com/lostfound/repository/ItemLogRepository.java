package com.lostfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lostfound.model.ItemLog;

public interface ItemLogRepository extends JpaRepository<ItemLog, Long> {
}