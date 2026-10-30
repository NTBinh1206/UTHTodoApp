package com.example.baitapmoi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.baitapmoi.ui.theme.BaiTapMoiTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaiTapMoiTheme {
                    var screen by rememberSaveable { mutableStateOf("intro")}
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    when (screen) {
                        "intro" -> IntroductionScreen(onFinish = { screen = "login" })
                        "login" -> LoginPage(
                        onGoogleLogin = {
                            scope.launch {
                                signInWithGoogle(
                                    context,
                                    onSuccess = { screen = "home" },
                                    onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                                )
                            }
                        },
                        onGuest = { screen = "home" }
                        )
                        "home" -> Text("Xin chào ${FirebaseAuth.getInstance().currentUser?.email ?: "bạn (khách)"}")
                    }
                }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaiTapMoiTheme {

    }
}