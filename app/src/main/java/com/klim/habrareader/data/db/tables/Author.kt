package com.klim.habrareader.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
class Author(
    @PrimaryKey
    var nickname: String,

    @ColumnInfo(name = "author_icon")
    var authorIcon: String?,
) {

}