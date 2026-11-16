package com.example.baitapmoi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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
    val subtasks : List<SubtaskDto>,
)
data class DoneBody(val done: Boolean)
fun TaskDto.toTask() = Task(
    id = id.toString(),
    title = title,
    time = time,
    category = if (category == "study") "Học tập" else "Cuộc sống",
    done = done,
    day = date,
    priority = priority,
    note = note ,
    subtasks = subtasks,
    remind = remind

)
fun Task.toDto() = TaskDto(
    id = id.toIntOrNull() ?: 0,
    title = title,
    note = note,
    category = if (category == "Học tập") "study" else "life",
    date = day,
    // 👉 bạn điền nốt 5 trường: time, priority, remind, done, subtasks
    time = time,
    priority = priority,
    remind = remind,
    done = done,
    subtasks = subtasks
)
interface TaskApi {
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @GET("mock/teach/tasks")
    suspend fun getTasks(): List<TaskDto>
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @POST("mock/teach/tasks")
    suspend fun createTask(@Body body: TaskDto): TaskDto
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @PATCH("mock/teach/tasks/{id}")
    suspend fun updateDone(
        @Path("id") id: Int,
        @Body body: DoneBody
    ): TaskDto
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @PATCH("mock/teach/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Int, @Body body: TaskDto): TaskDto

    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @DELETE("mock/teach/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}
object TaskApiClient {
    val api: TaskApi = Retrofit.Builder()
        .baseUrl("https://share.tryapps.store/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}