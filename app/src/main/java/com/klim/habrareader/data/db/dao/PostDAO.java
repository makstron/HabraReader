package com.klim.habrareader.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.klim.habrareader.data.db.tables.Post;
import com.klim.habrareader.data.repositories.post.dataSources.local.models.PostDetailsWithAuthor;
import com.klim.habrareader.data.repositories.post.dataSources.local.models.PostThumbWithAuthor;

import java.util.List;

@Dao
public interface PostDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM posts WHERE id = :id")
    Post getPost(int id);

    @Query("SELECT count(*) > 0 FROM posts WHERE id = :id")
    boolean postExist(int id);

    @Query("SELECT short_description FROM posts WHERE id = :id")
    String getPostShortDescription(int id);

    @Query("SELECT post_image FROM posts WHERE id = :id")
    String getPostImage(int id);

    @Query("SELECT description FROM posts WHERE id = :id")
    String getPostDescription(int id);

    @Query("SELECT * FROM posts ORDER BY created_timestamp DESC")
    List<Post> getAllPosts();

    @Query("SELECT p.id, p.title, p.post_image, p.short_description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
            "FROM posts p " +
            "JOIN authors a ON p.author_nickname = a.nickname " +
            "WHERE p.votes_count >= :votesCount " +
            "ORDER BY p.created_timestamp DESC " +
            "LIMIT 20")
    List<PostThumbWithAuthor> getAllPostsThumbs(int votesCount);

    @Query("SELECT p.id, p.title, p.post_image, p.short_description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
            "FROM posts p " +
            "JOIN authors a ON p.author_nickname = a.nickname " +
            "WHERE p.created_timestamp > :latestDate " +
            "ORDER BY p.votes_count DESC " +
            "LIMIT 20")
    List<PostThumbWithAuthor> getBestPostsThumbs(long latestDate);

    @Query("SELECT p.id, p.title, p.post_image, p.description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
            "FROM posts p " +
            "JOIN authors a ON p.author_nickname = a.nickname " +
            "WHERE p.id = :postId  ") //AND p.description IS NOT NULL AND p.description != ''
    PostDetailsWithAuthor getPostDetails(int postId);
}