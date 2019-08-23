package com.sindra.MapDataBase.DataTypes;

public class SetMembers implements Comparable<SetMembers> {
    private String score;
    private String key;

    public SetMembers(String score, String key) {
        this.score = score;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int compareTo(SetMembers other) {
        if(this.score.equals(other.score))
            return this.key.compareTo(other.key);
        else
            return this.score.compareTo(other.score);
    }
}
