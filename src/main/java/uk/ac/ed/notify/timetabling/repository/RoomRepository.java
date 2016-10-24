/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.timetabling.entity.Room;

/**
 *
 * @author hsun1
 */
public interface RoomRepository extends JpaRepository<Room, Long>{
    List<Room> findAllRooms();
    
    List<Room> findRoomByTimetableid(String timetableId);
}