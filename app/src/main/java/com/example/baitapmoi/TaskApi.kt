package com.example.baitapmoi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
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
    val subTask : List<SubtaskDto>
)
data class DoneBody(val done: Boolean)
fun TaskDto.toTask() = Task(
    id = id.toString(),
    title = title,
    time = time,
    category = if (category == "study") "Học tập" else "Cuộc sống",
    done = done,
    day = date,
    priority = priority
)
interface TaskApi {
    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @GET("mock/teach/tasks")
    suspend fun getTasks(): List<TaskDto>

    @Headers("Authorization: Bearer TuanTruong-uth-student")
    @PATCH("mock/teach/tasks/{id}")
    suspend fun updateDone(
        @Path("id") id: Int,
        @Body body: DoneBody
    ): TaskDto
}
object TaskApiClient {
    val api: TaskApi = Retrofit.Builder()
        .baseUrl("https://share.tryapps.store/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TaskApi::class.java)
}