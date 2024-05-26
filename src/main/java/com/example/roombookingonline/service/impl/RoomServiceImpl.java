package com.example.roombookingonline.service.impl;

import com.example.roombookingonline.convertor.SortMap;
import com.example.roombookingonline.entity.*;
import com.example.roombookingonline.repository.*;
import com.example.roombookingonline.service.RoomService;
import com.example.roombookingonline.ulti.FileUploadUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomOrderRepository roomOrderRepository;
    @Autowired
    private RoomTypeImageRepository roomTypeImageRepository;
    @Autowired
    private RoomDetailRepository roomDetailRepository;
    @Autowired
    private RoomTypeDetailRepository roomTypeDetailRepository;
    @Autowired
    private RoomBedTypeRepository roomBedTypeRepository;

    private final RoomDetailEntity roomDetailEntity = new RoomDetailEntity();
    private final RoomTypeImageEntity roomTypeImageEntity = new RoomTypeImageEntity();

    @Override
    public List<String> getTypeBed(){
        return roomBedTypeRepository.getAllTypeBed();
    }
    @Override
    public List<RoomDetailEntity.Status> getStatus(){
        return roomDetailEntity.getAllStatus();
    }
    @Override
    public List<RoomTypeImageEntity.photoName> getPhotoName(){
        return roomTypeImageEntity.getAllPhotoName();
    }
//
//
    @Override
    public RoomTypeEntity findRoomTypeById(Long roomTypeId){
        return roomTypeRepository.findById(roomTypeId).orElseThrow();
    }
    @Override
    public RoomTypeDetailEntity findRoomTypeDetailById(Long roomTypeDetailId){
        return roomTypeDetailRepository.findById(roomTypeDetailId).orElseThrow();
    }
    @Override
    public List<RoomTypeEntity> getRoomType(){
        return roomTypeRepository.findRoomType();
    }
    @Override
    public List<String> getRoomTypeNameExits(){
        return roomTypeRepository.findName();
    }
    @Override
    public List<RoomTypeEntity> getAllRoomType(){
        return roomTypeRepository.findAll();
    }
    @Override
    public List<RoomTypeEntity> getAllRoomTypeActive(){
        return roomTypeRepository.findRoomTypeActive();
    }
    @Override
    public List<RoomTypeEntity> getAllTypeByName(String name){
        return roomTypeRepository.findByNameAndActiveIsTrue(name);
    }
    @Override
    public Map<RoomTypeEntity, Integer> findByDateAndCapacity(LocalDateTime startDate, LocalDateTime endDate, int capacity){
        List<Long> roomDetailsIdListUsed = roomDetailRepository.findRoomIsUsed(startDate.minusHours(2), endDate.plusHours(2));
        List<RoomDetailEntity> roomDetailListEmpty = roomDetailRepository.findRoomIsEmpty(roomDetailsIdListUsed);
        List<RoomTypeEntity> roomTypeList = roomTypeRepository.findAll();
        Map<RoomTypeEntity, Integer> roomTypeMap = new HashMap<>();
        for(RoomTypeEntity roomTypeEntity : roomTypeList){
            if (roomTypeEntity.getRoomBedType().getCapacity() < capacity){
                continue;
            }
            int quantity = 0;
            for(RoomDetailEntity roomDetailEntity : roomDetailListEmpty){
                if(Objects.equals(roomDetailEntity.getRoomEntity().getRoomTypeEntity().getId(), roomTypeEntity.getId())){
                    quantity++;
                }
            }
            if(quantity > 0){
                roomTypeMap.put(roomTypeEntity, quantity);
            }
        }
        return SortMap.sortRoomTypeMap(roomTypeMap);
//        List<Long> roomTypeIdList = roomRepository.findRoomTypeNotInRoomDetailId(roomDetailsIdListUsed);
//        return roomTypeRepository.findDistinctByIdInAndCapacityGreaterThanEqual(roomTypeIdList, capacity);
    }
    @Override
    public RoomDetailEntity selectRoomForOrder(Long roomTypeId, LocalDateTime startDate, LocalDateTime endDate){
//        List<Long> roomDetailsIdListUsed = roomDetailRepository.findRoomIsUsed(startDate.minusHours(1), endDate.plusHours(1));
//        List<Long> roomDetailIdListEmpty = roomDetailRepository.findRoomIsEmptyForOrder(roomDetailsIdListUsed, roomTypeId);
//        List<Long> roomDetailIdListEmptyDay = roomDetailRepository.findRoomEmptyDayByIdIn(roomDetailIdListEmpty, startDate);
//        if(roomDetailIdListEmpty.size() == roomDetailIdListEmptyDay.size()){
//            Random random = new Random();
//            Long id = roomDetailIdListEmptyDay.get(random.nextInt(roomDetailIdListEmptyDay.size()));
//            return roomDetailRepository.findById(id).orElseThrow();
//        }
//        if(!roomDetailIdListEmptyDay.isEmpty()){
//            roomDetailIdListEmpty = roomDetailIdListEmpty.stream()
//                    .filter(element -> !roomDetailIdListEmptyDay.contains(element))
//                    .toList();
//        }
//        Long roomDetailId = roomDetailRepository.findRoomForOrder(roomDetailIdListEmpty, startDate);
//        if(roomDetailId == null){
//            Random random = new Random();
//            roomDetailId = roomDetailIdListEmpty.get(random.nextInt(roomDetailIdListEmpty.size()));
//        }
//        return roomDetailRepository.findById(roomDetailId).orElseThrow();
        return null;
    }

    @Override
    @Transactional
    public void createRoomType(RoomTypeEntity roomTypeEntity, MultipartFile image, String des) throws IOException {
        RoomTypeEntity roomTypeSaved = roomTypeRepository.save(roomTypeEntity);
        updateRoomTypeImage(roomTypeSaved, image, RoomTypeImageEntity.photoName.bed, des);
    }

    @Override
    @Transactional(rollbackOn = IOException.class)
    public void updateRoomTypeImage(RoomTypeEntity roomTypeSaved, MultipartFile image,
                                    RoomTypeImageEntity.photoName name, String des) throws IOException {
        if((image.getOriginalFilename() == null || image.getOriginalFilename().isEmpty())
            && (des == null || des.isEmpty())){
            return;
        }
        RoomTypeImageEntity roomTypeImageEntity = new RoomTypeImageEntity();
        if(roomTypeSaved.getRoomTypeImageEntities() != null) {
            for(RoomTypeImageEntity ri : roomTypeSaved.getRoomTypeImageEntities()){
                if(ri.getName().equals(name)){
                    roomTypeImageEntity = ri;
                }
            }
        }
        roomTypeImageEntity.setName(name);
        roomTypeImageEntity.setDescription(des);
        roomTypeImageEntity.setRoomTypeEntity(roomTypeSaved);
        if((des != null || !des.isEmpty()) && (image.getOriginalFilename() == null || image.getOriginalFilename().isEmpty())){
            roomTypeImageRepository.save(roomTypeImageEntity);
            return;
        }
        roomTypeImageEntity.setPhotoPath(StringUtils.cleanPath(image.getOriginalFilename()));
        roomTypeImageRepository.save(roomTypeImageEntity);
        String uploadDir = "other-resource/room-type-images/" + roomTypeImageEntity.getName() + "/" +
                roomTypeImageEntity.getRoomTypeEntity().getId();
        FileUploadUtil.saveFile(uploadDir, roomTypeImageEntity.getPhotoPath(), image);
    }

    @Override
    public RoomTypeEntity updateRoomTypeDetail(RoomTypeEntity roomType, RoomTypeDetailEntity roomTypeDetailEntity) {
        roomType.setRoomBedType(roomBedTypeRepository.findByName(roomType.getRoomBedType().getName()));
        RoomTypeEntity roomTypeSaved = roomTypeRepository.save(roomType);
        List<RoomTypeImageEntity> imageEntityList = roomTypeImageRepository.findByRoomTypeId(roomTypeSaved.getId());
        roomTypeSaved.setRoomTypeImageEntities(imageEntityList);
        roomTypeDetailEntity.setRoomType(roomTypeSaved);
        roomTypeDetailRepository.save(roomTypeDetailEntity);
        return roomTypeSaved;
    }
    @Override
    public List<RoomTypeImageEntity> getNewRoomTypeImage(){
        List<RoomTypeImageEntity> imageEntityList = new ArrayList<>();
        for(RoomTypeImageEntity.photoName image : getPhotoName()){
            RoomTypeImageEntity roomTypeImageEntity = new RoomTypeImageEntity();
            roomTypeImageEntity.setName(image);
            imageEntityList.add(roomTypeImageEntity);
        }
        return imageEntityList;
    }

    @Override
    @Transactional
    public void saveRoom(RoomEntity room, RoomDetailEntity roomDetail){
        room.setRoomTypeEntity(findRoomTypeById(room.getRoomTypeEntity().getId()));
        RoomEntity roomEntitySaved = roomRepository.save(room);
        roomDetail.setRoomEntity(roomEntitySaved);
        roomDetailRepository.save(roomDetail);
    }

    @Override
    public List<RoomEntity> findAllRoom(){
        return  roomRepository.findAll();
    }

    @Override
    public void deleteRoomType(Long roomTypeId) {
        roomTypeRepository.deleteById(roomTypeId);
    }

    @Override
    public void disableRoomType(Long roomTypeId) {
        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId).orElseThrow();
        roomType.setActive(false);
        roomTypeRepository.save(roomType);
    }

    @Override
    public void enableRoomType(Long roomTypeId){
        RoomTypeEntity roomType = roomTypeRepository.findById(roomTypeId).orElseThrow();
        roomType.setActive(true);
        roomTypeRepository.save(roomType);
    }

    @Override
    public void enableRoom(Long roomNumber) {
        RoomEntity room = roomRepository.findById(roomNumber).orElseThrow();
        room.setActive(true);
        roomRepository.save(room);
    }

    @Override
    public void disableRoom(Long roomNumber){
        RoomEntity room = roomRepository.findById(roomNumber).orElseThrow();
        room.setActive(false);
        roomRepository.save(room);
    }

    @Override
    public RoomDetailEntity findRoomDetailById(Long roomId) {
        return roomDetailRepository.findById(roomId).orElseThrow();
    }

    @Override
    public List<RoomBedTypeEntity> getAllBedType() {
        return roomBedTypeRepository.findAll();
    }

    @Override
    public void saveBedType(RoomBedTypeEntity roomBedTypeEntity){
        roomBedTypeRepository.save(roomBedTypeEntity);
    }

    @Override
    public void deleteBedType(Long roomBedTypeId) {
        roomBedTypeRepository.deleteById(roomBedTypeId);
    }

}
