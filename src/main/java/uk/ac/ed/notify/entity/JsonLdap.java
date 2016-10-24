/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.entity;

/**
 *
 * @author hsun1
 */
public class JsonLdap {
    // {"id":"ou=CSG3,ou=CSG,ou=UOE,ou=org,ou=grouper2,dc=authorise,dc=ed,dc=ac,dc=uk", "text":"Corporate Services",  "children":true}
    
    private String id;
    private String text;
    private Boolean children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }
    
    
    
}
