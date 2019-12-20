# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#---------------------------------反射相关的类和方法-----------------------
#在这下面写反射涉及的类和方法，没有就不用写！
#-keep class [your_pkg].** { *; }
-keepclasseswithmembers class * extends android.app.Dialog{
    public <init>(android.content.Context);
}

#---------------------------------自定义View的类------------------------
#在这下面写自定义View的类的类，没有就去掉这句话！
-keep class com.android.dsly.common.widget.** { *; }