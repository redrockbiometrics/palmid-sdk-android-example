package com.palmid.sdk.example

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.palmid.native_sdk.PalmIDNativeSDK
import com.palmid.sdk.example.ui.theme.PalmIDSDKExampleTheme

private val TAG = "PalmIDSDKExample"

class MainActivity : ComponentActivity() {
    private var palmServerEntrypoint: String = "https://api2.palmid.com/saas"
    private var appServerEntrypoint: String = "https://app.palmid.com/"
    private var projectId: String = ""  // Replace with your projectId
    private var requiredEnrollmentScans: Int = 1 // Optional. Required number of scans for enrollment. Default is 1.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PalmIDNativeSDK.getInstance().initialize(this, palmServerEntrypoint, appServerEntrypoint, projectId, requiredEnrollmentScans) { result ->
            Log.d(TAG, "palmid sdk init result: $result")
            Toast.makeText(this, "Initialize result: $result", Toast.LENGTH_LONG).show()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        enableEdgeToEdge()
        setContent {
            PalmIDSDKExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var userId by remember { mutableStateOf("") }
                    var showDialog by remember { mutableStateOf(false) }
                    var dialogMessage by remember { mutableStateOf("") }
                    
                    MainScreen(
                        activity = this@MainActivity,
                        userId = userId,
                        setUserId = { userId = it },
                        showDialog = { message ->
                            dialogMessage = message
                            showDialog = true
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                    
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Result") },
                            text = { Text(dialogMessage) },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    activity: Activity,
    userId: String,
    setUserId: (String) -> Unit,
    showDialog: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "UserId: $userId")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().enroll(activity, null) { result ->
                if (result.errorCode == 100004) {
                    showDialog("duplicate enrollment, palms are already registered")
                } else {
                    Log.d(TAG, "enroll result: $result")
                    showDialog("enroll result: $result")
                    val userIdValue = result?.data?.userId ?: ""
                    setUserId(userIdValue)
                }
            }
        }) {
            Text("Enroll")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().identify(activity, null) { result ->
                Log.d(TAG, "identify result: $result")
                showDialog("identify result: $result")
                val userIdValue = result?.data?.userId ?: ""
                setUserId(userIdValue)
            }
        }) {
            Text("Identify")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (userId == "") {
                showDialog("verification requires an input userId")
            } else {
                PalmIDNativeSDK.getInstance().verifyWithUserId(activity, userId, null) { result ->
                    Log.d(TAG, "verify result: $result")
                    showDialog("verify result: $result")
                }
            }
        }) {
            Text("Verify")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (userId == "") {
                showDialog("deleteUser requires an input userId")
            } else {
                PalmIDNativeSDK.getInstance().deleteUser(userId) { result ->
                    Log.d(TAG, "deleteUser result: $result")
                    showDialog("deleteUser result: $result")
                }
            }
        }) {
            Text("DeleteUser")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().releaseEngine()
            Log.d(TAG, "sdk released")
            showDialog("sdk released")
        }) {
            Text("Release")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PalmIDSDKExampleTheme {
    }
}