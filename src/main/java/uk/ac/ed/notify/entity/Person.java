/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.entity;

import java.util.List;

/**
 * General user class 
 * @author Richard Good
 */
public class Person {
    
    private String cn;
    private String gidNumber;
    private String homeDirectory;
    private String sn;
    private String uid;
    private String uidNumber;
    private String eduniCategory;
    private String eduniCollegeCode;
    private String eduniIdmsID;
    private String eduniIDStatus;
    private String eduniOrganisation;
    private String eduniOrgCode;
    private String eduniPrimaryAffiliationId;
    private String eduniSchoolCode;
    private List<String> eduniServiceCode;
    private String eduniTitle;
    private String eduniType;    
    private List<String> eduPersonAffiliation;
    private List<String> eduPersonEntitlement;
    private String gecos;
    private String givenName;
    private String krbName;
    private String loginShell;
    private String mail;
    private String title;

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
  
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGidNumber() {
        return gidNumber;
    }

    public void setGidNumber(String gidNumber) {
        this.gidNumber = gidNumber;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUidNumber() {
        return uidNumber;
    }

    public void setUidNumber(String uidNumber) {
        this.uidNumber = uidNumber;
    }

    public String getEduniCategory() {
        return eduniCategory;
    }

    public void setEduniCategory(String eduniCategory) {
        this.eduniCategory = eduniCategory;
    }

    public String getEduniCollegeCode() {
        return eduniCollegeCode;
    }

    public void setEduniCollegeCode(String eduniCollegeCode) {
        this.eduniCollegeCode = eduniCollegeCode;
    }

    public String getEduniIdmsID() {
        return eduniIdmsID;
    }

    public void setEduniIdmsID(String eduniIdmsID) {
        this.eduniIdmsID = eduniIdmsID;
    }

    public String getEduniIDStatus() {
        return eduniIDStatus;
    }

    public void setEduniIDStatus(String eduniIDStatus) {
        this.eduniIDStatus = eduniIDStatus;
    }

    public String getEduniOrganisation() {
        return eduniOrganisation;
    }

    public void setEduniOrganisation(String eduniOrganisation) {
        this.eduniOrganisation = eduniOrganisation;
    }

    public String getEduniOrgcode() {
        return eduniOrgCode;
    }

    public void setEduniOrgcode(String eduniOrgcode) {
        this.eduniOrgCode = eduniOrgcode;
    }

    public String getEduniPrimaryAffiliationId() {
        return eduniPrimaryAffiliationId;
    }

    public void setEduniPrimaryAffiliationId(String eduniPrimaryAffiliationId) {
        this.eduniPrimaryAffiliationId = eduniPrimaryAffiliationId;
    }

    public String getEduniSchoolCode() {
        return eduniSchoolCode;
    }

    public void setEduniSchoolCode(String eduniSchoolCode) {
        this.eduniSchoolCode = eduniSchoolCode;
    }

    public String getEduniTitle() {
        return eduniTitle;
    }

    public void setEduniTitle(String eduniTitle) {
        this.eduniTitle = eduniTitle;
    }

    public String getEduniType() {
        return eduniType;
    }

    public void setEduniType(String eduniType) {
        this.eduniType = eduniType;
    }

    public String getGecos() {
        return gecos;
    }

    public void setGecos(String gecos) {
        this.gecos = gecos;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getKrbName() {
        return krbName;
    }

    public void setKrbName(String krbName) {
        this.krbName = krbName;
    }

    public String getLoginShell() {
        return loginShell;
    }

    public void setLoginShell(String loginShell) {
        this.loginShell = loginShell;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEduniOrgCode() {
        return eduniOrgCode;
    }

    public void setEduniOrgCode(String eduniOrgCode) {
        this.eduniOrgCode = eduniOrgCode;
    }

    public List<String> getEduniServiceCode() {
        return eduniServiceCode;
    }

    public void setEduniServiceCode(List<String> eduniServiceCode) {
        this.eduniServiceCode = eduniServiceCode;
    }

    public List<String> getEduPersonAffiliation() {
        return eduPersonAffiliation;
    }

    public void setEduPersonAffiliation(List<String> eduPersonAffiliation) {
        this.eduPersonAffiliation = eduPersonAffiliation;
    }

    public List<String> getEduPersonEntitlement() {
        return eduPersonEntitlement;
    }

    public void setEduPersonEntitlement(List<String> eduPersonEntitlement) {
        this.eduPersonEntitlement = eduPersonEntitlement;
    }
    
    
       
}