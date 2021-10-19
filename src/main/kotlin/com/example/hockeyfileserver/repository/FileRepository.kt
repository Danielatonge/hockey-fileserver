package com.example.hockeyfileserver.repository

import com.example.hockeyfileserver.model.FileDB
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface FileRepository : CrudRepository<FileDB, String> {
    @Query("select * from files")
    fun findFiles(): List<FileDB>
}