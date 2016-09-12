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
-dontwarn retrofit2.Platform$Java8

# keep GsonSerializable interface, it would be thrown away by proguard since it is empty
-keep class com.ysapps.videoplayer.AndroidGsonDeserializer

# member fields of serialized classes, including enums that implement this interface
-keepclassmembers class * implements com.vimeo.networking.GsonDeserializer {
    <fields>;
}

# also keep names of these classes. not required, but just in case.
-keepnames class * implements com.vimeo.networking.GsonDeserializer

# Fabric
-keepattributes SourceFile,LineNumberTable,*Annotation*
-keep class com.crashlytics.android.**

-keepattributes *Annotation*
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface