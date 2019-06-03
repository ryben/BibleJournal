package com.example.user.biblejournal.model.asynctasks

import android.os.AsyncTask

import com.example.user.biblejournal.model.Repository
import com.example.user.biblejournal.model.database.verse.VerseDao
import com.example.user.biblejournal.model.database.verse.VerseEntity

class GetVerseByIdAsyncTask(private val mAsyncTaskDao: VerseDao, private val listener: Repository.VerseRepositoryListener) : AsyncTask<Int, Void, VerseEntity>() {

    override fun doInBackground(vararg params: Int?): VerseEntity {
        return mAsyncTaskDao.getVerse(params[0]!!, params[1]!!, params[2]!!)
    }

    override fun onPostExecute(verseEntity: VerseEntity) {
        super.onPostExecute(verseEntity)

        listener.onVerseRead(verseEntity)
    }
}