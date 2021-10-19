package com.example.hockeyfileserver.model

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "files")
data class FileDB (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,
    val name: String,
    val type: String,
    val data: ByteArray
) {

    fun toModel(): File {
        return File(id, name, type, data)
    }
}