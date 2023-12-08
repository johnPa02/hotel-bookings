package com.tdtu.backend.repository;

import com.tdtu.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByCategoryName(String name);
    List<Room> findByStatus(String status);
}
