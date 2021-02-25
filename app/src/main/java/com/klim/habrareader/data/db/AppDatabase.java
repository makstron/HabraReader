package com.klim.habrareader.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.klim.habrareader.data.db.dao.AuthorDAO;
import com.klim.habrareader.data.db.dao.PostDAO;
import com.klim.habrareader.data.db.tables.Author;
import com.klim.habrareader.data.db.tables.Post;

@Database(entities = {Post.class, Author.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PostDAO getPostDao();
    public abstract AuthorDAO getAuthorDao();
}