package com.nammapustaka.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val studentId: Int,
    val issueDate: Long = System.currentTimeMillis(),
    val dueDate: Long = System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000L), // 14 days
    val returnDate: Long? = null,
    val status: String = STATUS_ISSUED  // ISSUED, RETURNED, OVERDUE
) {
    companion object {
        const val STATUS_ISSUED = "ISSUED"
        const val STATUS_RETURNED = "RETURNED"
        const val STATUS_OVERDUE = "OVERDUE"
    }

    val isOverdue: Boolean
        get() = returnDate == null && System.currentTimeMillis() > dueDate
}
