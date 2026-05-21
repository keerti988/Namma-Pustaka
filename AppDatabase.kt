package com.nammapustaka.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.model.Review
import com.nammapustaka.data.model.Student
import com.nammapustaka.data.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Book::class, Student::class, Transaction::class, Review::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun studentDao(): StudentDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_pustaka_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateSampleData(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateSampleData(db: AppDatabase) {
            // Sample Students
            val students = listOf(
                Student(name = "Ravi Kumar", className = "6A", rollNumber = "001"),
                Student(name = "Priya Sharma", className = "7B", rollNumber = "002"),
                Student(name = "Arjun Nair", className = "8A", rollNumber = "003"),
                Student(name = "Kavitha Reddy", className = "6B", rollNumber = "004"),
                Student(name = "Suresh Gowda", className = "7A", rollNumber = "005")
            )
            students.forEach { db.studentDao().insertStudent(it) }

            // Sample Books
            val books = listOf(
                Book(
                    title = "Chandamama Kathe",
                    author = "K. Shivaram Karanth",
                    category = "Story",
                    summary = "A classic collection of moon stories for children",
                    summaryKannada = "ಮಕ್ಕಳಿಗಾಗಿ ಚಂದ್ರನ ಕಥೆಗಳ ಸಂಕಲನ",
                    totalPages = 120,
                    qrCode = "BOOK001"
                ),
                Book(
                    title = "Vigyana Vishwa",
                    author = "T.R. Anantharamu",
                    category = "Science",
                    summary = "Exploring the wonders of science for young minds",
                    summaryKannada = "ಯುವ ಮನಸ್ಸುಗಳಿಗಾಗಿ ವಿಜ್ಞಾನದ ಅದ್ಭುತಗಳ ಅನ್ವೇಷಣೆ",
                    totalPages = 180,
                    qrCode = "BOOK002"
                ),
                Book(
                    title = "Karnataka Ithihasa",
                    author = "M. Chidananda Murthy",
                    category = "History",
                    summary = "The rich history of Karnataka from ancient times",
                    summaryKannada = "ಪ್ರಾಚೀನ ಕಾಲದಿಂದ ಕರ್ನಾಟಕದ ಶ್ರೀಮಂತ ಇತಿಹಾಸ",
                    totalPages = 250,
                    qrCode = "BOOK003"
                ),
                Book(
                    title = "Malgudi Days",
                    author = "R.K. Narayan",
                    category = "Story",
                    summary = "Short stories set in the fictional town of Malgudi",
                    summaryKannada = "ಮಾಲ್ಗುಡಿ ಎಂಬ ಕಾಲ್ಪನಿಕ ನಗರದಲ್ಲಿ ನಡೆಯುವ ಕಥೆಗಳು",
                    totalPages = 200,
                    qrCode = "BOOK004"
                ),
                Book(
                    title = "Ganitha Saurabha",
                    author = "S. Narasimhan",
                    category = "Science",
                    summary = "Making mathematics fun and easy for school students",
                    summaryKannada = "ಶಾಲಾ ವಿದ್ಯಾರ್ಥಿಗಳಿಗಾಗಿ ಗಣಿತವನ್ನು ಮೋಜಾಗಿ ಮಾಡುವ ಪ್ರಯತ್ನ",
                    totalPages = 160,
                    qrCode = "BOOK005"
                )
            )
            books.forEach { db.bookDao().insertBook(it) }
        }
    }
}
