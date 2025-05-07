package com.example.elasticService.databaseService.repositories;

import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SQLConnectionRepository extends JpaRepository<SQLiteConnectionEntity, Long> {
    List<SQLiteConnectionEntity> findByUserId(Integer userId);  // Fetch connections by user_id


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO connection_table (connection_string, type, user_id) VALUES (:connectionString, :type, :userId)", nativeQuery = true)
    void saveConnectionManually(@Param("connectionString") String connectionString,
                                @Param("type") String type,
                                @Param("userId") Integer userId);
}
