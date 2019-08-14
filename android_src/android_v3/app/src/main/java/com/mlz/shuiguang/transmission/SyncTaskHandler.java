package com.mlz.shuiguang.transmission;

/**
 * Created by dehualai on 1/13/17.
 */

public interface SyncTaskHandler {
    public void onSuccess(Object object);
    public void onFailure(String message, int errorCode);
}
