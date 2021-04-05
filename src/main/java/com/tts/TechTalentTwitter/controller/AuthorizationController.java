package com.tts.TechTalentTwitter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.TechTalentTwitter.model.User;
import com.tts.TechTalentTwitter.service.UserService;


@Controller
public class AuthorizationController 
{
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/login")
	public String login()
	{
		return "login";
	}
	
	@GetMapping(value="/signup")
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping(value = "/signup")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model)
    {	
    	// check first if a user exists, and if they do return an error
        User userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {//field of where error was: type of error? : error message
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveNewUser(user);
            				
            model.addAttribute("success", "Sign up successful!");
            model.addAttribute("user", new User()); 
        }
        return "registration"; //uses registration.html template
    }

}

