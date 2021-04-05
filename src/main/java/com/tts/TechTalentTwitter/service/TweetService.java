package com.tts.TechTalentTwitter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.TechTalentTwitter.model.Tag;
import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.repository.TagRepository;
import com.tts.TechTalentTwitter.repository.TweetRepository;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<TweetDisplay> findAll() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
        return formatTweets(tweets);
    }

    public List<TweetDisplay> findAllByUser(User user) {
        List<Tweet> tweets = tweetRepository.findAllByUserOrderByCreatedAtDesc(user);
        return formatTweets(tweets);
    }

    public List<TweetDisplay> findAllByUsers(List<User> users) {
        List<Tweet> tweets = tweetRepository.findAllByUserInOrderByCreatedAtDesc(users);
        return formatTweets(tweets);
    }

    public List<TweetDisplay> findAllWithTag(String tag) {
        List<Tweet> tweets = tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatTweets(tweets);
    }

    public void save(Tweet tweet) {
        handleTags(tweet);
        tweetRepository.save(tweet);
    }

    private void handleTags(Tweet tweet) {
        List<Tag> tags = new ArrayList<Tag>();
    //looking for a # and one or more characters without whitespace
        Pattern pattern = Pattern.compile("#\\w+");
    //
        Matcher matcher = pattern.matcher(tweet.getMessage());
        while (matcher.find()) 
        {
            String phrase = matcher.group().substring(1).toLowerCase();
            // check to see if tag already exists, and if it does this will grab the info
            Tag tag = tagRepository.findByPhrase(phrase);
            //if it doesnt exist, we will create a new one
            if (tag == null) 
            {
            	//construct a new tag
                tag = new Tag();
                //add the phrase to it
                tag.setPhrase(phrase);
                //save it to repository
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        
        tweet.setTags(tags);
    }

    
    private List<TweetDisplay> formatTweets(List<Tweet> tweets) {
        addTagLinks(tweets);
        shortenLinks(tweets);
        List<TweetDisplay> displayTweets = formatTimestamps(tweets);
        return displayTweets;
    }

    private void addTagLinks(List<Tweet> tweets) {
    	//serach for hashtags with no whitespace folowing
        Pattern pattern = Pattern.compile("#\\w+");
        //walks through tweets it is handed
        for (Tweet tweet : tweets) 
        {
        	//get messages from tweets
            String message = tweet.getMessage();
            //search through messages for pattern
            Matcher matcher = pattern.matcher(message);
           //set is same as a list but no order
            Set<String> tags = new HashSet<String>();
          //add all the messages to a set of tags 
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            //walk all the tags we just found
            for (String tag : tags) 
            {
            	//finally, we will replace the tag with a hyperlink so that the tags are linked
            	//the text of the hyperlink will be the tag
            	String replacement = "<a class=\"tag\" href=\"/tweets/";
                replacement += tag.substring(1).toLowerCase();
                replacement += "\">";
            	replacement += tag;
                replacement += "</a>";
            	
            	message = message.replaceAll(tag, replacement);
                        
            }
            tweet.setMessage(message);
        }
        
    }
        
        private void shortenLinks(List<Tweet> tweets) {
            Pattern pattern = Pattern.compile("https?[^ ]+");
            //walk list
            for (Tweet tweet : tweets)
            {
                String message = tweet.getMessage();
                // find all matching links
                Matcher matcher = pattern.matcher(message);
                //walk through all links
                while (matcher.find()) {
                    String link = matcher.group();
                    String shortenedLink = link;
                    //this will shorten if longer than 23 characters
                    if (link.length() > 23) {
                    	//replaces link with shortenedLink
                        shortenedLink = link.substring(0, 20) + "...";
                        String replacement = "<a class=\"tag\" href=\"";
                        replacement += link;
                        replacement += "\" target=\"_blank\">";
                        replacement += shortenedLink;
                        replacement += "</a>";
                        
                        message = message.replace(link, replacement);
                    }
                    tweet.setMessage(message);
                }

            }

    }

        private List<TweetDisplay> formatTimestamps(List<Tweet> tweets) {
            List<TweetDisplay> response = new ArrayList<>();
            PrettyTime prettyTime = new PrettyTime();
            SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
            Date now = new Date();
            for (Tweet tweet : tweets) 
            {
                TweetDisplay tweetDisplay = new TweetDisplay();
                tweetDisplay.setUser(tweet.getUser());
                tweetDisplay.setMessage(tweet.getMessage());
                tweetDisplay.setTags(tweet.getTags());
                
                long diffInMillies = Math.abs(now.getTime() - tweet.getCreatedAt().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if (diff > 3)
                {
                    tweetDisplay.setDate(simpleDate.format(tweet.getCreatedAt()));
                } else 
                {
                    tweetDisplay.setDate(prettyTime.format(tweet.getCreatedAt()));
                }
                response.add(tweetDisplay);
            }
            return response;
        }
        
}
