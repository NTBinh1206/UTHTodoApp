package com.example.baitapmoi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
private val blueUTH = Color(0xFF2F5FE8)
@Composable
fun Ready(){
    Column(modifier = Modifier.fillMaxSize().background(Color.Blue) ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
        AppLogo()
        Spacer(Modifier.height(16.dp))
        Text("UTH Todo", fontSize = 30.sp , fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Việc học, việc nhà - gọn gàng mỗi ngày " , fontSize = 10.sp)

    }
}
@Composable
fun AppLogo(){
    Box(){
        Icon(
            imageVector = Icons.Filled.Checklist,
            contentDescription = "Logo" ,
            tint = blueUTH ,
            modifier = Modifier.size(56.dp)
        )
    }
}