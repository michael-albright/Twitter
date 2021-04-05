package com.tts.TechTalentTwitter.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//NO ENTITY TAG!!!-- we are not storing into the database!
public class TweetDisplay {
    private User user;
    private String message;
    private String date; //This will be a String date that we will format
    private List<Tag> tags;

}