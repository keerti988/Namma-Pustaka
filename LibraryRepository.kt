package com.nammapustaka.data.repository

import androidx.lifecycle.LiveData
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.model.Review
import com.nammapustaka.data.model.Student
import com.nammapustaka.data.model.Transaction

class LibraryRepository(private val db: AppDatabase) {

    // ---- Books ----
    val allBooks: LiveData<List<Book>> = db.bookDao().getAllBooks()
    val totalBookCount: LiveData<Int> = db.bookDao().getTotalBookCount()
    val availableBookCount: LiveData<Int> = db.bookDao().getAvailableBookCount()
    val allCategories: LiveData<List<String>> = db.bookDao().getAllCategories()

    suspend fun insertBook(book: Book): Long = db.bookDao().insertBook(book)
    suspend fun updateBook(book: Book) = db.bookDao().updateBook(book)
    suspend fun deleteBook(book: Book) = db.bookDao().deleteBook(book)
    fun getBookById(id: Int) = db.bookDao().getBookById(id)
    fun getBooksByCategory(category: String) = db.bookDao().getBooksByCategory(category)
    fun searchBooks(query: String) = db.bookDao().searchBooks(query)
    suspend fun getBookByQrCode(qrCode: String) = db.bookDao().getBookByQrCode(qrCode)
    suspend fun updateBookAvailability(bookId: Int, available: Boolean) =
        db.bookDao().updateAvailability(bookId, available)

    // ---- Students ----
    val allStudents: LiveData<List<Student>> = db.studentDao().getAllStudents()
    val leaderboard: LiveData<List<Student>> = db.studentDao().getLeaderboard()

    suspend fun insertStudent(student: Student): Long = db.studentDao().insertStudent(student)
    suspend fun updateStudent(student: Student) = db.studentDao().updateStudent(student)
    suspend fun getStudentById(id: Int) = db.studentDao().getStudentById(id)
    fun searchStudents(query: String) = db.studentDao().searchStudents(query)
    suspend fun addPagesRead(studentId: Int, pages: Int) =
        db.studentDao().addPagesRead(studentId, pages)

    // ---- Transactions ----
    val allTransactions: LiveData<List<Transaction>> = db.transactionDao().getAllTransactions()
    val activeBorrowCount: LiveData<Int> = db.transactionDao().getActiveBorrowCount()
    val overdueTransactions: LiveData<List<Transaction>> =
        db.transactionDao().getOverdueTransactions()

    suspend fun issueBook(transaction: Transaction): Long {
        db.bookDao().updateAvailability(transaction.bookId, false)
        return db.transactionDao().insertTransaction(transaction)
    }

    suspend fun returnBook(transactionId: Int, bookId: Int, pages: Int, studentId: Int) {
        db.transactionDao().returnBook(transactionId)
        db.bookDao().updateAvailability(bookId, true)
        db.studentDao().addPagesRead(studentId, pages)
    }

    fun getTransactionsByStudent(studentId: Int) =
        db.transactionDao().getTransactionsByStudent(studentId)

    fun getTransactionsByBook(bookId: Int) =
        db.transactionDao().getTransactionsByBook(bookId)

    suspend fun markOverdue() = db.transactionDao().markOverdueTransactions()

    // ---- Reviews ----
    suspend fun insertReview(review: Review) = db.reviewDao().insertReview(review)
    fun getReviewsByBook(bookId: Int) = db.reviewDao().getReviewsByBook(bookId)
    fun getAverageRating(bookId: Int) = db.reviewDao().getAverageRating(bookId)
}
