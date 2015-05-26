# Vehicle Routing Problem application

The Application demonstrate OptaPlanner functionality on the Android platform.

## How to build and run this application

To build and run the application follow these steps:

1. Download android-sdk.
2. Clone this project.
3. Create `local.properties` file with one property:

```
sdk.dir=.../android-sdk
```

4. Run `gradle build`.
5. Run `gradle assembleDebug`.
6. Run `adb install app/build/outputs/apk/app-debug.apk`.
7. Open application in device.
