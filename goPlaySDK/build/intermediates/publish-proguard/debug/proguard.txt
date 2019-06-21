#
# This ProGuard configuration file illustrates how to process a program
# library, such that it remains usable as a library.
# Usage:
#     java -jar proguard.jar @library.pro
#

# Specify the input jars, output jars, and library jars.
# In this case, the input jar is the program library that we want to process.

#-injars  in.jar
#-outjars out.jar

#-libraryjars  <java.home>/lib/rt.jar

# Save the obfuscation mapping to a file, so we can de-obfuscate any stack
# traces later on. Keep a fixed source file attribute and all line number
# tables to get line numbers in the stack traces.
# You can comment this out if you're not interested in stack traces.

-printmapping out.map
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

# Preserve all annotations.

-keepattributes *Annotation*

# Preserve all fundamental application classes.
-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep class com.android.vending.billing.**

-keep class com.facebook.** {
   *;
}

-keepclassmembers class ** {
    public void onEvent*(**);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

# Preserve all classes that have special context constructors, and the
# constructors themselves.
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keep,includedescriptorclasses class com.android.volley.** {
    <fields>;
    <methods>;
}

# Preserve all classes that have special context constructors, and the
# constructors themselves.
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}



-keepclassmembers class * extends android.os.Parcelable {
    static ** CREATOR;
}



# Preserve the special fields of all Parcelable implementations.
-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Preserve the required interface from the License Verification Library
# (but don't nag the developer if the library is not used at all).
-keep public interface  com.android.vending.licensing.ILicensingService

# Preserve the special static methods that are required in all enumeration
# classes.
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# -keepclasseswithmembernames class com.vtcmobile.gamesdk.widgets.FloatButton {
#    <fields>;
#    <methods>;
# }
-keep public class com.goplay.gamesdk.core.GoPlaySDK {
    public <methods>;
}
-keep public class com.goplay.gamesdk.common.GoPlayAction {
    public static <fields>;
}
-keep public class com.goplay.gamesdk.core.GoPlaySDKVersion {
    public static <fields>;
}

-keep public class com.goplay.gamesdk.models.GoPlaySession {
    public <fields>;
}

-keep public class com.goplay.gamesdk.core.GoPlayReceiver {
    public <methods>;
}


-keep class de.greenrobot.event.** {
    <fields>;
    <methods>;
}

-keep class com.gc.materialdesign.** {
    <fields>;
    <methods>;
}

-keep class org.apache.** {
    <fields>;
    <methods>;
}
-keep class android.net.** {
    <fields>;
    <methods>;
}
-keep class com.android.internal.http.multipart.** {
    <fields>;
    <methods>;
}
-keep,includedescriptorclasses class com.vtcmobile.gamesdk.widgets.TabPageIndicator {
    <fields>;
    <methods>;
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
