package com.grandmasteredge.core.data

import androidx.room.*

@Entity(tableName = "puzzles")
data class PuzzleEntity(
    @PrimaryKey val id: String,
    val fen: String,
    val solution: String, // Comma-separated moves
    val difficulty: Int,
    val category: String
)

@Dao
interface PuzzleDao {
    @Query("SELECT * FROM puzzles WHERE id = :id")
    suspend fun getPuzzleById(id: String): PuzzleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuzzle(puzzle: PuzzleEntity)

    @Query("SELECT * FROM puzzles")
    suspend fun getAllPuzzles(): List<PuzzleEntity>
}

@Database(entities = [PuzzleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun puzzleDao(): PuzzleDao
}
