package com.example.baitapmoi

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Task(
    val id: String = "",
    val title: String = "",
    val time: String = "",
    val category: String = "",
    val done: Boolean = false
)

@Composable
fun HomePage(onNotice:()-> Unit){
    val tasks = remember { mutableStateListOf<Task>() }
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val list = TaskApiClient.api.getTasks().map { it.toTask() }
            tasks.clear()
            tasks.addAll(list)
        } catch (e: Exception) {
            error = e.message
        }
    }
    val total = tasks.size
    val doneTask = tasks.count { it.done }
    val percent = if(total > 0 ) doneTask * 100 / total else 0
    val message = when {
        percent == 0 -> "Chưa hoàn thành 1 công việc nào cả"
        percent < 50 -> "Sắp được 1 nửa chặng đường rồi "
        percent <100 -> "Hơn 1/2 chặng đường rồi "
        else -> "Đã hoàn thành tất cả công việc"
    }
    val today = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("EEEE, d 'tháng' M", Locale.forLanguageTag("vi")))
        .replaceFirstChar {it.uppercase() }
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF4F6FA)).systemBarsPadding().padding(24.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ){
            val name = FirebaseAuth.getInstance().currentUser?.displayName
            Avatar(name)
            Spacer(Modifier.width(12.dp))
            Column()
            {
                Text(today, fontSize = 16.sp, color = Color.Gray)
                Text("Chào Bạn!", fontWeight = FontWeight.Bold, fontSize = 22.sp )
            }
            Spacer(Modifier.weight(1f))
            notice(onNotice)
        }
        Spacer(Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF2F5FE8), shape = RoundedCornerShape(16.dp)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column() {
                Text("Tiến độ hôm nay ", fontSize = 16.sp, color = Color.White)
                Text("$doneTask/$total Việc" , fontSize = 18.sp, fontWeight = FontWeight.Bold , color = Color.White)
                Text("$message " , fontSize = 18.sp, color = Color.White)

            }
            Spacer(Modifier.weight(1f))
            Box(
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(progress = { percent /100f}, color = Color.White , trackColor = Color.White.copy(alpha = 0.3f))
                Text("$percent%", color = Color.White)
            }
        }
        Spacer(Modifier.height(24.dp))
        error?.let { Text("Lỗi tải dữ liệu: $it", color = Color.Red) }
        TaskList(tasks, onToggle = { task ->
            val i = tasks.indexOf(task)
            tasks[i] = task.copy(done = !task.done)
            if(!task.done){
                val bundle = Bundle().apply { putString("category", task.category) }
                FirebaseAnalytics.getInstance(context).logEvent("task_completed", bundle)
            }
        }, modifier = Modifier.weight(1f))
    }
}
@Composable
private fun Avatar(name: String?){
    Box(
        modifier = Modifier.size(44.dp).background(Color(0xFFE8EEFD) ,CircleShape),
        contentAlignment = Alignment.Center
    ){
        if(!name.isNullOrBlank()) {
            val firstname = name.first().uppercase()
            Text(firstname,color = Color(0xFF2F5FE8))
        }
        else {
            Icon(Icons.Filled.Person, contentDescription = null)
        }
    }
}

@Composable
fun notice(onNotice: ()-> Unit ){
    IconButton(onClick = onNotice,modifier = Modifier.size(44.dp).background(Color.White, CircleShape)){
        Icon(Icons.Filled.NotificationsActive, contentDescription = null)
    }
}

@Composable
fun TaskList(tasks : List<Task> , onToggle: (Task) -> Unit , modifier:Modifier = Modifier){
    Column(modifier = modifier){
        Text("Việc hôm nay " , fontSize = 18.sp , fontWeight = FontWeight.Bold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
            items(tasks) { task ->
                TaskItem(task, onToggle)
            }
        }
    }
}


@Composable
fun TaskBar(current: String, onTabClick: (String) -> Unit, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()                     // chừa chỗ cho thanh điều hướng của máy
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround, // 5 phần rải đều
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem(Icons.Filled.Home, "Trang chủ", current == "home") { onTabClick("home") }
        TabItem(Icons.AutoMirrored.Filled.List, "Danh sách", current == "list") { onTabClick("list") }
        FloatingActionButton(
            onClick = onAdd,
            shape = CircleShape,
            containerColor = Color(0xFF2F5FE8),
            contentColor = Color.White,
            modifier = Modifier.offset(y = (-20).dp)     // nhô lên trên thanh
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Thêm công việc")
        }
        TabItem(Icons.Filled.NotificationsActive, "Thông báo", current == "notice") { onTabClick("notice") }
        TabItem(Icons.Filled.Tune, "Cài đặt", current == "settings") { onTabClick("settings") }
    }
}

@Composable
private fun TabItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) Color(0xFF2F5FE8) else Color.Gray   // tab đang chọn thì xanh
    Column(
        modifier = Modifier.clickable { onClick() }.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = color)
        Text(label, fontSize = 11.sp, color = color)
    }
}
@Composable
fun TaskItem(task: Task, onToggle: (Task) -> Unit){
        Row(modifier = Modifier.fillMaxWidth().background(Color.White , RoundedCornerShape(16.dp)).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.clickable { onToggle(task) }){
                if(task.done) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null)
                }
                else {
                    Icon(Icons.Outlined.RadioButtonUnchecked , contentDescription = null)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title)
                Row() {
                    Text(task.category)
                    Text(task.time)
                }
            }
            Box(){
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }
        }
}
