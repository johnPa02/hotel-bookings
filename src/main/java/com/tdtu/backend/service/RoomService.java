package com.tdtu.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tdtu.backend.model.Room;
import com.tdtu.backend.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        room.setName(roomDetails.getName());
        room.setCapacity(roomDetails.getCapacity());
        room.setPrice(roomDetails.getPrice());
        room.setStatus(roomDetails.getStatus());
        room.setCategory(roomDetails.getCategory());
        room.setImagePath(roomDetails.getImagePath());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        roomRepository.delete(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room findRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }
}
