package com.tenpo.percentage.respository;

import com.tenpo.percentage.model.CallHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallHistoryRepository extends JpaRepository<CallHistoryEntity, Long> {}
