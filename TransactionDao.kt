package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY issueDate DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE studentId = :studentId ORDER BY issueDate DESC")
    fun getTransactionsByStudent(studentId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE bookId = :bookId ORDER BY issueDate DESC")
    fun getTransactionsByBook(bookId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE status = 'ISSUED' AND dueDate < :now")
    fun getOverdueTransactions(now: Long = System.currentTimeMillis()): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE studentId = :studentId AND status = 'ISSUED' LIMIT 1")
    suspend fun getActiveTransactionByStudent(studentId: Int): Transaction?

    @Query("UPDATE transactions SET returnDate = :returnDate, status = 'RETURNED' WHERE id = :transactionId")
    suspend fun returnBook(transactionId: Int, returnDate: Long = System.currentTimeMillis())

    @Query("UPDATE transactions SET status = 'OVERDUE' WHERE status = 'ISSUED' AND dueDate < :now")
    suspend fun markOverdueTransactions(now: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM transactions WHERE status = 'ISSUED'")
    fun getActiveBorrowCount(): LiveData<Int>
}
