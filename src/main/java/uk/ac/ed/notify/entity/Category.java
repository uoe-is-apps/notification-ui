package uk.ac.ed.notify.entity;

import java.util.List;

/**
 * Created by rgood on 30/09/2015.
 */
public class Category {

    private List<String> errors;

    private String title;

    private List<Notification> entries;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Notification> getEntries() {
        return entries;
    }

    public void setEntries(List<Notification> entries) {
        this.entries = entries;
    }
}
