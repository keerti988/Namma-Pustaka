package com.nammapustaka.ui

import android.app.Application
import androidx.lifecycle.*
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.model.Review
import com.nammapustaka.data.model.Student
import com.nammapustaka.data.model.Transaction
import com.nammapustaka.data.repository.LibraryRepository
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LibraryRepository

    val allBooks: LiveData<List<Book>>
    val allStudents: LiveData<List<Student>>
    val leaderboard: LiveData<List<Student>>
    val allTransactions: LiveData<List<Transaction>>
    val overdueTransactions: LiveData<List<Transaction>>
    val totalBookCount: LiveData<Int>
    val availableBookCount: LiveData<Int>
    val activeBorrowCount: LiveData<Int>
    val allCategories: LiveData<List<String>>

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    val searchResults: LiveData<List<Book>> = Transformations.switchMap(_searchQuery) { query ->
        if (query.isBlank()) repository.allBooks
        else repository.searchBooks(query)
    }

    private val _selectedCategory = MutableLiveData<String?>(null)
    val selectedCategory: LiveData<String?> = _selectedCategory

    val filteredBooks: LiveData<List<Book>> = Transformations.switchMap(_selectedCategory) { cat ->
        if (cat == null) repository.allBooks
        else repository.getBooksByCategory(cat)
    }

    init {
        val db = AppDatabase.getDatabase(application)
        repository = LibraryRepository(db)
        allBooks = repository.allBooks
        allStudents = repository.allStudents
        leaderboard = repository.leaderboard
        allTransactions = repository.allTransactions
        overdueTransactions = repository.overdueTransactions
        totalBookCount = repository.totalBookCount
        availableBookCount = repository.availableBookCount
        activeBorrowCount = repository.activeBorrowCount
        allCategories = repository.allCategories
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun insertBook(book: Book) = viewModelScope.launch {
        repository.insertBook(book)
    }

    fun updateBook(book: Book) = viewModelScope.launch {
        repository.updateBook(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        repository.deleteBook(book)
    }

    fun getBookById(id: Int) = repository.getBookById(id)

    fun getBooksByCategory(category: String) = repository.getBooksByCategory(category)

    fun issueBook(bookId: Int, studentId: Int) = viewModelScope.launch {
        val transaction = Transaction(bookId = bookId, studentId = studentId)
        repository.issueBook(transaction)
    }

    fun returnBook(transactionId: Int, bookId: Int, pages: Int, studentId: Int) =
        viewModelScope.launch {
            repository.returnBook(transactionId, bookId, pages, studentId)
        }

    fun insertStudent(student: Student) = viewModelScope.launch {
        repository.insertStudent(student)
    }

    fun getTransactionsByStudent(studentId: Int) = repository.getTransactionsByStudent(studentId)

    fun getTransactionsByBook(bookId: Int) = repository.getTransactionsByBook(bookId)

    fun addReview(review: Review) = viewModelScope.launch {
        repository.insertReview(review)
    }

    fun getReviewsByBook(bookId: Int) = repository.getReviewsByBook(bookId)

    fun getAverageRating(bookId: Int) = repository.getAverageRating(bookId)

    suspend fun getBookByQrCode(qrCode: String) = repository.getBookByQrCode(qrCode)

    fun markOverdue() = viewModelScope.launch {
        repository.markOverdue()
    }
}
