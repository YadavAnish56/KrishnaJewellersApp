# Proguard rules for Krishna Jewellers
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
