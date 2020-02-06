# PFLockScreen-Android

[![](https://jitpack.io/v/thealeksandr/PFLockScreen-Android.svg)](https://jitpack.io/#thealeksandr/PFLockScreen-Android)

**Feel free to ask questions add issues.** If I don't respond to an issue or PR, feel free to ping me or send me **DIRECT Message** on twitter @thealeksandr. If you create issue it would be nice if you **SUBSCRIBE FOR UPDATES**. So we can discuss it.

**Please support with a Star :D**

## 
7 Update - Bug fixing (November 3, 2019)
* Added code validation for CREATE mode.

## Min SDK Version - 15

PFLockScreen - Lock Screen Library for Android Application. Library support **pin code** and **fingerprint** authorization for API level 23+.

<p align="center">
<img src="https://user-images.githubusercontent.com/1378730/37100456-9225c0c6-2255-11e8-972c-e365ef2659fa.png" alt="alt text" width="200" hspace="40"><img src="https://user-images.githubusercontent.com/1378730/36675641-2c16130a-1b3c-11e8-88ac-32038e5b3540.png" alt="alt text" width="200" hspace="40"><img src="https://user-images.githubusercontent.com/1378730/36675645-2c63823e-1b3c-11e8-8936-6db8c84333f1.png" alt="alt text" width="200" hspace="40">
</p>



## Add library to your project
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```gradle
dependencies {
	compile 'com.github.thealeksandr:PFLockScreen-Android:1.0.0-beta7'
}
```

## Create pin code

Creating lock screen fragment in **create mode**.

```java
PFLockScreenFragment fragment = new PFLockScreenFragment();
PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
				.setMode(PFFLockScreenConfiguration.MODE_CREATE);
fragment.setConfiguration(builder.build());
fragment.setCodeCreateListener(new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
	@Override
  public void onCodeCreated(String encodedCode) {
		//TODO: save somewhere;
	}
});
//TODO: show fragment;
```


After user created pin code. The library will encode it using Android Key Store and return an encoded string in **PFLockScreenFragment.OnPFLockScreenCodeCreateListener**. All you have to do is save encoded string somewhere - database, SharedPreferences, Android Account etc.


## Show authorization screen

Creating lock screen fragment in *authorization mode* is same as in *creation mode*, but instead of MODE_CREATE use MODE_AUTH.

```java
PFFLockScreenConfiguration.Builder(this).setMode(PFFLockScreenConfiguration.MODE_AUTH);
```


## Configure screen

```java
PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
            .setTitle("Unlock")
            .setUseFingerprint(true).
            .setMode(PFFLockScreenConfiguration.MODE_AUTH)
            .setCodeLength(6)
            .setLeftButton("Can't remeber",
			            new View.OnClickListener() {
		                @Override
                    public void onClick(View v) {

                    }
                });

```


*setTitle(String)* - set custom string on the top of the screen.
*setUseFingerprint(boolean)* - by default fingerprint button will be shown for all device 23+ with a fingerprint sensor. If you don't want use fingerprint at all set *false*.
*setMode(PFLockScreenMode)* - MODE_CREATE or MODE_AUTH. See details above.
*setCodeLength(int)* - set the length of the pin code. By default, length is 4. Minimum length is 4.
*setLeftButton(String, View.OnClickListener)* - set string for the left button and ClickListener.



## Check if pin code encryption key exist

```java
boolean isExist = PFSecurityManager.getInstance().getPinCodeHelper().isPinCodeEncryptionKeyExist();
```

An encryption key is needed to encode/decode pin code and stored in Android KeyStore.



## Delete pin code encryption key.
You need to delete encryption key if you delete/reset pin code.

```java
PFSecurityManager.getInstance().getPinCodeHelper().delete();
```

**Don't use PFFingerprintPinCodeHelper.getInstance().isPinCodeExist() directly.**

## PFPinCodeViewModel

Also you can use PFPinCodeViewModel() for the same methods. ViewModel wrapper around PinCodeHelper that returns LiveData objects.

## Custom encryption.  **NEW!** **NEW!** **NEW!**
Now you can create your custom encryption if for some reasons the one I have doesn't meet your app requarements: 

You need to override ```java IPFPinCodeHelper``` interface. That has four methods: 
```java
void encodePin(Context context, String pin, PFPinCodeHelperCallback<String> callBack);     
void checkPin(Context context, String encodedPin, String pin, PFPinCodeHelperCallback<Boolean> callback);        
void delete(PFPinCodeHelperCallback<Boolean> callback);      
void isPinCodeEncryptionKeyExist(PFPinCodeHelperCallback<Boolean> callback);         
```
*encodePin* - method where you encode pin.   
*checkPin* - to check if pin is valid.        
*delete* - deletePinCode and all related stuff.    
*isPinCodeEncryptionKeyExist* - if you have any encryprion keys or something else you're using to encrpt your key
here you check if all keys you need are exists. Haven't beed deleted or anyting. This method is only for your own logic.
Basically to check if you're code can be decrypted.

All methods take callback as a parameter in case if you want implement something async like server side or whatever.


## UI Customization

You can customize buttons, backgrounds, etc. To do that, use attributes in your activity theme:

*pf_key_button* - style object for key buttons (0-9)
*pf_lock_screen* - style object for the background. Use it to set custom background.
*pf_fingerprint_button* - style object for fingerprint button. You can set custom drawable, paddings, etc.
*pf_delete_button* - style object for delete/backspace button. You can set custom drawable, paddings, etc.
*pf_code_view* - style object to customize code view. (The view from the top of the screen). The view itself is set of check boxes. To customize it use a custom selector with checked states.       
*pf_title* (**NEW**) - style object for title.       
*pf_next* (**NEW**) - style object for next button.         
*pf_hint* (**NEW**) - style object for hint button (Can't remember).          


Examples:
```xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <!-- Some of your styles. -->
    <item name="pf_key_button">@style/MyLockScreenButtonStyle</item>
    <item name="pf_fingerprint_button">@style/MyLockScreenFingerprintButtonStyle</item>
    <item name="pf_delete_button">@style/MyLockScreenDeleteButtonStyle</item>
    <item name="pf_lock_screen">@style/MyLockScreenStyle</item>
    <item name="pf_code_view">@style/MyLockScreenCodeStyle</item>
</style>


 <style name="MyLockScreenStyle">
    <item name="android:background">@drawable/screen_background</item>
</style>

<style name="MyLockScreenButtonStyle">
    <item name="android:textColor">@android:color/white</item>
    <item name="android:textSize">18dp</item>
    <item name="android:foreground">@drawable/key_foreground</item>
</style>

<style name="MyLockScreenFingerprintButtonStyle">
    <item name="android:src">@drawable/fingerprint_icon</item>
</style>

<style name="MyLockScreenDeleteButtonStyle">
    <item name="android:src">@drawable/delete_lockscreen</item>
</style>

<style name="MyLockScreenCodeStyle">
    <item name="android:button">@drawable/code_selector</item>
</style>

<style name="MyLockNextButtonStyle">
    <item name="android:textColor">#9FFF</item>
    <item name="android:textSize">18dp</item>
    <item name="android:background">@null</item>
    <item name="android:layout_margin">20dp</item>
</style>

<style name="MyLockTitleStyle">
    <item name="android:textColor">#FFF</item>
</style>

<style name="MyLockHintStyle">
    <item name="android:textColor">#FFF</item>
</style>
```

**If you want just a bit correct an existing styles you can override:**
```xml
PFLockScreenStyle
PFLockScreenButtonStyle
PFLockScreenFingerPrintButtonStyle
PFLockScreenDeleteButtonStyle
PFCheckBox
PFLockScreenCodeStyle
PFLockScreenNextTextStyle
PFLockScreenHintTextStyle
PFLockScreenTitleTextStyle
```

The only important thing you can't change right now is the size of keys. But it's coming.

**Feel free to ask questions add issues.** If I don't respond to an issue or PR, feel free to ping me or send me DM on twitter @thealeksandr. If you create issue it would be nice if you **subscribe** for updates. So we can discuss it.
