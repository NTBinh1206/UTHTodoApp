package com.example.baitapmoi

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
}