/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author hsun1
 */

@Entity
@Table(name = "cs_room") //, schema="dbo"
@NamedQueries({
    @NamedQuery(name = "Room.findAllRooms", query = "SELECT v FROM Room v "), //WHERE v.availableInd = 'Y' AND v.rowStatus = '0'
    @NamedQuery(name = "Room.findRoomByTimetableid", query = "SELECT v FROM Room v WHERE v.timetableId = (?1)")      
})
public class Room implements Serializable{
     private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "timetableId")
    @Id
    private String timetableId;  
    
    @Basic(optional = false)
    @Column(name = "roomName")    
    private String roomName;
            
    @Basic(optional = false)
    @Column(name = "address")            
    private String address;

    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
            
             
            
}
