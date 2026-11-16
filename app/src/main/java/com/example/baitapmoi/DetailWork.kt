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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailWork(
    task: Task,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit,          // xoá cả công việc
    onUpdate: (Task) -> Unit       // lưu task sau khi đổi danh sách việc nhỏ
) {
    val subTotal = task.subtasks.size
    val subDone = task.subtasks.count { it.done }
    val subProgress = if (subTotal > 0) subDone.toFloat() / subTotal else 0f

    // State riêng của màn chi tiết
    var newSub by rememberSaveable { mutableStateOf("") }              // chữ trong ô "Thêm việc nhỏ"
    var editingIndex by rememberSaveable { mutableStateOf<Int?>(null) } // đang sửa việc nhỏ thứ mấy (null = không sửa)
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }  // có đang hiện hộp "Xoá công việc?" không

    // Các thao tác với việc nhỏ: tạo danh sách MỚI rồi copy vào task (không sửa trực tiếp list cũ)
    fun toggleSub(index: Int) = onUpdate(
        task.copy(subtasks = task.subtasks.mapIndexed { i, s -> if (i == index) s.copy(done = !s.done) else s })
    )
    fun deleteSub(index: Int) = onUpdate(
        task.copy(subtasks = task.subtasks.filterIndexed { i, _ -> i != index })
    )
    fun renameSub(index: Int, newTitle: String) = onUpdate(
        task.copy(subtasks = task.subtasks.mapIndexed { i, s -> if (i == index) s.copy(title = newTitle) else s })
    )
    fun addSub() {
        if (newSub.isBlank()) return
        onUpdate(task.copy(subtasks = task.subtasks + SubtaskDto(title = newSub.trim(), done = false)))
        newSub = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { onBack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.ArrowBackIosNew, contentDescription = null,
                    tint = Color(0xFF2F5FE8), modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Trang chủ", color = Color(0xFF2F5FE8))
            }

            // Cụm 2: sửa
            Text(
                "Sửa", color = Color(0xFF2F5FE8), fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onEdit() })
        }

        // Phần nội dung cuộn được (nhiều việc nhỏ thì không bị tràn)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val (label, color) = when (task.priority) {
                    "high" -> "Ưu tiên cao" to Color.Red
                    "medium" -> "Ưu tiên vừa" to Color.Gray
                    else -> "Ưu tiên thấp" to Color.Gray
                }
                CategoryChip(task.category)
                Tag(label, color)
                if (task.done) {
                    Tag("Đã xong", Color(0xFF2E9E5B))
                }
            }
            Text(
                task.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp),
                textDecoration = if (task.done) TextDecoration.LineThrough else null,
                color = if (task.done) Color.Gray else Color.Black
            )
            Spacer(Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                InfoRow(Icons.Outlined.CalendarToday, "Hạn chót", dateLabel(task.day, task.time))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                InfoRow(
                    Icons.Outlined.Notifications,
                    "Nhắc nhở",
                    if (task.remind) "Trước 15p" else "Không"
                )
            }
            Spacer(Modifier.height(24.dp))
            if (task.note.isNotBlank()) {
                Text(
                    "Ghi chú",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(task.note)
                }
                Spacer(Modifier.height(24.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Việc nhỏ", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text("$subDone/$subTotal", color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { subProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(6.dp),
                color = Color(0xFF2F5FE8),
                trackColor = Color(0xFFE6E8EE),
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
            Spacer(Modifier.height(12.dp))

            // --- Thẻ việc nhỏ: danh sách + dòng "Thêm việc nhỏ" ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                if (task.subtasks.isEmpty()) {
                    Text(
                        "Chưa có việc nhỏ nào. Chia nhỏ để dễ bắt đầu hơn.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                } else {
                    // forEachIndexed: giống forEach nhưng cho biết thêm vị trí (index) của phần tử
                    task.subtasks.forEachIndexed { index, sub ->
                        SubtaskRow(
                            sub = sub,
                            onToggle = { toggleSub(index) },
                            onEdit = { editingIndex = index },
                            onDelete = { deleteSub(index) }
                        )
                    }
                }
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF2F5FE8))
                    Spacer(Modifier.width(12.dp))
                    BasicTextField(
                        value = newSub,
                        onValueChange = { newSub = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 15.sp),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner ->
                            Box {
                                if (newSub.isEmpty()) Text("Thêm việc nhỏ", color = Color.Gray, fontSize = 15.sp)
                                inner()
                            }
                        }
                    )
                    TextButton(onClick = { addSub() }, enabled = newSub.isNotBlank()) {
                        Text(
                            "Thêm",
                            fontWeight = FontWeight.Bold,
                            color = if (newSub.isNotBlank()) Color(0xFF2F5FE8) else Color.Gray
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Button(
            onClick = onToggle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F5FE8))
        ) {
            Text(if (task.done) "Đánh dấu chưa xong" else "Đánh dấu hoàn thành", fontWeight = FontWeight.Bold)
        }
        TextButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Xóa công việc", fontWeight = FontWeight.Bold, color = Color.Red)
        }
        Spacer(Modifier.height(8.dp))
    }

    // ===== Hộp thoại xác nhận xoá công việc =====
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xóa công việc?") },
            text = { Text("\"${task.title}\" sẽ bị xoá khỏi danh sách.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) { Text("Xóa", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Hủy") }
            }
        )
    }

    // ===== Hộp thoại sửa tên việc nhỏ =====
    // let: chỉ chạy khi editingIndex khác null, và đặt tên nó là index
    editingIndex?.let { index ->
        var text by rememberSaveable(index) { mutableStateOf(task.subtasks.getOrNull(index)?.title ?: "") }
        AlertDialog(
            onDismissRequest = { editingIndex = null },
            title = { Text("Sửa việc nhỏ") },
            text = {
                OutlinedTextField(value = text, onValueChange = { text = it }, singleLine = true)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        renameSub(index, text.trim())
                        editingIndex = null
                    },
                    enabled = text.isNotBlank()
                ) { Text("Lưu", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { editingIndex = null }) { Text("Hủy") }
            }
        )
    }
}

// Một dòng việc nhỏ: ô tick vuông + tên + nút sửa + nút xoá
@Composable
private fun SubtaskRow(sub: SubtaskDto, onToggle: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                if (sub.done) Icons.Filled.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (sub.done) Color(0xFF2F5FE8) else Color.LightGray
            )
        }
        Text(
            sub.title,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,      // tên dài quá thì hiện "..."
            textDecoration = if (sub.done) TextDecoration.LineThrough else null,
            color = if (sub.done) Color.Gray else Color.Black
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Outlined.Edit, contentDescription = "Sửa", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Outlined.Delete, contentDescription = "Xoá", tint = Color.Red, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.Gray)
        Spacer(Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.Bold)
    }
}
