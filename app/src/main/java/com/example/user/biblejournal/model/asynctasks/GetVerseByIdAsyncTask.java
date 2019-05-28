package com.example.user.biblejournal.model.asynctasks;

import android.os.AsyncTask;

import com.example.user.biblejournal.model.Repository;
import com.example.user.biblejournal.model.database.verse.VerseDao;
import com.example.user.biblejournal.model.database.verse.VerseEntity;

public class GetVerseByIdAsyncTask extends AsyncTask<Integer, Void, VerseEntity> {
    private Repository.VerseRepositoryListener listener;
    private VerseDao mAsyncTaskDao;

    public GetVerseByIdAsyncTask(VerseDao dao, Repository.VerseRepositoryListener listener) {
        mAsyncTaskDao = dao;
        this.listener = listener;
    }

    @Override
    protected VerseEntity doInBackground(final Integer... params) {
        return mAsyncTaskDao.getVerse(params[0], params[1], params[2]);
    }

    @Override
    protected void onPostExecute(VerseEntity verseEntity) {
        super.onPostExecute(verseEntity);

        listener.onVerseRead(verseEntity);
    }
}