package com.example.baitapmoi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class IntroPage(
    val icon : ImageVector,
    val title : String ,
    val description : String ,
)

private val introPages = listOf(
    IntroPage(Icons.Filled.School,"Mọi việc gọn trong một chỗ" ,"Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào "),
    IntroPage(Icons.Filled.Checklist,"Chia theo học tập và Cuộc sống" ,"Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì "),
    IntroPage(Icons.Filled.NotificationsActive, "Nhắc đúng lúc" ,"Bật thông báo để UTH Todo nhắc bạn trước hạn chót "),
)
private val introBlue = Color(0xFF2F5FE8)
@Composable
fun IntroductionScreen(onFinish:() -> Unit ){
    var index by rememberSaveable { mutableIntStateOf(0)}
    val page = introPages[index]
    val isLast = index == introPages.lastIndex
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),

    ){
        Row(Modifier.fillMaxWidth().height(48.dp) , verticalAlignment = Alignment.CenterVertically ){
            Box(
                modifier = Modifier.size(28.dp).background(introBlue,RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ){
               Icon(Icons.Filled.Checklist, contentDescription = null , tint = Color.White , modifier=Modifier.size(18.dp))
            }
            Spacer(Modifier.width(8.dp))
            Text("UTH", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(" Todo", color = introBlue, fontSize = 18.sp)
            Spacer(Modifier.weight(1f))
            if(!isLast){ TextButton(onClick = onFinish){Text("Bỏ qua" , color = Color.Gray)}}
        }
        Spacer(Modifier.weight(1f))
        IntroContent(page)
        Spacer(Modifier.weight(1f))
        PageIndicator(total = introPages.size, current = index)
        Spacer(Modifier.height(24.dp))
        Button(onClick ={ if(!isLast){index++} else {onFinish()} } ,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = introBlue)
            ){
            if(isLast) Text("Bắt đầu ngay") else Text("Tiếp tục")
        }
    }
}
@Composable
private fun IntroContent(page : IntroPage){
    Column( modifier = Modifier.fillMaxWidth()){
        Box(modifier = Modifier.align(Alignment.CenterHorizontally).size(200.dp).background(introBlue.copy(alpha=0.1f), RoundedCornerShape(40.dp)) ,
            contentAlignment = Alignment.Center
            ){
            Box(Modifier.size(96.dp).shadow(8.dp,CircleShape).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center) {
                Icon(
                    page.icon,
                    contentDescription = null,
                    tint = introBlue,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(Modifier.height(48.dp))
        Text(page.title , fontSize = 26.sp , fontWeight = FontWeight.Bold , lineHeight = 32.sp)
        Spacer(Modifier.height(12.dp))
        Text(page.description , fontSize = 15.sp , color = Color.Gray)
    }
}
@Composable
private fun PageIndicator(total : Int , current: Int ){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
        repeat(total){ i->
            Box(Modifier.height(8.dp).width(if( i== current) 24.dp else 8.dp).background(if(i==current) introBlue else Color.LightGray, CircleShape)){}
        }
    }
}