package com.klim.habrareader.data.db.dao

import androidx.room.*
import com.klim.habrareader.data.db.tables.Post
import com.klim.habrareader.data.repositories.post.dataSources.local.models.PostThumbWithAuthor
import com.klim.habrareader.data.repositories.post.dataSources.local.models.PostDetailsWithAuthor

@Dao
interface PostDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(post: Post?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg posts: Post?)

    @Delete
    fun delete(post: Post?)

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPost(id: Int): Post?

    @Query("SELECT count(*) > 0 FROM posts WHERE id = :id")
    fun postExist(id: Int): Boolean

    @Query("SELECT short_description FROM posts WHERE id = :id")
    fun getPostShortDescription(id: Int): String?

    @Query("SELECT post_image FROM posts WHERE id = :id")
    fun getPostImage(id: Int): String?

    @Query("SELECT description FROM posts WHERE id = :id")
    fun getPostDescription(id: Int): String?

    @get:Query("SELECT * FROM posts ORDER BY created_timestamp DESC")
    val allPosts: List<Post?>?

    @Query(
        "SELECT p.id, p.title, p.post_image, p.short_description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
                "FROM posts p " +
                "JOIN authors a ON p.author_nickname = a.nickname " +
                "WHERE p.votes_count >= :votesCount " +
                "ORDER BY p.created_timestamp DESC " +
                "LIMIT 20"
    )
    fun getAllPostsThumbs(votesCount: Int): List<PostThumbWithAuthor?>?

    @Query(
        "SELECT p.id, p.title, p.post_image, p.short_description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
                "FROM posts p " +
                "JOIN authors a ON p.author_nickname = a.nickname " +
                "WHERE p.created_timestamp > :latestDate " +
                "ORDER BY p.votes_count DESC " +
                "LIMIT 20"
    )
    fun getBestPostsThumbs(latestDate: Long): List<PostThumbWithAuthor?>?

    @Query(
        "SELECT p.id, p.title, p.post_image, p.description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
                "FROM posts p " +
                "JOIN authors a ON p.author_nickname = a.nickname " +
                "WHERE p.id = :postId  "
    )
    //AND p.description IS NOT NULL AND p.description != ''
    fun getPostDetails(postId: Int): PostDetailsWithAuthor?

    @Query(
        "SELECT p.id, p.title, p.post_image, p.description, p.created_timestamp, p.votes_count, p.bookmarks_count, p.views_count, p.comments_count, a.nickname, a.author_icon " +
                "FROM posts p " +
                "JOIN authors a ON p.author_nickname = a.nickname " +
                "WHERE p.id = :postId  "
    )
    suspend fun getPostDetails_S(postId: Int): PostDetailsWithAuthor?
}