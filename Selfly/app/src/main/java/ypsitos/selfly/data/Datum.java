
package ypsitos.selfly.data;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Datum {

    @SerializedName("attribution")
    @Expose
    private Object attribution;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("comments")
    @Expose
    private Comments comments;
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("likes")
    @Expose
    private Likes likes;
    @SerializedName("images")
    @Expose
    private Images images;
    @SerializedName("users_in_photo")
    @Expose
    private List<Object> usersInPhoto = new ArrayList<Object>();
    @SerializedName("caption")
    @Expose
    private Caption caption;
    @SerializedName("user_has_liked")
    @Expose
    private boolean userHasLiked;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * 
     * @return
     *     The attribution
     */
    public Object getAttribution() {
        return attribution;
    }

    /**
     * 
     * @param attribution
     *     The attribution
     */
    public void setAttribution(Object attribution) {
        this.attribution = attribution;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The location
     */
    public Object getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    public void setLocation(Object location) {
        this.location = location;
    }

    /**
     * 
     * @return
     *     The comments
     */
    public Comments getComments() {
        return comments;
    }

    /**
     * 
     * @param comments
     *     The comments
     */
    public void setComments(Comments comments) {
        this.comments = comments;
    }

    /**
     * 
     * @return
     *     The filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * 
     * @param filter
     *     The filter
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * 
     * @return
     *     The createdTime
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * 
     * @param createdTime
     *     The created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 
     * @return
     *     The link
     */
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The likes
     */
    public Likes getLikes() {
        return likes;
    }

    /**
     * 
     * @param likes
     *     The likes
     */
    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    /**
     * 
     * @return
     *     The images
     */
    public Images getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(Images images) {
        this.images = images;
    }

    /**
     * 
     * @return
     *     The usersInPhoto
     */
    public List<Object> getUsersInPhoto() {
        return usersInPhoto;
    }

    /**
     * 
     * @param usersInPhoto
     *     The users_in_photo
     */
    public void setUsersInPhoto(List<Object> usersInPhoto) {
        this.usersInPhoto = usersInPhoto;
    }

    /**
     * 
     * @return
     *     The caption
     */
    public Caption getCaption() {
        return caption;
    }

    /**
     * 
     * @param caption
     *     The caption
     */
    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    /**
     * 
     * @return
     *     The userHasLiked
     */
    public boolean isUserHasLiked() {
        return userHasLiked;
    }

    /**
     * 
     * @param userHasLiked
     *     The user_has_liked
     */
    public void setUserHasLiked(boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

}
