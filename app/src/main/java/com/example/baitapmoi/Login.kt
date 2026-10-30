package com.example.baitapmoi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
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

@Composable
fun LoginPage(onGoogleLogin: ()-> Unit , onGuest: ()-> Unit){
    Column( modifier = Modifier.fillMaxSize().background(Color.White).systemBarsPadding().padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppLogo()
        }
        Spacer(Modifier.height(8.dp))
        Text("Welcome to UTH Todo", fontSize = 30.sp , fontWeight = FontWeight.Bold )
        Spacer(Modifier.height(8.dp))
        Text("Đăng nhập để lưu công việc và đồng bộ trên mọi thiết bị" , fontSize = 15.sp , textAlign = TextAlign.Left , color = Color.Gray)
        Spacer(Modifier.height(32.dp))
        ButtonLoginGoogle(onGoogleLogin)
        Spacer(Modifier.height(8.dp))
        Lined("hoặc")
        Spacer(Modifier.height(8.dp))
        Anonymous(onGoogleLogin)
        Spacer(Modifier.weight(1f))
        Text("Chính sách bảo mật - Điều khoản dịch vụ", color = Color.Gray , fontSize = 12.sp , modifier = Modifier.fillMaxWidth().padding(24.dp), textAlign = TextAlign.Center)
    }
}
@Composable
fun Lined(text: String){
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Text(text , color = Color.Gray , modifier = Modifier.padding(horizontal = 12.dp))
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun Anonymous(onGuestLogin:()-> Unit){
        TextButton(onClick = onGuestLogin){
            Text("Dùng thử không cần tài khoản")
        }
}
@Composable
fun ButtonLoginGoogle(onLogin:()-> Unit){
        OutlinedButton(onClick = onLogin , modifier = Modifier.fillMaxWidth().height(52.dp)){
            Text("Tiếp tục với google")
    }
}