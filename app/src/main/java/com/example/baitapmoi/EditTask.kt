package com.example.baitapmoi

import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val EditBlue = Color(0xFF2F5FE8)
private val EditOrange = Color(0xFFE8833A)

@Composable
fun EditTask(task: Task, isNew: Boolean = false, onCancel: () -> Unit, onSave: (Task) -> Unit) {
    val context = LocalContext.current

    // ===== 1. STATE: mỗi ô trong form có 1 biến, giá trị ban đầu lấy từ task =====
    var title by rememberSaveable { mutableStateOf(task.title) }
    var note by rememberSaveable { mutableStateOf(task.note) }
    var category by rememberSaveable { mutableStateOf(task.category) }
    var day by rememberSaveable { mutableStateOf(task.day) }       // dạng "2026-11-12"
    var time by rememberSaveable { mutableStateOf(task.time) }     // dạng "06:30"
    var remind by rememberSaveable { mutableStateOf(task.remind) }
    var priority by rememberSaveable { mutableStateOf(task.priority) }

    // Nút Back của điện thoại = bấm "Hủy"
    BackHandler { onCancel() }

    // ===== 2. TÍNH TOÁN =====
    // 3 lựa chọn ngày: (chữ hiển thị, ngày thật)
    val today = LocalDate.now()
    val dayOptions = listOf(
        "Hôm nay" to today,
        "Ngày mai" to today.plusDays(1),
        "Tuần này" to today.with(DayOfWeek.SUNDAY)   // Chủ nhật của tuần này
    )
    // Ô ngày nào đang được chọn (lấy ô đầu tiên khớp với biến day)
    val selectedDay = dayOptions.firstOrNull { it.second.toString() == day }?.first

    // "06:30" -> "06:30 AM" để hiển thị
    val timeLabel = runCatching {
        LocalTime.parse(time).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US))
    }.getOrDefault(time)

    // Gói các giá trị mới vào bản copy của task rồi gửi lên
    val save = {
        onSave(
            task.copy(
                title = title.trim(),
                note = note.trim(),
                category = category,
                day = day,
                time = time,
                remind = remind,
                priority = priority
            )
        )
    }

    // ===== 3. GIAO DIỆN =====
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // --- Thanh trên: Hủy | Sửa công việc | Lưu ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("Hủy", color = EditBlue)
            }
            Text(if (isNew) "Việc mới" else "Sửa công việc", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            TextButton(onClick = save, enabled = title.isNotBlank()) {
                Text("Lưu", fontWeight = FontWeight.Bold, color = if (title.isNotBlank()) EditBlue else Color.LightGray)
            }
        }

        // Phần form cuộn được (màn nhỏ không bị mất nội dung)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            // --- Thẻ Tên công việc / Ghi chú ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tên công việc", fontSize = 12.sp, color = Color.Gray)
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        decorationBox = { innerTextField ->
                            Box {
                                if (title.isEmpty()) {
                                    Text("Ví dụ: Ôn chương 4 – Giải thuật", fontSize = 18.sp, color = Color.Gray)
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Column(Modifier.padding(16.dp)) {
                    Text("Ghi chú", fontSize = 12.sp, color = Color.Gray)
                    BasicTextField(
                        value = note,
                        onValueChange = { note = it },
                        textStyle = TextStyle(fontSize = 15.sp),
                        modifier = Modifier.fillMaxWidth(),
                        // decorationBox: "trang trí" quanh ô nhập -> hiện chữ mờ khi ô trống
                        decorationBox = { innerTextField ->
                            Box {
                                if (note.isEmpty()) {
                                    Text("Thêm ghi chú (không bắt buộc)", fontSize = 15.sp, color = Color.Gray)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // --- Nhóm ---
            EditLabel("Nhóm")
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ChoiceBox(
                    label = "Học tập",
                    selected = category == "Học tập",
                    color = EditBlue,
                    onClick = { category = "Học tập" },
                    modifier = Modifier.weight(1f)
                )
                ChoiceBox(
                    label = "Cuộc sống",
                    selected = category == "Cuộc sống",
                    color = EditOrange,
                    onClick = { category = "Cuộc sống" },
                    modifier = Modifier.weight(1f)
                )
            }

            // --- Ngày ---
            EditLabel("Ngày")
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dayOptions.forEach { (label, date) ->
                    ChoiceBox(
                        label = label,
                        selected = selectedDay == label,
                        color = EditBlue,
                        onClick = { day = date.toString() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- Thẻ Giờ / Nhắc tôi ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(12.dp))
                    Text("Giờ")
                    Spacer(Modifier.weight(1f))
                    // Ô giờ: bấm vào mở hộp chọn giờ của Android
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF1F5))
                            .clickable {
                                val current = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(8, 0))
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute -> time = "%02d:%02d".format(hour, minute) },
                                    current.hour,
                                    current.minute,
                                    false
                                ).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(timeLabel, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Nhắc tôi")
                        Text("Trước 15 phút", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = remind,
                        onCheckedChange = { remind = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF34C759)
                        )
                    )
                }
            }

            // --- Mức ưu tiên (3 nút dính liền nhau) ---
            EditLabel("Mức ưu tiên")
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFE6E8EE), RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                // (giá trị lưu trong API, chữ hiển thị)
                listOf("low" to "Thấp", "medium" to "Vừa", "high" to "Cao").forEach { (value, label) ->
                    val selected = priority == value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) Color.White else Color.Transparent)
                            .clickable { priority = value }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) Color.Black else Color.Gray
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // --- Nút Lưu thay đổi (luôn nằm ở đáy) ---
        Button(
            onClick = save,
            enabled = title.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EditBlue)
        ) {
            Text(if (isNew) "Lưu công việc" else "Lưu thay đổi", fontWeight = FontWeight.Bold)
        }
    }
}

// Tiêu đề nhỏ màu xám phía trên mỗi phần (Nhóm, Ngày, Mức ưu tiên)
@Composable
private fun EditLabel(text: String) {
    Text(
        text,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
    )
}

// Ô lựa chọn có viền: dùng cho Nhóm và Ngày
@Composable
private fun ChoiceBox(
    label: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (selected) color.copy(alpha = 0.12f) else Color.White)
            .border(1.dp, if (selected) color else Color(0xFFE0E0E0), shape)
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontWeight = FontWeight.Bold, color = if (selected) color else Color.Black)
    }
}
