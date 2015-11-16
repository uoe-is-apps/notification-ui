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
@Table(name = "course_users", schema="Bblearn")
@NamedQueries({
    @NamedQuery(name = "CourseUsers.findAll", query = "SELECT v FROM CourseUsers v"),
    @NamedQuery(name = "CourseUsers.findByCrsmainPk1", query = "SELECT v FROM CourseUsers v WHERE v.crsmainPk1 = (?1)")  
})
public class CourseUsers {
    private static final long serialVersionUID = 1L;
    
//pk1                    not null number(38)     
//crsmain_pk1            not null number(38)     
//users_pk1              not null number(38)     

    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "crsmain_pk1")
    private Integer crsmainPk1;    
    
    @Basic(optional = false)
    @Column(name = "users_pk1")
    private Integer usersPk1;  

    @Basic(optional = false)
    @Column(name = "row_status")
    private Integer rowStatus;      

    
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

    public Integer getCrsmainPk1() {
        return crsmainPk1;
    }

    public void setCrsmainPk1(Integer crsmainPk1) {
        this.crsmainPk1 = crsmainPk1;
    }

    public Integer getUsersPk1() {
        return usersPk1;
    }

    public void setUsersPk1(Integer usersPk1) {
        this.usersPk1 = usersPk1;
    }
    
    
    
}
