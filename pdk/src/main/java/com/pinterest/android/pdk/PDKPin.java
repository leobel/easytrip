package com.pinterest.android.pdk;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

public class PDKPin extends PDKModel {

    private static ObjectMapper objectMapper;
    private String uid;
    private PDKBoard board;
    private PDKUser user;
    private String url;
    private String link;
    private String note;
    private String color;
    private PDKMetadata metadata;
    private Date createdAt;
    private Integer likeCount;
    private Integer commentCount;
    private Integer repinCount;
    private PDKImage image;
    private String originalLink;

    public static ObjectMapper getObjectMapper(){
        if(objectMapper == null){
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JodaModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        return objectMapper;
    };


    public static PDKPin makePin(Object obj) {
        ObjectMapper objectMapper = getObjectMapper();
        PDKPin pin = new PDKPin();
        try {
            if (obj instanceof JSONObject) {
                JSONObject dataObj = (JSONObject)obj;
                if (dataObj.has("creator")) {
                    pin.setUser(PDKUser.makeUser(dataObj.getJSONObject("creator")));
                }
                if(dataObj.has("url")){
                    pin.setUrl(dataObj.getString("url"));
                }
                if (dataObj.has("created_at")) {
                    pin.setCreatedAt(
                            Utils.getDateFormatter().parse(dataObj.getString("created_at")));
                }
                if (dataObj.has("id")) {
                    pin.setUid(dataObj.getString("id"));
                }
                if (dataObj.has("note")) {
                    pin.setNote(dataObj.getString("note"));
                }
                if(dataObj.has("color")){
                    pin.setColor(dataObj.getString("color"));
                }
                if (dataObj.has("link")) {
                    pin.setLink(dataObj.getString("link"));
                }
                if (dataObj.has("board")) {
                    pin.setBoard(PDKBoard.makeBoard(dataObj.getJSONObject("board")));
                }
                if (dataObj.has("image")) {
                    pin.setImage(objectMapper.readValue(dataObj.getJSONObject("image").toString(), PDKImage.class));
                }
                if (dataObj.has("counts")) {
                    JSONObject countsObj = dataObj.getJSONObject("counts");
                    if (countsObj.has("likes")) {
                        pin.setLikeCount(countsObj.getInt("likes"));
                    }
                    if (countsObj.has("comments")) {
                        pin.setCommentCount(countsObj.getInt("comments"));
                    }
                    if (countsObj.has("repins")) {
                        pin.setRepinCount(countsObj.getInt("repins"));
                    }
                }
                if(dataObj.has("original_link")){
                    pin.setOriginalLink(dataObj.getString("original_link"));
                }
                if (dataObj.has("metadata")) {
                   pin.setMetadata(objectMapper.readValue(dataObj.get("metadata").toString(), PDKMetadata.class));
                }

            }
        } catch (JSONException e) {
            Utils.loge("PDK: PDKPin JSON parse error %s", e.getLocalizedMessage());
        } catch (ParseException e) {
            Utils.loge("PDK: PDKPin Text parse error %s", e.getLocalizedMessage());
        } catch (JsonParseException e) {
            Utils.loge("PDK: PDKPin JSON parse error %s", e.getLocalizedMessage());
        } catch (JsonMappingException e) {
            Utils.loge("PDK: PDKPin JSON mapping error %s", e.getLocalizedMessage());
        } catch (IOException e) {
            Utils.loge("PDK: IO error %s", e.getLocalizedMessage());
        }
        return pin;
    }

    public static List<PDKPin> makePinList(Object obj) {
        List<PDKPin> pinList = new ArrayList<PDKPin>();
        try {
            if (obj instanceof JSONArray) {

                JSONArray jAarray = (JSONArray)obj;
                int size = jAarray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject dataObj = jAarray.getJSONObject(i);
                    pinList.add(makePin(dataObj));
                }
            }
        } catch (JSONException e) {
            Utils.loge("PDK: PDKPinList parse JSON error %s", e.getLocalizedMessage());
        }
        return pinList;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public PDKBoard getBoard() {
        return board;
    }

    public PDKUser getUser() {
        return user;
    }

    public String getLink() {
        return link;
    }

    public String getNote() {
        return note;
    }

    public PDKMetadata getMetadata() {
        return metadata;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Integer getRepinCount() {
        return repinCount;
    }

    public PDKImage getImage() {
        return image;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public void setBoard(PDKBoard board) {
        this.board = board;
    }

    public void setUser(PDKUser user) {
        this.user = user;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMetadata(PDKMetadata metadata) {
        this.metadata = metadata;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setRepinCount(Integer repinCount) {
        this.repinCount = repinCount;
    }

    public void setImage(PDKImage image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PDKPin){
            return ((PDKPin)o).getUid().equals(getUid());
        }
        else{
            return false;
        }
    }
}
