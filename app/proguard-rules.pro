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

    # Keep Retrofit classes
    -keep class retrofit2.** { *; }
    -keep interface retrofit2.** { *; }
    -keepattributes Signature
    -keepattributes *Annotation*

    # Keep your service interfaces
    -keep interface com.filipebicho.pokerclash.bot.** { *; }

    # Keep your data classes
    -keep class com.filipebicho.pokerclash.bot.ChatRequest { *; }
    -keep class com.filipebicho.pokerclash.bot.ChatResponse { *; }
    -keep class com.filipebicho.pokerclash.bot.ResponseFormat { *; }
    -keep class com.filipebicho.pokerclash.bot.Message { *; }
    -keep class com.filipebicho.pokerclash.bot.MessageContent { *; }
    -keep class com.filipebicho.pokerclash.bot.Choice { *; }

    # Keep enums
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    # Keep classes used by reflection
    -keepnames class * {
        @retrofit2.http.* *;
    }
    -keep class com.filipebicho.pokerclash.bot.ChatgptApiInterface { *; }