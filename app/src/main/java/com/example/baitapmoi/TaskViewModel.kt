package com.example.baitapmoi

import android.R.attr.id
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    val tasks = mutableStateListOf<Task>()
    var error by mutableStateOf<String?>(null)
    private set
    init{ load() }
    fun load(){
        viewModelScope.launch{
            try {
                error = null
                val list = TaskApiClient.api.getTasks().map {it.toTask()}
                tasks.clear()
                tasks.addAll(list)
            } catch(e: Exception) {
                error = e.message
            }
        }
    }
    fun toggle(task: Task ){
        val i = tasks.indexOf(task)
        if (i == -1) return
        tasks[i] = task.copy(done = !task.done)       //  đổi trên máy NGAY, để giao diện phản hồi tức thì
        viewModelScope.launch {
            try {
                TaskApiClient.api.updateDone(task.id.toInt(), DoneBody(!task.done))   //  báo server
            } catch (e: Exception) {
                tasks[i] = task                       //  server lỗi thì trả lại như cũ
                error = e.message
            }
        }
    }

    // Sửa công việc (màn Sửa bấm Lưu): thay Task cũ bằng bản mới có cùng id
    fun update(task: Task) {
        val i = tasks.indexOfFirst { it.id == task.id }
        if (i == -1) return
        val old = tasks[i]                 // 1. nhớ bản cũ
        tasks[i] = task                    // 2. đổi ngay
        viewModelScope.launch {            // 3.
            try {
                TaskApiClient.api.updateTask(task.id.toInt(),task.toDto())
            } catch (e: Exception) {
                tasks[i] = old
                error = e.message
            }
        }
    }

    // Thêm việc mới (màn Việc mới bấm Lưu): tự tạo id = id lớn nhất + 1
    fun add(task: Task) {
        val nextId = (tasks.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0) + 1
        val newTask = task.copy(id = nextId.toString())
        tasks.add(0, newTask)
        viewModelScope.launch {
            try {
                TaskApiClient.api.createTask(newTask.toDto())
            } catch (e: Exception) {
                tasks.remove(newTask)
                error = e.message
            }
        }
    }

    // Xoá công việc có cùng id
    fun delete(task: Task) {
        val i = tasks.indexOfFirst { it.id == task.id }
        if (i == -1) return
        tasks.removeAt(i)
        viewModelScope.launch {
            try {
                val res = TaskApiClient.api.deleteTask(task.id.toInt())
                if (!res.isSuccessful) throw Exception("Xoá thất bại: ${res.code()}")
            } catch (e: Exception) {
                tasks.add(minOf(i, tasks.size), task)   // trả về đúng chỗ cũ
                error = e.message
            }
        }
    }
}