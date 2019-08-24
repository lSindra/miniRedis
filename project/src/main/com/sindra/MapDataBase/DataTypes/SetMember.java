package com.sindra.MapDataBase.DataTypes;

public class SetMember implements Comparable<SetMember> {
    private String score;
    private String key;

    public SetMember(String score, String key) {
        this.score = score;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isValid() {
        try {
            Double.parseDouble(this.score);
            return !this.score.isEmpty() && !this.key.isEmpty();
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
    }

    @Override
    public int compareTo(SetMember other) {
        if(this.score.equals(other.score))
            return this.key.compareTo(other.key);
        else
            return this.score.compareTo(other.score);
    }
}
