package com.hoangiathinh.managerbook.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import com.hoangiathinh.managerbook.model.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel
) {

    var showAllBookList by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
//            Text(
//                text = "Hệ thống",
//                style = MaterialTheme.typography.headlineSmall,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Quản lí thư viện",
//                style = MaterialTheme.typography.headlineSmall,
//                textAlign = TextAlign.Center
//            )
            Text(
                text = "Hệ thống Quản lí thư viện",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxSize()) {
                Text("Sinh viên", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = viewModel.studentName.value,
                        onValueChange = { viewModel.studentName.value = it },
                        singleLine = true,
                        label = { Text("Nhập id") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val input = viewModel.studentName.value
                        if (input.isNotEmpty()) {
                            viewModel.loadDataForStudent(input)
                        }
                    }) {
                        Text("Thay đổi")
                    }
                }
                Spacer(Modifier.height(16.dp))

                Column(modifier = Modifier
                    .fillMaxSize()

                ) {
                    Text("Danh sách sách", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xffd8d8d8),
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth(),

                    ) {
                        val bookList = viewModel.borrowedBooks.value
                        if (bookList.isEmpty()) {
                            Text(
                                "Bạn chưa mượn quyển sách nào",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(5.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            LazyColumn(modifier = Modifier.padding(16.dp)) {
                                items(bookList) { book ->
                                    BookRow(
                                        title = book.title,
                                        checked = true,
                                        onCheckedChange = { isChecked ->
                                            if (!isChecked) {
                                                viewModel.returnBook(book)
                                            }
                                        }
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (viewModel.currentStudent.value != null) {
                            showAllBookList = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(48.dp),
                    enabled = viewModel.currentStudent.value != null
                ) {
                    Text("Thêm")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    if (showAllBookList) {
        AddBookDialog(
            viewModel = viewModel,
            onDismiss = { showAllBookList = false }
        )
    }
    }

@Composable
private fun BookRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked, onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary)
            )
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
@Composable
private fun AddBookDialog(viewModel: MainViewModel, onDismiss: () -> Unit) {
    val allBooks = viewModel.allBooksInLibrary.value
    val borrowedBooksIds = viewModel.borrowedBooks.value.map { it.id }.toSet()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Chọn sách để mượn", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(allBooks) { book ->
                        val isChecked = book.id in borrowedBooksIds
                        BookRow(
                            title = book.title,
                            checked = isChecked,
                            onCheckedChange = { newCheckedState ->
                                if (newCheckedState) {
                                    viewModel.borrowBook(book)
                                } else {
                                    viewModel.returnBook(book)
                                }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Xong")
                }
            }
        }
    }
}