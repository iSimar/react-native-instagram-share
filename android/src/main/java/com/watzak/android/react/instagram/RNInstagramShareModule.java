package com.watzak.android.react.instagram;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.io.File;
import java.util.List;

public class RNInstagramShareModule extends ReactContextBaseJavaModule {
  ReactApplicationContext reactContext;

  // Constants
  private static final String NAME = "RNInstagramShare";
  private static final String INSTAGRAM_PACKAGE = "com.instagram.android";
  private static final int INSTAGRAM_REQUEST_CODE = 678917;
  private static final String E_NO_INSTAGRAM = "Instagram not installed.";
  private static final String E_GENERIC_ERROR = "Error while sharing to Instagram.";

  public RNInstagramShareModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return NAME;
  }

  private boolean doesPackageExist(String targetPackage) {
    PackageManager pm = this.reactContext.getPackageManager();
    try {
      PackageInfo info = pm.getPackageInfo(
        targetPackage,
        PackageManager.GET_META_DATA
      );
     } catch (PackageManager.NameNotFoundException e) {
       return false;
     }
     return true;
  }

  @ReactMethod
  public void share(String localFilePath, String type, Promise promise) {
    try {
      if (doesPackageExist(INSTAGRAM_PACKAGE)) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.parse(localFilePath);

        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setPackage(INSTAGRAM_PACKAGE);
        shareIntent.setType(type);
        reactContext.startActivity(shareIntent);
        promise.resolve(true);
      } else {
        promise.reject(E_NO_INSTAGRAM);
      }
    } catch (Exception ex) {
      promise.reject(E_GENERIC_ERROR, ex);
    }
  }
}
