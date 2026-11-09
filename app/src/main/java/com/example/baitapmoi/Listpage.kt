package com.example.baitapmoi

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable
fun ListPage(vm: TaskViewModel) {
    var query by rememberSaveable { mutableStateOf("") }        // chữ đang gõ trong ô tìm
    var filter by rememberSaveable { mutableStateOf("Tất cả") } // chip đang chọn

    val notDoneCount = vm.tasks.count { !it.done }               // đếm MỌI việc chưa xong

    // Lọc danh sách theo ô tìm + chip
    val shown = vm.tasks.filter { task ->
        task.title.contains(query, ignoreCase = true) &&
                (filter == "Tất cả" || task.category == filter)
    }
    val todo = shown.filter { !it.done }
    val doneList = shown.filter { it.done }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFF4F6FA)).statusBarsPadding().padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Danh sách" ,fontSize = 28.sp , fontWeight = FontWeight.Bold)
            Text("$notDoneCount việc chưa xong" , color = Color.Gray)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Tìm công việc") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Tất cả", "Học tập", "Cuộc sống").forEach { label ->
                FilterChipItem(label, selected = filter == label) { filter = label }
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item { SectionTitle("CẦN LÀM") }
            items(todo) { task -> ListTaskRow(task, onToggle = { vm.toggle(task) }) }

            if (doneList.isNotEmpty()) {
                item { SectionTitle("ĐÃ XONG") }
                items(doneList) { task -> ListTaskRow(task, onToggle = { vm.toggle(task) }) }
            }

            if (shown.isEmpty()) {
                item { Text("Không tìm thấy công việc nào.", color = Color.Gray) }
            }
        }
    }

}
@Composable
private fun FilterChipItem(label: String, selected: Boolean, onClick: () -> Unit) {
    val bgcolor = if (selected) Color.Black else Color.White
    val textColor = if(selected) Color.White else Color.Black
    Box(
        modifier = Modifier.clickable { onClick()}.background(bgcolor, RoundedCornerShape(50)).padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Text(label , color = textColor, fontSize = 14.sp )
    }
}
@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
}
@Composable
private fun ListTaskRow(task: Task, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.clickable { onToggle() }){
            if(task.done) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null , tint = Color(0xFF2F5FE8))
            }
            else {
                Icon(Icons.Outlined.RadioButtonUnchecked , contentDescription = null , tint = Color.LightGray)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ){
            Text(
                task.title,
                textDecoration = if (task.done) TextDecoration.LineThrough else null,
                color = if (task.done) Color.Gray else Color.Black
            )
            if (!task.done) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                   CategoryChip(task.category)
                    Text(dateLabel(task.day, task.time), fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
        if (task.priority == "high" && !task.done) {
            Text(
                "Gấp",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

private fun dateLabel(date: String, time: String): String {
    val d = runCatching { LocalDate.parse(date) }.getOrNull() ?: return time
    val today = LocalDate.now()
    val dayText = when {
        d == today -> "Hôm nay"
        d == today.plusDays(1) -> "Ngày mai"
        else -> "${d.dayOfMonth}/${d.monthValue}"
    }
    return "$dayText, $time"
}