package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.Review

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Query("SELECT * FROM reviews WHERE bookId = :bookId ORDER BY reviewDate DESC")
    fun getReviewsByBook(bookId: Int): LiveData<List<Review>>

    @Query("SELECT AVG(starRating) FROM reviews WHERE bookId = :bookId")
    fun getAverageRating(bookId: Int): LiveData<Float?>

    @Query("SELECT * FROM reviews WHERE studentId = :studentId ORDER BY reviewDate DESC")
    fun getReviewsByStudent(studentId: Int): LiveData<List<Review>>
}
