
<h1 align="center">
  <br>
  <img src="https://i.imgur.com/fLsHNCO.png" alt="GodotAdMob" width=500>
  <br>
  Godot AdMob Android
  <br>
</h1>

<h4 align="center">A Godot's plugin for Android of <a href="https://admob.google.com" target="_blank">AdMob</a>.</h4>

<p align="center">
  <a href="https://github.com/Poing-Studios/godot-admob-android/releases">
    <img src="https://img.shields.io/github/v/tag/Poing-Studios/godot-admob-android?label=Version">
  </a>
  <a href="https://github.com/Poing-Studios/godot-admob-android/actions">
    <img src="https://github.com/Poing-Studios/godot-admob-android/workflows/Build%20Android/badge.svg">
  </a>
  <a href="https://github.com/Poing-Studios/godot-admob-android/releases">
    <img src="https://img.shields.io/github/downloads/Poing-Studios/godot-admob-android/total?style=social">
  </a>
  <img src="https://img.shields.io/github/stars/Poing-Studios/godot-admob-android?style=social">
  <img src="https://img.shields.io/github/license/Poing-Studios/godot-admob-android?style=plastic">
</p>

<p align="center">
  <a href="#about">About</a> •
  <a href="#installation">Installation</a> •
  <a href="#documentation">Docs</a> •
  <a href="https://github.com/Poing-Studios/godot-admob-android/releases">Downloads</a> 
</p>

     
## ❓ About 

<img src="static/admob.webp" align="right"
     alt="Preview" width="auto" height="390">

This repository is for a _Godot Engine Plugin_ that allows showing the ads offered by **AdMob** in an **easy** way, without worrying about the building or version, **just download and use**.

The **purpose** of this plugin is to always keep **up to date with Godot**, supporting **ALMOST ALL** versions from v4.1+, and also make the code **compatible** on **Android and [iOS](https://github.com/Poing-Studios/godot-admob-ios)**, so each advertisement will work **identically on both systems**.

### 🔑 Key features

- It's a wrapper for [Google Mobile Ads SDK](https://developers.google.com/admob/android/sdk). 🎁
- Easy Configuration. 😀
- Supports nearly all Ad Formats: **Banner**, **Interstitial**, **Rewarded**, **Rewarded Interstitial**. 📺
- GDPR Compliance with UMP Support. ✉️
- Targeting Capabilities. 🎯
- Seamless integration with Mediation partners: **AdColony**, **Meta**, **Vungle**. 💰
- CI/CD for streamlined development and deployment. 🔄🚀
- Features a dedicated [Godot Plugin](https://github.com/Poing-Studios/Godot-AdMob-Editor-Plugin), reducing the need for extensive coding. 🔌


## 🙋‍♂️How to use 
- We recommend you to use the [AdMob Plugin](https://github.com/Poing-Studios/godot-admob-plugin), you can download direcly from [Godot Assets](https://godotengine.org/asset-library/asset/933).
- After download, we recommend you to read the [README.md](https://github.com/Poing-Studios/godot-admob-plugin/blob/master/README.md) of the Plugin to know how to use.

## 📥Installing:
- To get started, download the `android-template-v{{ your_godot_version }}.zip` file from the [releases tab](https://github.com/Poing-Studios/godot-admob-android/releases). We recommend checking the [supported Godot version](https://github.com/Poing-Studios/godot-admob-versions/blob/master/versions.json) before proceeding. You can also use the [AdMob Plugin](https://github.com/Poing-Studios/godot-admob-plugin) for this step by navigating to `Tools -> AdMob Download Manager -> Android -> LatestVersion`.
- Enable Android Build Template. [Check the tutorial here](https://docs.godotengine.org/en/stable/tutorials/export/android_custom_build.html).
- Inside `android-?-template-v{{ your_godot_version }}.zip` you downloaded, you will face some folders like `'ads'`, `adcolony`, `meta`, `vungle`. To AdMob works `ads` is required, but if you want [Mediation](https://support.google.com/admob/answer/13420272?hl=en), you need the other folders.
- Move the content inside the folder which you need into ```res://android/plugins``` directory on your Godot project.
- Add your [AdMob App ID](https://support.google.com/admob/answer/7356431) to your app's ```res://android/build/AndroidManifest.xml``` file by adding a ```<meta-data>``` tag with name ```com.google.android.gms.ads.APPLICATION_ID```, as shown below. If you don't do this then you App will crash on start-up.

``` xml
<!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
<meta-data
	android:name="com.google.android.gms.ads.APPLICATION_ID"
	android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
```
- (Optional) If you are using UMP, you can add too the [Delay app measurement](https://developers.google.com/admob/ump/android/quick-start#delay_app_measurement_optional) inside ```AndroidManifest.xml``` 

``` xml
<meta-data
	android:name="com.google.android.gms.ads.DELAY_APP_MEASUREMENT_INIT"
	android:value="true"/>
```

- Export the project enabling the `Use Custom Build` and `AdMob Plugin`:
- ![Export Project](https://i.imgur.com/MPbnmoD.png)

## iOS (v3.3+):
- https://github.com/Poing-Studios/godot-admob-ios

## User Messaging Platform (UMP):
- To use UMP due of EUROPE ePrivacy Directive and the General Data Protection Regulation (GDPR), you first need to do configure your [Funding Choices](https://support.google.com/fundingchoices/answer/9180084).
- If your app is "ForChildDirectedTreatment" then the UMP [won't appear and signals won't work for consent](https://stackoverflow.com/a/63232045), this is normal so don't worry.
- To show personalized or non-personalized ads, then you need to change inside your [AdMob Account](https://apps.admob.com/?utm_source=internal&utm_medium=et&utm_campaign=helpcentrecontextualopt&utm_term=http://goo.gl/6Xkfcf&subid=ww-ww-et-amhelpv4)
![npa-image](https://i.stack.imgur.com/0v1eL.png)

## Documentation
For a complete documentation of this Plugin, [check our wiki](https://github.com/Poing-Studios/Godot-admob-android/wiki).

Alternatively, you can check the docs of AdMob itself of [Android](https://developers.google.com/admob/android/quick-start) and [iOS](https://developers.google.com/admob/ios/quick-start).

## Contribute
We are a dedicated area to how contribute for Android and iOS on our wiki.
- Android: https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/wiki/Android-Plugin#developing
- iOS: https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/wiki/iOS-Plugin#developing

## Getting help
[![DISCUSSIONS](https://img.shields.io/badge/Poing%20AdMob-%F0%9F%86%98%20Discussions%C2%A0%F0%9F%86%98-green?style=for-the-badge)](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/discussions)
