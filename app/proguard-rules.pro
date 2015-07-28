# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/akwasiowusu/Documents/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

-keepclassmembers class * {
    public <init>(android.content.Context);
 }

-dontwarn weborb.**
-dontwarn com.backendless.**
-dontwarn org.apache.**
-dontwarn com.soundcloud.**
-dontwarn android.test.**

-keep class backendless.** { *; }
-keep class com.backendless.** { *; }
-keep class weborb.** { *; }
-keep class de.voidplus.** { *; }
-keep class org.apache.** { *; }