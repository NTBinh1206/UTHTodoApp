package com.example.baitapmoi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.baitapmoi.ui.theme.BaiTapMoiTheme
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
                    val taskVm: TaskViewModel = viewModel()
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
                        onGuest = { signInAsGuest(
                            onSuccess = { screen = "home" },
                            onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
                        )
                        }
                        )
                        "home", "list", "notice", "settings" -> Column(Modifier.fillMaxSize()) {
                            Box(Modifier.weight(1f)) {
                                when (screen) {
                                    "home" -> HomePage(vm = taskVm ,onNotice = { screen = "notice" })
                                    "list" -> ListPage(vm = taskVm)
                                    "notice" -> Text("Màn Thông báo (chưa làm)")
                                    "settings" -> Text("Màn Cài đặt (chưa làm)")
                                }
                            }
                            TaskBar(
                                current = screen,
                                onTabClick = { tab -> screen = tab },
                                onAdd = {}
                            )
                        }
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