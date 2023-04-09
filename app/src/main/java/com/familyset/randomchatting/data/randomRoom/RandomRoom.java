package com.familyset.randomchatting.data.randomRoom;

import com.google.firebase.Timestamp;

import java.util.Map;

public class RandomRoom {
    private String id;
    private Map<String, Integer> users;
    private int isMatched;
    private Timestamp createdTimestamp;
    // TODO 성별?

    public RandomRoom() {}

    public RandomRoom(String id, int isMatched, Timestamp createdTimestamp) {
        this.id = id;
        this.isMatched = isMatched;
        this.createdTimestamp = createdTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Integer> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Integer> users) {
        this.users = users;
    }

    public int getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(int isMatched) {
        this.isMatched = isMatched;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
