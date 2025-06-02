# PalmID SDK Android Example

This is a simple example of how to use the PalmID SDK.

## Configuration

1. add the following permissions to your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

2. add the following to your `settings.gradle` file:

```gradle
maven {
    url = uri("https://storage.googleapis.com/download.flutter.io")
}
maven {
    url = uri("https://raw.githubusercontent.com/redrockbiometrics/palmid-sdk-android-repo/master")
}
```

3. add the following to your `build.gradle` file:

```gradle
implementation 'com.palmid:native_sdk:latest.version'
```

4. add the following to your `MainActivity` file:

```kotlin
PalmIDNativeSDK.getInstance().initialize(this, entrypoint, partnerId, projectId) { result ->
    Log.d(TAG, "palmid sdk init result: $result")
}
```

4.1 Parameters

| Parameter | Required | Description |
|------------|-------------|-------------|
| entrypoint | Yes | The entrypoint of the PalmID SDK. |
| partnerId | Yes | The partnerId of the PalmID SDK. |
| projectId | Yes | The projectId of the PalmID SDK. |
| accessToken | No | The accessToken of the PalmID SDK. If not provided, it will be automatically generated using partnerId and projectId. |
| requiredEnrollmentScans | No | Required number of scans for enrollment |

## Result Error Codes

| Description | Error Code |
|------------|-------------|
| SuccessException          | 100000  |
| UserCancelledException    | -1      |
| AuthenticationException   | 100001  |
| UserNotFoundException     | 100002  |
| NoMatchUserException      | 100003  |
| UserAlreadyExistsException| 100004  |
| EnrollException           | 100005  |
| IdentifyException         | 100006  |
| VerifyException           | 100007  |
| DeleteUserException       | 100008  |
| OtherException            | 999999  |