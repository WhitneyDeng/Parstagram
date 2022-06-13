package com.example.parstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/* representation of an entry in Post database class
 * gets and sets from and to the database */
@ParseClassName("Post") // for Parse
public class Post extends ParseObject
{
    //qq: so does Parse automatically get these fields?
    // keys match column names in Post database class
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    //==getters & setters for each attribute of a Post database entry==//
    public String getDescription()
    {
        return getString(KEY_DESCRIPTION); // getString() inherited from ParseObject
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage()
    {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile)
    {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser)
    {
        put(KEY_USER, parseUser);
    }
}
