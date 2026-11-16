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
                    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
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
                        "detail" -> {
                            val task = taskVm.tasks.find {it.id == selectedId}
                            if(task!=null){
                                DetailWork(
                                    task = task ,
                                    onBack = {screen = "home"},
                        onEdit = { screen = "edit" },
                        onToggle = { taskVm.toggle(task) }, onDelete = { taskVm.delete(task); screen = "home" }, onUpdate = { taskVm.update(it) }
                    )
                            }
                        }
                "new" -> EditTask(
                    task = Task(category = "Học tập", day = java.time.LocalDate.now().toString(), time = "19:00", remind = true, priority = "medium"),
                    isNew = true,
                    onCancel = { screen = "home" },
                    onSave = { newTask ->
                        taskVm.add(newTask)
                        screen = "home"
                    }
                )
                "edit" -> {
                    val task = taskVm.tasks.find { it.id == selectedId }
                    if (task != null) {
                        EditTask(
                            task = task,
                            onCancel = { screen = "detail" },
                            onSave = { updated ->
                                taskVm.update(updated)
                                screen = "detail"
                            }
                        )
                    }
                }
                        "home", "list", "notice", "settings" -> Column(Modifier.fillMaxSize()) {
                            Box(Modifier.weight(1f)) {
                                when (screen) {
                                    "home" -> HomePage(vm = taskVm ,onNotice = { screen = "notice" } ,
                                        onOpen ={task -> selectedId = task.id; screen = "detail" } )
                                    "list" -> ListPage(vm = taskVm , onOpen = {task -> selectedId = task.id; screen = "detail" })
                                    "notice" -> NoticePage(onAddTask = { screen = "new" }, onOpenSettings = { screen = "settings" })
                                    "settings" -> Text("Màn Cài đặt (chưa làm)")
                                }
                            }
                            TaskBar(
                                current = screen,
                                onTabClick = { tab -> screen = tab },
                                onAdd = { screen = "new" }
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