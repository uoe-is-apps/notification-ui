/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.ed.notify.entity.TopicDetails;

/**
 * Created by rgood on 20/10/2015.
 */
public interface TopicDetailsRepository extends CrudRepository<TopicDetails,String> {
    List<TopicDetails> findByTopicId(String topicId);
    List<TopicDetails> findAll();
}
