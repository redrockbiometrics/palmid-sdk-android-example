package com.palmid.sdk.example

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.palmid.sdk.example.ui.theme.PalmIDSDKExampleTheme
import androidx.compose.ui.unit.dp
import com.palmid.native_sdk.PalmIDNativeSDK
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private val TAG = "PalmIDSDKExample"

class MainActivity : ComponentActivity() {
    private var entrypoint: String = "https://api-us-west.redrockbiometrics.com/saas/api/"
    private var partnerId: String = ""  // Replace with your partnerId
    private var projectId: String = ""  // Replace with your projectId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PalmIDNativeSDK.getInstance().initialize(this, entrypoint, partnerId, projectId) { result ->
            Log.d(TAG, "palmid sdk init result: $result")
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        enableEdgeToEdge()
        setContent {
            PalmIDSDKExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var palmId by remember { mutableStateOf("") }
                    MainScreen(
                        activity = this@MainActivity,
                        palmId = palmId,
                        setPalmId = { palmId = it },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    activity: Activity,
    palmId: String,
    setPalmId: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "PalmId: $palmId")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().identify(activity, null) { result ->
                Log.d(TAG, "identify result: $result")
                val palmIdValue = result?.data?.palmId ?: ""
                setPalmId(palmIdValue)
            }
        }) {
            Text("Identify")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().enroll(activity, null) { result ->
                Log.d(TAG, "enroll result: $result")
                val palmIdValue = result?.data?.palmId ?: ""
                setPalmId(palmIdValue)
            }
        }) {
            Text("Enroll")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().verifyWithPalmId(activity, palmId, null) { result ->
                Log.d(TAG, "verify result: $result")
            }
        }) {
            Text("Verify")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().deleteUser(palmId) { result ->
                Log.d(TAG, "deleteUser result: $result")
            }
        }) {
            Text("DeleteUser")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            PalmIDNativeSDK.getInstance().releaseEngine()
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