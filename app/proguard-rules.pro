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
# ----------------------------------
# 1. 保留所有继承自View的类的构造方法，以免在XML布局中引用时出错
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 2. 保留Activity中的方法，以免被混淆后无法被系统调用
-keep class * extends android.app.Activity {
    public void *(android.view.View);
}

# 3. 保留所有继承自Application的类，它们是应用的入口
-keep public class * extends android.app.Application

# 4. 保留所有Android支持库中的类和方法
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class com.google.android.material.** { *; } # 如果你用了Material库

# 5. 保留实现了Parcelable接口的类，用于序列化
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 6. 保留实现了Serializable接口的类
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 7. 保留Native方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# 8. 保留枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 9. 保留自定义View的完整构造方法
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# ----------------------------------
# 对于 com.licheedev:android-serialport
# ----------------------------------

# 保持整个包下的所有类及其公共成员不被混淆
-keep class android.serialport.** { *; }
-keep class com.licheedev.android.serialport.** { *; }

# 保持所有实现 Serializable 接口的类（序列化相关）
-keepnames class * implements java.io.Serializable

# 保持 Serializable 实现类的特定成员，这是标准规则
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持所有本地方法（JNI）不被混淆，这是最重要的规则之一
-keepclasseswithmembernames class * {
    native <methods>;
}

# ----------------------------------
# 对于 org.yaml:snakeyaml
# ----------------------------------

# 1. 首先，保持snakeyaml库自身的代码不被混淆
-keep class org.yaml.snakeyaml.** { *; }

# 2. 最重要的部分：保持所有通过YAML加载/转储的Bean对象
# 如果你有明确的Bean类，最好明确列出（推荐，最安全）
# -keep class com.yourpackage.model.** { *; }

# 如果没有明确的包，或者Bean类分布很散，可以使用以下通用规则：
# 保持所有类的setter、getter方法以及无参构造函数
# 这条规则会保留所有类的 类名、以"set"/"get"开头的方法、以及字段名。
-keepclassmembers class * {
    public void set*(***);
    public *** get*();
    public *** is*();
    public <init>();
}

# 可选：如果你在YAML中使用类的全限定名作为tag，则需要保持类名
# -keepnames class com.yourpackage.model.**
