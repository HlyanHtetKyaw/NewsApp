package com.test.newsapp.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier, onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }

    val trailingIconView = @Composable {
        IconButton(onClick = {
            onSearch(text)
        }) {
            Icon(Icons.Filled.Search, contentDescription = "search", tint = Color.Blue)
        }
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (text.isEmpty()) {
                onSearch(text)
            }
        },
        label = { Text("Search...") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(text)
            }),
        singleLine = true,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        trailingIcon = trailingIconView
    )
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar()
}
