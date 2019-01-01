# react-native-add-shortcut

## Introduction
Library for creating Android [App Shortcuts](https://developer.android.com/guide/topics/ui/shortcuts.html) in [React Native](http://facebook.github.io/react-native).

**Google Official intro for App Shortcuts**

If your app targets Android 7.1 (API level 25) or higher, you can define shortcuts to specific actions in your app. These shortcuts can be displayed in a supported launcher. Shortcuts let your users quickly start common or recommended tasks within your app.

<img src="https://developer.android.com/images/guide/topics/ui/shortcuts.png" height="400" />

## Requirements
- Android SDK version >= `25`
- React Native >= `0.40`

## Getting started

`$ npm install react-native-add-shortcut --save`

### Mostly automatic installation

`$ react-native link react-native-add-shortcut`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `new AddShortcutPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-add-shortcut'
  	project(':react-native-add-shortcut').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-add-shortcut/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-add-shortcut')
  	```
4. Make sure the android sdk version specify to >= `25` in `android/app/build.gradle`:
  ```
    android {
      compileSdkVersion 25
      buildToolsVersion "25.0.2"

      defaultConfig {
          applicationId "com.test"
          minSdkVersion 16
          targetSdkVersion 25
      }
  ```

## Usage
Import the package
```js
import AddShortcut from 'react-native-add-shortcut';
```

### Add and update App Shortcut

**Add Dynamic App Shortcut**
```js
AddShortcut.setDynamicShortcuts([
  {
    name: 'Android',
    imageUrl: 'https://sdtimes.com/wp-content/uploads/2017/03/android-1904852_1920.jpg',
    deepLink: 'fb://profile/423556347'
  },
  {
    name: 'Android Studio',
    imageUrl: 'https://sdtimes.com/wp-content/uploads/2018/09/image1a.png',
    deepLink: 'fb://page/346556456'
  },
  {
    name: 'Apple',
    imageUrl: 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/Apple-Apple.svg/1000px-Apple-Apple.svg.png',
    deepLink: 'fb://group/234536456'
  }
])
```

**Add Pinned App Shortcut**
```js
AddShortcut.setPinnedShortcuts({
    name: 'Apple',
    imageUrl: 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/df/Apple-Apple.svg/1000px-Apple-Apple.svg.png',
    deepLink: 'fb://group/234536456'
})
```

**Options Meaning**

| Name | Type | Description |
| --- | ---  | --- |
| name | String | Short label, Long label and ID. |
| imageUrl | String | Image url to fetch for the icons. |
| deepLink | String | Deep link to open when tapping the shortcut. |


**Remove All Dynamic Shortcuts**
```js
AddShortcut.removeAllDynamicShortcuts()
```

**Get Dynamic Shortcuts** - return `List<ShortcutInfo>`
```js
AddShortcut.getDynamicShortcuts()
```
