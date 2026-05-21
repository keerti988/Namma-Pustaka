package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.Student

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): Student?

    @Query("SELECT * FROM students ORDER BY totalPagesRead DESC LIMIT 10")
    fun getLeaderboard(): LiveData<List<Student>>

    @Query("UPDATE students SET totalPagesRead = totalPagesRead + :pages WHERE id = :studentId")
    suspend fun addPagesRead(studentId: Int, pages: Int)

    @Query("SELECT * FROM students WHERE name LIKE '%' || :query || '%'")
    fun searchStudents(query: String): LiveData<List<Student>>
}
