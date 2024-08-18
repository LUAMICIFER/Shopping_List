package com.example.udeemy7shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.tooling.data.EmptyGroup.name
import androidx.compose.ui.unit.dp

data class ShoppingItem(var id:Int,var name:String , var quantity:Int,var editable:Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sitems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDailog by remember {mutableStateOf(false)}
    var itemName by remember { mutableStateOf("") }
    var itemquantity by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(),verticalArrangement= Arrangement.Center,horizontalAlignment= Alignment.CenterHorizontally) {
        Button(onClick = { showDailog = true },modifier= Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "add item")

        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sitems){
                item ->
                if (item.editable){
                    Sshopinglisteditor(item = item, editbutonclick = {
                        editedName,editedQuantity ->
                        sitems = sitems.map{it.copy(editable = false)}
                        var editedItem = sitems.find{ it.id == item.id}
                        editedItem?.let {
                            item->
                            item.name = editedName
                            item.quantity = editedQuantity
//                            it.name = editedName
//                            it.quantity = editedQuantity
                        }
                    }) {
                        
                    }
                }
                else{
                    ShoppingListItem(item = item , editbutonclick = {
//                        finding which item edit button we are clicking
                        sitems = sitems.map { it.copy(editable = it.id == item.id) } }, onDeleteClick = {sitems = sitems- item})
                }
            }
        }
    }
    if(showDailog){
        AlertDialog(onDismissRequest = { showDailog = false }, confirmButton = {
                                                                               Row(modifier = Modifier
                                                                                   .fillMaxWidth()
                                                                                   .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                                                                   Button(onClick = {
                                                                                       if(itemName.isNotBlank()){
                                                                                           val newItem = ShoppingItem(
                                                                                               name = itemName,
                                                                                               quantity = itemquantity.toInt(),
                                                                                               id = sitems.size+1
                                                                                           )
                                                                                           sitems = sitems + newItem
                                                                                           showDailog = false
                                                                                           itemName=""
                                                                                           itemquantity=""
                                                                                       }
                                                                                   }) {
                                                                                       Text(text = "Add")
                                                                                   }
                                                                                   Button(onClick = { showDailog=false }) {
                                                                                       Text(text = "cancel")
                                                                                   }

                                                                               }
        },title= { Text (text = "Add shopping item") },
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange = {itemName = it},label = { Text("Item Name") }, singleLine = true,modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                    OutlinedTextField(value = itemquantity, onValueChange = {itemquantity = it},label = { Text("Item Quantity") }, singleLine = true,modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp))
                }
            }
        )
    }
}
@Composable
fun ShoppingListItem(item : ShoppingItem, editbutonclick: () -> Unit,onDeleteClick: () -> Unit ){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(BorderStroke(2.dp, Color.Cyan)), horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(text = item.name, modifier= Modifier.padding(8.dp))
        Text(text = item.quantity.toString(), modifier= Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = editbutonclick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
            
        }
    }

}

@Composable
fun Sshopinglisteditor(item : ShoppingItem,editbutonclick: (String , Int) ->Unit , onDeleteClick: () -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.editable) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(value = editedName, onValueChange = {editedName = it }, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity = it }, singleLine = true, modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))

        }
        Button(onClick = {isEditing = false
        editbutonclick(editedName,editedQuantity.toIntOrNull() ?:1)}) {
            Text(text = "Save")
        }
    }
}