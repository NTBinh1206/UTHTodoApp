package com.example.baitapmoi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

data class SubtaskDto (
    val title : String ,
    val done : Boolean
)
data class TaskDto (
    val id : Int,
    val title : String,
    val note : String,
    val category : String,
    val date : String ,
    val time : String ,
    val priority : String ,
    val remind : Boolean ,
    val done : Boolean ,
    val subTask : List<SubtaskDto>
)

fun TaskDto.toTask() = Task(
    id = id.toString(),
    title = title,
    time = time,
    category = if (category == "study") "Học tập" else "Cuộc sống",
    done = done
)
interface TaskApi {
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @GET("mock/teach/tasks")
    suspend fun getTasks(): List<TaskDto>
}
object TaskApiClient {
    val api: TaskApi = Retrofit.Builder()
        .baseUrl("https://share.tryapps.store/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}