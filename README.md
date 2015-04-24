# Vehicle Routing Problem

## Summary

The Application demonstrate OptaPlanner functionality on the Android platform.

## Build

To build and run the application follow these steps:

1. Download adnroid-sdk.
2. Clone this project.
3. Create `local.properties` file with two properties:

```
build-tools.version=x.y.z
sdk.dir=.../android-sdk
```

4. Run `gradle editDx`
5. Run `gradle build`
6. Run `gradle assembleDebug`
7. Run `adb install app/build/outputs/apk/app-debug.apk`
