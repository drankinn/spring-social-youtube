/**
 * User: lance
 * Date: 3/1/12
 * Time: 5:31 PM
 */
package org.springframework.social.youtube.api.impl;

import org.springframework.social.youtube.api.YoutubeDataEntry;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *   Youtube User Profile
 * </p>
 * @author Lance Andersen
 */
@XmlRootElement
public class YoutubeProfile implements YoutubeDataEntry, Serializable{

    public YoutubeProfile(){

    }


    public YoutubeProfile(String id, String firstname, String lastname, String username, Integer age, String email, String thumbnail){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.thumbnail = thumbnail;
        this.published = new Date();
        this.updated = new Date();
    }


    private String id;

    private Date updated;

    private Date published;

    private String firstname;

    private String lastname;

    private String username;

    private Integer age;

    private String email;

    private String thumbnail;


    public String getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
}
