# Project specific ProGuard rules are defined here
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Widget classes
-keep public class * extends android.appwidget.AppWidgetProvider

# Keep View constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep RemoteViews
-keepclassmembers class ** extends android.widget.RemoteViews {
    public <init>(java.lang.String, int);
}
