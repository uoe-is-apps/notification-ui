/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author hsun1
 */
@Entity
@Table(name = "attempt", schema="Bblearn")
@NamedQueries({
    @NamedQuery(name = "Attempt.findAll", query = "SELECT v FROM Attempt v"),
    @NamedQuery(name = "Attempt.findByGradebookGradePk1", query = "SELECT v FROM Attempt v WHERE v.gradebookGradePk1 = (?1)")
})
public class Attempt {//
    private static final long serialVersionUID = 1L;
    
//    gradebook_grade_pk1     
//    date_modified
    
    @Basic(optional = false)
    @Column(name = "gradebook_grade_pk1")
    @Id
    private Integer gradebookGradePk1;

    @Basic(optional = false)
    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;  

    public Integer getGradebookGradePk1() {
        return gradebookGradePk1;
    }

    public void setGradebookGradePk1(Integer gradebookGradePk1) {
        this.gradebookGradePk1 = gradebookGradePk1;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
 
    
    
}
