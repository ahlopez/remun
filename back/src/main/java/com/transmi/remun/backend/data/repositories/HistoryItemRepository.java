package com.transmi.remun.backend.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transmi.remun.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long>
{
}// HistoryItemRepository
