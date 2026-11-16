package com.example.baitapmoi

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ===== VIEW: chỉ hiển thị dữ liệu từ ViewModel và báo sự kiện lên =====
// vm = viewModel(): Compose tự tạo NoticeViewModel lần đầu, các lần sau lấy lại đúng cái cũ
@Composable
fun NoticePage(
    vm: NoticeViewModel = viewModel(),
    onAddTask: () -> Unit = {},        // nút "Thêm việc cần làm" ở emptyView
    onOpenSettings: () -> Unit = {}    // chữ "Cài đặt nhắc việc" ở emptyView
) {
    val notices = vm.notices

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Thông báo", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            // Chỉ hiện nút khi còn thông báo
            if (notices.isNotEmpty()) {
                Text(
                    "Đánh dấu đã đọc",
                    color = Color(0xFF2F5FE8),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { vm.markAllRead() }
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        if (notices.isEmpty()) {
            // Danh sách rỗng -> hiện emptyView, chiếm hết phần còn lại của màn hình
            EmptyNotice(
                onAddTask = onAddTask,
                onOpenSettings = onOpenSettings,
                modifier = Modifier.weight(1f)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                // forEachIndexed để biết dòng nào là dòng cuối (dòng cuối không cần đường kẻ)
                notices.forEachIndexed { index, notice ->
                    NoticeRow(notice)
                    if (index < notices.lastIndex) {
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                    }
                }
            }
        }
    }
}

@Composable
private fun NoticeRow(notice: Notice) {
    val (icon, color) = when (notice.type) {
        "late" -> Icons.Outlined.WarningAmber to Color.Red
        "done" -> Icons.Outlined.Check to Color(0xFF2E9E5B)
        else -> Icons.Outlined.Alarm to Color(0xFF2F5FE8)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (notice.unread) Color(0xFFF3F6FF) else Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 1. Ô icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        // 2. Khoảng cách (trong Row nên dùng width)
        Spacer(Modifier.width(12.dp))
        // 3. Tiêu đề + giờ + nội dung
        Column(Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    notice.title,
                    fontWeight = if (notice.unread) FontWeight.Bold else FontWeight.Normal
                )
                Text(notice.time, fontSize = 12.sp, color = Color.Gray)
            }
            Text(notice.message, fontSize = 14.sp, color = Color.Gray)
        }
        // 4. Chấm xanh: nằm NGOÀI Column, bên phải cùng của Row
        if (notice.unread) {
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .padding(top = 6.dp)
                    .size(8.dp)
                    .background(Color(0xFF2F5FE8), CircleShape)
            )
        }
    }
}

// ===== emptyView: hiện khi không còn thông báo nào =====
@Composable
private fun EmptyNotice(onAddTask: () -> Unit, onOpenSettings: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Vòng tròn xanh nhạt > vòng tròn trắng > chuông, kèm huy hiệu tick xanh lá ở góc
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(Color(0xFFE8EEFD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color(0xFF2F5FE8),
                    modifier = Modifier.size(44.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)       // đặt riêng huy hiệu ở góc dưới phải
                    .offset(x = (-14).dp, y = (-14).dp)
                    .size(34.dp)
                    .background(Color(0xFF2E9E5B), CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Chưa có thông báo nào", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Khi có việc sắp đến hạn, quá hạn hoặc lời nhắc, bạn sẽ thấy chúng ở đây.",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onAddTask,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F5FE8))
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(6.dp))
            Text("Thêm việc cần làm", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onOpenSettings) {
            Text("Cài đặt nhắc việc", color = Color(0xFF2F5FE8), fontWeight = FontWeight.Bold)
        }
    }
}
