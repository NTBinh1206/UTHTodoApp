package com.example.baitapmoi

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ===== MODEL: dữ liệu của 1 thông báo =====
data class Notice(
    val type: String,      // "due" (sắp đến hạn), "late" (quá hạn), "done" (làm tốt), "remind"
    val title: String,
    val message: String,
    val time: String,
    val unread: Boolean
)

// ===== VIEWMODEL: giữ danh sách thông báo + xử lý dữ liệu =====
// View (NoticePage) chỉ đọc vm.notices để vẽ và gọi vm.markAllRead() khi bấm nút
class NoticeViewModel : ViewModel() {

    val notices = mutableStateListOf(
        Notice("due", "Sắp đến hạn", "Ôn chương 3 – Cấu trúc dữ liệu lúc 14:00.", "13:45", true),
        Notice("late", "Đã quá hạn", "Bài đọc tham khảo môn Mạng máy tính chưa xong.", "10:10", true),
        Notice("done", "Làm tốt lắm!", "Bạn đã xong 2 việc trong sáng nay.", "11:30", false),
        Notice("remind", "Nhắc việc", "Họp nhóm đồ án trong tuần này.", "Hôm qua", false)
    )

    // init chạy 1 lần khi ViewModel được tạo
    init {
        viewModelScope.launch {
            delay(10_000)       // chờ 10 giây (10_000 mili giây), không làm đơ giao diện
            notices.clear()     // danh sách rỗng -> NoticePage tự vẽ lại thành emptyView
        }
    }

    // Đánh dấu tất cả là đã đọc: thay từng phần tử bằng bản copy có unread = false
    fun markAllRead() {
        notices.indices.forEach { i ->
            notices[i] = notices[i].copy(unread = false)
        }
    }
}
