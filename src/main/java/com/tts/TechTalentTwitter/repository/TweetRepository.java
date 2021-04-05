package com.tts.TechTalentTwitter.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.User;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {
    //finds all orders them by Created at time, descending
	List<Tweet> findAllByOrderByCreatedAtDesc();
	//finds all tweets by users and show by user
    List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);
    //finds all tweets and order them by list??
    List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);
    //creates a query where given a certain tag phrase, it will find 
    //the phrase, order them by time, and output that list
    List<Tweet> findByTags_PhraseOrderByCreatedAtDesc(String phrase);
    
}