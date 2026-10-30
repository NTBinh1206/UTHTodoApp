package com.example.baitapmoi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomePage(onNotice:()-> Unit){
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ){
            val name = FirebaseAuth.getInstance().currentUser?.displayName
            Avatar(name)
            notice(onNotice)
        }
    }
}
@Composable
private fun Avatar(name: String?){
    Box(
        modifier = Modifier.size(44.dp).background(Color(0xFFE8EEFD) ,CircleShape),
        contentAlignment = Alignment.Center
    ){
        if(!name.isNullOrBlank()) {
            val firstname = name.first().uppercase()
            Text(firstname,color = Color.Blue)
        }
        else {
            Icon(Icons.Filled.Person, contentDescription = null)
        }
    }
}

@Composable
fun notice(onNotice: ()-> Unit ){
    IconButton(onClick = onNotice,modifier = Modifier.size(44.dp).background(Color.White, CircleShape)){
        Icon(Icons.Filled.NotificationsActive, contentDescription = null)
    }
}
@Composable
fun TaskBar(onAdd:()->Unit){
    Row(modifier = Modifier.fillMaxWidth().background(Color.White)){
        Box(
            modifier =Modifier.size(33.dp)
        ){
            Icon(Icons.Filled.Home, contentDescription = null)
        }
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier.size(33.dp)
        ){
            Icon(Icons.Filled.List, contentDescription = null)
        }
        Spacer(Modifier.width(4.dp))
        FloatingActionButton(
                onClick = onAdd,
                shape = CircleShape,
                containerColor = Color(0xFF2F5FE8),
                contentColor = Color.White
            ){
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        Spacer(Modifier.width(4.dp))
        Box(modifier = Modifier.size(33.dp)){
            Icon(Icons.Filled.NotificationsActive, contentDescription = null)
        }
    }
}

