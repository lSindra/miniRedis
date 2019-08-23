package com.sindra;

import java.util.concurrent.atomic.AtomicReference;

public class Data {
    private long expiration;
    private boolean expires = false;
    private AtomicReference reference;

    public Data(Object initialValue) {
        reference = new AtomicReference<>(initialValue);
    }

    public Data(Object initialValue, int life) {
        reference = new AtomicReference<>(initialValue);
        this.expiration = getExpirationFromLife(life);
        this.expires = true;
    }

    private long getExpirationFromLife(int life) {
        return life * 1000 + System.currentTimeMillis();
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(int expirationTime) {
        this.expiration = getExpirationFromLife(expirationTime);
    }

    public boolean expires() {
        return expires;
    }

    public void setExpires(boolean expires) {
        this.expires = expires;
    }

    public AtomicReference getReference() {
        return reference;
    }
}
