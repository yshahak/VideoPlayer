# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\B.E.L\Documents\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepclassmembers enum com.ysapps.videoplayer.** { *; }
# Support v4
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

# Support v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
# Picasso
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
#-dontwarn retrofit2.Platform$Java8

## BEGIN RETROFIT
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
## END RETROFIT


## BEGIN VIMEO-NETWORKING
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
## END VIMEO-NETWORKING


## BEGIN GSON
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.vimeo.networking.** { *; }
-keepclassmembers enum * { *; }

## END GSONr

# Fabric
-keepattributes SourceFile,LineNumberTable,*Annotation*
-dontwarn io.fabric.sdk.android.services.**


-keep class com.crashlytics.android.**

#StartApp
-keep class com.startapp.** {
      *;
}
-dontwarn com.startapp.**

#FLURRY
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Google Play Services library
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

#Solve  can't find referenced method 'float getBatteryLevel(android.content.Context)' in program class io.fabric.sdk.android.services.common.CommonUtils
# http://stackoverflow.com/questions/36425075/error-while-making-signed-apk-after-enabling-the-proguard-in-android-studio
-keep public class android.util.FloatMath

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface

