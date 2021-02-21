package com.klim.habrareader.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.klim.habrareader.data.db.tables.Author;
import com.klim.habrareader.data.db.tables.Post;

import java.util.List;

@Dao
public interface AuthorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Author author);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Author... authors);

    @Delete
    void delete(Author author);

    @Query("SELECT * FROM authors WHERE nickname = :nickname")
    Author getAuthor(String nickname);

}