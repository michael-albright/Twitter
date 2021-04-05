package com.tts.TechTalentTwitter.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.TechTalentTwitter.model.Tweet;
import com.tts.TechTalentTwitter.model.TweetDisplay;
import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.TweetService;
import com.tts.TechTalentTwitter.service.UserService;

@Controller
public class TweetController
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private TweetService tweetService;
    
    @GetMapping(value = {"/tweets", "/"})
    public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model)
    {       
        User loggedInUser = userService.getLoggedInUser();
        List<TweetDisplay> tweets = new ArrayList<>();
        
        if (filter == null)
        {
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following"))
        {
            List<User> following = loggedInUser.getFollowing();
            tweets = tweetService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        }
        else
        {
            tweets = tweetService.findAll();
            model.addAttribute("filter", "all");
        }        
        model.addAttribute("tweetList", tweets);
        return "feed";
    }

    @GetMapping(value = "/tweets/new")
    public String getTweetForm(Model model)
    {
        model.addAttribute("tweet", new Tweet());
        return "newTweet";
    }
    
    @PostMapping(value = "/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model)
    {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors())
        {
            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet successfully created!");
            model.addAttribute("tweet", new Tweet());
        }
        return "newTweet";        
    }  
    
    @GetMapping(value = "/tweets/{tag}")
    public String getTweetsByTag(@PathVariable(value="tag") String tag, Model model)
    {
        List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("tag", tag);
        return "taggedTweets"; //return that we are going to use the template taggedTweets.html
                               // to respond to this request.
    }
    
}
















//package com.tts.TechTalentTwitter.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.tts.TechTalentTwitter.model.Tweet;
//import com.tts.TechTalentTwitter.model.TweetDisplay;
//import com.tts.TechTalentTwitter.model.User;
//import com.tts.TechTalentTwitter.service.TweetService;
//import com.tts.TechTalentTwitter.service.UserService;
//
//@Controller // need access to users and tweets service
//public class TweetController {
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private TweetService tweetService;
//
//	// main access to all tweets, passes all twets to the model
//	@GetMapping(value = { "/tweets", "/" })
//	public String getFeed(Model model) {
//		List<TweetDisplay> tweets = tweetService.findAll();
//		model.addAttribute("tweetList", tweets);
//		return "feed";
//	}
//
//	// fill out a form to create a new tweet
//	@GetMapping(value = "/tweets/new")
//	public String getTweetForm(Model model) {
//		model.addAttribute("tweet", new Tweet());
//		return "newTweet";
//	}
//
//	// HOMEWORK
//	// once posted above, the new tweet will go to "/tweets", and it will be
//	// validated with binding result, then it is attributed to the user who made
//	// the tweet
//	// First, we need to add the query parameter to our getFeed method in
//	// TweetController.
//
//	@GetMapping(value = { "/tweets", "/" })
//	public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
//		User loggedInUser = userService.getLoggedInUser();
//		List<TweetDisplay> tweets = new ArrayList<>();
//		if (filter == null) {
//			filter = "all";
//		}
//		if (filter.equalsIgnoreCase("following")) {
//			List<User> following = loggedInUser.getFollowing();
//			tweets = tweetService.findAllByUsers(following);
//			model.addAttribute("filter", "following");
//		} else {
//			tweets = tweetService.findAll();
//			model.addAttribute("filter", "all");
//		}
//		model.addAttribute("tweetList", tweets);
//		return "feed";
//	}
//
//	// ANYTHING YOU STORE IN A MODEL CAN BE USED IN THYMELEAF
//
//	// new input to output our tag feed
//	// {tag} is a wildcard that will change depending on the name of the tag, but
//	// that will
//	// also be the page it is stored in
//	@GetMapping(value = "/tweets/{tag}")
//
//	// value defines what the end of the
//	public String getTweetsByTag(@PathVariable(value = "tag") String tag, Model model) {
//		List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
//		model.addAttribute("tweetList", tweets);
//		// tweetList will become a variable we can use in thymeleaf
//		model.addAttribute("tag", tag);
//		return "taggedTweets";
//		// return that we are going to use the template taggedTweets.html
//		// to respond to this request
//	}
//
//}
//
////
////if (!bindingResult.hasErrors()) {
////	//chooses user to attribute tweet to
////    tweet.setUser(user);
////    //saves tweet
////    tweetService.save(tweet);
////    //output letting you know your tweet worked
////    model.addAttribute("successMessage", "Tweet successfully created!");