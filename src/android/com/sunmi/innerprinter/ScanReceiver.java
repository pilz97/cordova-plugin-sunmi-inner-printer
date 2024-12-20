package com.sunmi.innerprinter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import android.util.Log;

import org.json.JSONObject;

public class ScanReceiver extends BroadcastReceiver {
  private static final String TAG = "SunmiInnerScanReceiver";

  private CallbackContext callbackReceive;
  private boolean isReceiving = true;
  private CordovaInterface cordova;
  private CordovaWebView webView;

  public ScanReceiver() {

  }

  public void setCordova(CordovaInterface cordova, CordovaWebView webView) {
    this.cordova = cordova;
    this.webView = webView;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      String message = "";
      final String scannerType = "Sunmi";
      String code = intent.getStringExtra("data");
      Log.i(TAG, "Bind print service result: " + code);
      //String arr = intent.getByteArrayExtra("source_byte");
      if (code != null && !code.isEmpty()) {
        loadUrl("cordova.fireDocumentEvent('onScannedValue', {'value':\""+code+"\",'message':\""+message+"\",'scannerType':\""+scannerType+"\"});");
      }
    } catch (Exception e) {
      Log.i(TAG, "ERROR ScanReceiver onReceive: " + e.getMessage());
    }
  }

  public void startReceiving(CallbackContext ctx) {
    this.callbackReceive = ctx;
    this.isReceiving = true;

    Log.i(TAG, "Start receiving status");
  }

  public void stopReceiving() {
    this.callbackReceive = null;
    this.isReceiving = false;

    Log.i(TAG, "Stop receiving status");
  }

  private void loadUrl(String url) {
    Log.d(TAG, ">>> loadUrl(): " + url);
    if (cordova == null) {
      Log.i(TAG, "cordova is not initialized unable to send url: " + url);
      return;
    }

    try {
      cordova.getActivity().runOnUiThread(new Runnable() {
          public void run() {
              webView.loadUrl("javascript:try{" + url + "}catch(e){console.log('exception firing pause event from native');};");
          }
      });
    } catch(Exception ex) {
      Log.e(TAG, "failed to send url", ex);
    }
  }
}
