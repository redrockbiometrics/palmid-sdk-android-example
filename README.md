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
implementation 'com.palmid:native_sdk:1.3.6'
```


## API Reference

```java
/**
 * Initializes the PalmID SDK engine with required credentials and configuration.
 *
 * @param ctx
 * @param palmServerEntrypoint (Optional) Custom backend API endpoint URL. Pass null to use tdefault endpoint.
 * @param appServerEntrypoint (Optional) Custom backend API endpoint URL. Pass null to use tdefault endpoint.
 * @param projectId  (Required) Project identifier for service segregation. Must not be null.
 * @param requiredEnrollmentScans (Optional) Required number of scans for enrollment. Pass nuif not required.
 * @param completion Callback with initialization result (success/failure).
 */
public void initialize(@NonNull Context ctx,
                        @androidx.annotation.Nullable String palmServerEntrypoint,
                        @androidx.annotation.Nullable String appServerEntrypoint,
                        @NonNull String projectId,
                        @androidx.annotation.Nullable Integer requiredEnrollmentScans,
                        @NonNull PalmIDNativeSDKCompletion completion)

/**
 * Verifies a user's palm print against a registered palm ID.
 *
 * @param userId         Pre-registered user identifier to verify against.
 * @param activity       Host activity for presenting verification UI.
 * @param loadController (Optional) Custom loading UI controller. Pass null for default UI.
 * @param result         Callback with verification result (success/failure and metadata).
 */
public void verifyWithUserId(
    @NonNull android.app.Activity activity,
    @NonNull String userId,
    @androidx.annotation.Nullable Object loadController,
    @NonNull PalmIDNativeSDKResult result
);

/**
 * Identifies a user by capturing and matching their palm print.
 *
 * @param activity       Host activity for presenting capture UI.
 * @param loadController (Optional) Custom loading UI controller. Pass null for default UI.
 * @param result         Callback with identification result (matched palm ID or error).
 */
public void identify(
    @NonNull android.app.Activity activity,
    @androidx.annotation.Nullable Object loadController,
    @NonNull PalmIDNativeSDKResult result
);

/**
 * Enrolls a new user by capturing and registering their palm print.
 *
 * @param activity       Host activity for presenting enrollment UI.
 * @param loadController (Optional) Custom loading UI controller. Pass null for default UI.
 * @param result         Callback with enrollment result (success/failure status).
 */
public void enroll(
    @NonNull android.app.Activity activity,
    @androidx.annotation.Nullable Object loadController,
    @NonNull PalmIDNativeSDKResult result
);

/**
 * Removes a registered user from the palm recognition system.
 *
 * @param userId Unique identifier of the user to be removed.
 * @param result Callback with deletion result (success/failure status).
 */
public void deleteUser(
    @NonNull String userId,
    @NonNull PalmIDNativeSDKResult result
);

/**
 * Releases the engine resources.
 * Can be called multiple times. If the initialization parameter [autoReleaseEngine] is true, this method does not need to be called.
 */
public void releaseEngine();
```


### PalmIDNativeResult

```java
public class PalmIDNativeResult {
    private int errorCode;
    @NonNull
    private String errorMsg;
    @Nullable
    private PalmIDNativeResultData data;
}

public class PalmIDNativeResultData {
    @NonNull
    private String userId;
    private double score;
    @Nullable
    private String sessionId; // Only has value when appServerEntrypoint is specified during initialization
}
```

### errorCode

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