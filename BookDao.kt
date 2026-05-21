package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.Book

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books ORDER BY addedDate DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): LiveData<Book?>

    @Query("SELECT * FROM books WHERE category = :category ORDER BY title ASC")
    fun getBooksByCategory(category: String): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchBooks(query: String): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE qrCode = :qrCode LIMIT 1")
    suspend fun getBookByQrCode(qrCode: String): Book?

    @Query("UPDATE books SET isAvailable = :available WHERE id = :bookId")
    suspend fun updateAvailability(bookId: Int, available: Boolean)

    @Query("SELECT DISTINCT category FROM books ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<String>>

    @Query("SELECT COUNT(*) FROM books")
    fun getTotalBookCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM books WHERE isAvailable = 1")
    fun getAvailableBookCount(): LiveData<Int>
}
