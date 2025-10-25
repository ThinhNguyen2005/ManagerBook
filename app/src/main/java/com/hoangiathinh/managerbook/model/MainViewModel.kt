package com.hoangiathinh.managerbook.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val currentStudent = mutableStateOf<UserModel?>(null)
    val borrowedBooks = mutableStateOf<List<BookModel>>(emptyList())
    val studentName = mutableStateOf("")
    val allBooksInLibrary = mutableStateOf<List<BookModel>>(loadAllBooksFromDataBase())

    fun loadDataForStudent(studentID: String) {
        val student = loadStudentFromDataBase(studentID)
        if (student != null) {
            currentStudent.value = student
            studentName.value = student.name

            val bookIDs = student.borrowedBookIds
            val books = getBooksByIds(bookIDs)
            borrowedBooks.value = books
        } else {
            currentStudent.value = null
            studentName.value = ""
            borrowedBooks.value = emptyList()
        }
    }

    fun borrowBook(bookToBorrow: BookModel) {
        // Thêm sách vào danh sách đã mượn
        if (!borrowedBooks.value.contains(bookToBorrow)) {
            val updatedBorrowedBooks = borrowedBooks.value.toMutableList()
            updatedBorrowedBooks.add(bookToBorrow)
            borrowedBooks.value = updatedBorrowedBooks
        }
        // Cập nhật trạng thái sách trong thư viện (nếu cần)
        updateBookStatusInDataBase(bookToBorrow.id, true)
    }

    fun returnBook(bookToReturn: BookModel) {
        // Xóa sách khỏi danh sách đã mượn
        val updatedBorrowedBooks = borrowedBooks.value.filter { it.id != bookToReturn.id }
        borrowedBooks.value = updatedBorrowedBooks

        // Cập nhật trạng thái sách trong thư viện (nếu cần)
        updateBookStatusInDataBase(bookToReturn.id, false)
    }

    private fun updateBookStatusInDataBase(bookId: String, isBorrowed: Boolean) {
        // Tìm sách trong danh sách tổng và cập nhật trạng thái
        val bookIndex = allBooksInLibrary.value.indexOfFirst { it.id == bookId }
        if (bookIndex != -1) {
            val updatedBooks = allBooksInLibrary.value.toMutableList()
            updatedBooks[bookIndex] = updatedBooks[bookIndex].copy(isBorrowed = isBorrowed)
            allBooksInLibrary.value = updatedBooks
        }
    }

    private fun loadStudentFromDataBase(studentId: String): UserModel? {
        val allStudents = listOf(
            UserModel("01", "Duc", listOf("SACH_01", "SACH_02", "SACH_03")),
            UserModel("02", "Thinh", listOf("SACH_01", "SACH_02")),
            UserModel("03", "Em yeu", listOf())
        )
        return allStudents.find { it.id == studentId }
    }

    private fun loadAllBooksFromDataBase(): List<BookModel> {
        return listOf(
            BookModel("SACH_01", "Sách 01", "Truyen tre em", false),
            BookModel("SACH_02", "Sách 02", "Truyen co tich", false),
            BookModel("SACH_03", "Sách 03", "Truyen dai ca", false),
            BookModel("SACH_04", "Sách 04", "Tiểu thuyết", false),
            BookModel("SACH_05", "Sách 05", "Khoa học", false)
        )
    }

    private fun getBooksByIds(ids: List<String>): List<BookModel> {
        val allBooks = loadAllBooksFromDataBase()
        return allBooks.filter { book -> book.id in ids }
    }
}