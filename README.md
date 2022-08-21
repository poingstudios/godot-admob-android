
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
  <a href="https://github.com/Poing-Studios/godot-admob-android/blob/master/admob/build.gradle#L30">
    <img src="https://img.shields.io/badge/GAD%20SDK%20Android-v21.1.0-informational">
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

## About

<table>
  <tr>
  <td>

  This repository is for a _Godot Engine Plugin_ that allows showing the ads offered by **AdMob** in an **easy** way, without worrying about the building or version, **just download and use**.

  The **purpose** of this plugin is to always keep **up to date with Godot**, supporting **ALMOST ALL** versions from 3.x to 4.x (when it is released), and also make the code **compatible** on **Android and [iOS](https://github.com/Poing-Studios/godot-admob-ios)**, so each advertisement will work **identically on both systems**.

  ![Preview](https://i.imgur.com/u5y2GEx.png)

  <p align="right">
    <sub>(Preview)</sub>
  </p>

  </td>
  </tr>
</table>

## Features
  
|                                       Ad Formats                                        | Available 🤖 |
| :-------------------------------------------------------------------------------------: | :---------: |
|                                         Banner                                          |      ✔️      |
|                                      Interstitial                                       |      ✔️      |
|                                        Rewarded                                         |      ✔️      |
|        [Rewarded Interstitial](https://support.google.com/admob/answer/9884467)         |      ✔️      |
| Native is [REMOVED](https://github.com/Poing-Studios/Godot-AdMob-Android-iOS/issues/75) |      ❗      |

|   Others   | Available 🤖 |
| :--------: | :---------: |
| EU consent |      ✔️      |
| Targeting  |      ✔️      |
| Mediation  |      ❌      |
|   CI/CD    |      ✔️      |


## Installation 
- First of all you need to install the [AdMob Editor Plugin](https://github.com/Poing-Studios/Godot-AdMob-Editor-Plugin), you can download direcly from [Godot Assets](https://godotengine.org/asset-library/asset/933).

## Android (v3.2.2+):
- Tutorial: https://youtu.be/ZnlH3INcAGs
- Download the ```android-?-template-v{{ your_godot_version }}.zip``` in the [releases tab](https://github.com/Poing-Studios/godot-admob-android/releases), we recommend you to use always the latest.
- Enable Android Build Template. [Check the tutorial here](https://docs.godotengine.org/en/stable/getting_started/workflow/export/android_custom_build.html)
- Extract the content in ```android-?-template-v{{ your_godot_version }}.zip``` into ```res://android/plugins``` directory on your Godot project
- Add your [AdMob App ID](https://support.google.com/admob/answer/7356431) to your app's ```res://android/build/AndroidManifest.xml``` file by adding a ```<meta-data>``` tag with name ```com.google.android.gms.ads.APPLICATION_ID```, as shown below.

``` xml
<!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
<meta-data
	tools:replace="android:value"
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
