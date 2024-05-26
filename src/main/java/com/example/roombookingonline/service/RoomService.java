package com.example.roombookingonline.service;

import com.example.roombookingonline.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RoomService {

    List<String> getTypeBed();

    List<RoomDetailEntity.Status> getStatus();

    List<RoomTypeImageEntity.photoName> getPhotoName();

    RoomTypeEntity findRoomTypeById(Long roomTypeId);

    RoomTypeDetailEntity findRoomTypeDetailById(Long roomTypeDetailId);

    List<RoomTypeEntity> getRoomType();

    List<String> getRoomTypeNameExits();

    List<RoomTypeEntity> getAllRoomType();

    List<RoomTypeEntity> getAllRoomTypeActive();

    List<RoomTypeEntity> getAllTypeByName(String name);

    Map<RoomTypeEntity, Integer> findByDateAndCapacity(LocalDateTime startDate, LocalDateTime endDate, int capacity);

    RoomDetailEntity selectRoomForOrder(Long roomTypeId, LocalDateTime startDate, LocalDateTime endDate);

    void createRoomType(RoomTypeEntity roomTypeEntity, MultipartFile image, String des) throws IOException;

    void updateRoomTypeImage(RoomTypeEntity roomTypeSaved, MultipartFile image,
                             RoomTypeImageEntity.photoName name, String des) throws IOException;

    RoomTypeEntity updateRoomTypeDetail(RoomTypeEntity roomTypeEntity, RoomTypeDetailEntity roomTypeDetailEntity);

    List<RoomTypeImageEntity> getNewRoomTypeImage();

    @Transactional
    void saveRoom(RoomEntity room, RoomDetailEntity roomDetail);

    List<RoomEntity> findAllRoom();

    void deleteRoomType(Long roomTypeId);

    void disableRoomType(Long roomTypeId);

    void enableRoomType(Long roomTypeId);

    void enableRoom(Long roomNumber);

    void disableRoom(Long roomNumber);

    RoomDetailEntity findRoomDetailById(Long roomId);

    List<RoomBedTypeEntity> getAllBedType();

    void saveBedType(RoomBedTypeEntity roomBedTypeEntity);

    void deleteBedType(Long roomBedTypeId);
}
