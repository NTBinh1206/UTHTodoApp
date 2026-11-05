package com.example.baitapmoi

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object TaskRepository {

    // Đường dẫn: users / {mã người dùng} / tasks
    private fun tasksRef() = FirebaseFirestore.getInstance()
        .collection("users")
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .collection("tasks")

    // Lắng nghe: dữ liệu trên Firestore đổi → gọi onChange với danh sách mới
    fun listen(onChange: (List<Task>) -> Unit): ListenerRegistration =
        tasksRef().orderBy("time").addSnapshotListener { snapshot, _ ->
            val list = snapshot?.documents?.map { doc ->
                doc.toObject(Task::class.java)!!.copy(id = doc.id)
            } ?: emptyList()
            onChange(list)
        }

    // Thêm việc mới
    fun add(task: Task) {
        tasksRef().add(task.copy(id = ""))
    }

    // Tick / bỏ tick
    fun toggle(task: Task) {
        tasksRef().document(task.id).update("done", !task.done)
    }
}