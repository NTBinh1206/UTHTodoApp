package com.example.baitapmoi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppLogo()
        }
        Spacer(Modifier.height(8.dp))
        Text("Chào mừng đến với \nUTH Todo", fontSize = 30.sp , fontWeight = FontWeight.Bold , lineHeight = 36.sp )
        Spacer(Modifier.height(8.dp))
        Text("Đăng nhập để lưu công việc và đồng bộ trên mọi thiết bị" , fontSize = 15.sp , textAlign = TextAlign.Left , color = Color.Gray)
        Spacer(Modifier.weight(1f))
        ButtonLoginGoogle(onGoogleLogin)
        Spacer(Modifier.height(8.dp))
        Lined("hoặc")
        Spacer(Modifier.height(8.dp))
        Anonymous(onGuest)
        Spacer(Modifier.height(16.dp))
        Text("Khi tiếp tục, bạn đồng ý với Điều khoản sử dụng và Chính sách quyền riêng tư của UTH Todo", color = Color.Gray , fontSize = 12.sp , modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}
@Composable
fun Lined(text: String){
    Row(
        Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        HorizontalDivider(
            color = Color(0xFFE5E7EB),
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Text(text , color = Color.Gray , modifier = Modifier.padding(horizontal = 12.dp) , fontSize = 13.sp)
        HorizontalDivider(
            color = Color(0xFFE5E7EB),
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun Anonymous(onGuestLogin:()-> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onGuestLogin) {
            Text("Dùng thử không cần tài khoản")
        }
    }
}
@Composable
fun ButtonLoginGoogle(onLogin:()-> Unit){

        OutlinedButton(onClick = onLogin, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Box(
                modifier = Modifier.size(24.dp).border(1.dp , Color(0xFFE5E7EB), CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text("G",fontSize = 12.sp , color = Color(0xFFE5E7EB))
            }
            Spacer(Modifier.width(12.dp))
            Text("Tiếp tục với google")
    }
}