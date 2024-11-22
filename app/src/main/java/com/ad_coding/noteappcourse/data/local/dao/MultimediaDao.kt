package com.ad_coding.noteappcourse.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query

import com.ad_coding.noteappcourse.data.local.entity.MultimediaEntity
@Dao
interface MultimediaDao {

    @Insert
    suspend fun insert(multimedia: MultimediaEntity)

    @Delete
    suspend fun delete(multimedia: MultimediaEntity)

    @Query("SELECT * FROM multimedia WHERE idNota = :idNota")
    suspend fun getMultimediaForNota(idNota: Int): List<MultimediaEntity>

    @Query("DELETE FROM multimedia WHERE idNota = :idNota")
    suspend fun deleteAllForNota(idNota: Int)
}
