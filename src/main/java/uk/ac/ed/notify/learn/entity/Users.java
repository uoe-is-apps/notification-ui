/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.entity;

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
@Table(name = "users", schema="Bblearn")
@NamedQueries({
    @NamedQuery(name = "Users.findAllActiveUsers", query = "SELECT v FROM Users v "
        + " WHERE v.availableInd = 'Y' AND v.rowStatus = '0' "), //apply active filter   
    @NamedQuery(name = "Users.findByPk1", query = "SELECT v FROM Users v WHERE v.pk1 = (?1)")      
})
public class Users {
    private static final long serialVersionUID = 1L;
//pk1                   not null number(38)     
//user_id               not null nvarchar2(50)  

    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "user_id")
    private String userId;   

    
    @Basic(optional = false)
    @Column(name = "row_status")
    private Integer rowStatus;      

    @Basic(optional = false)
    @Column(name = "available_ind")
    private String availableInd;  
    
    
    
    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Integer getPk1() {
        return pk1;
    }

    public void setPk1(Integer pk1) {
        this.pk1 = pk1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvailableInd() {
        return availableInd;
    }

    public void setAvailableInd(String availableInd) {
        this.availableInd = availableInd;
    }

}
