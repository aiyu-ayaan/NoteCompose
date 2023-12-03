package com.atech.note.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.atech.note.data.database.model.Note
import com.atech.note.utils.RootScope
import com.atech.note.utils.noteList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val dao: NoteDao

    companion object{
        const val DATABASE_NAME = "notes_database"
    }
    class NoteDatabaseCallback @Inject constructor(
        private val database: Provider<NoteDatabase>,
        @RootScope private val coroutineScope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            database.get().dao.let { dao ->
                coroutineScope.launch {
                    dao.insertAll(noteList)
                }
            }
        }
    }
}