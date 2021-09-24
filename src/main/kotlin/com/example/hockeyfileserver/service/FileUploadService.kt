package com.example.hockeyfileserver.service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


interface FileUploadService {
    fun init()
    fun save(file: MultipartFile)
    fun load(filename: String): Resource
    fun deleteAll()
}

@Service
class FileUploadServiceImpl : FileUploadService {
    val root: Path = Paths.get("uploads")

    override fun init() {
        try {
            Files.createDirectory(root);
        } catch (e: IOException) {
            throw RuntimeException("Initialization failed")
        }
    }

    override fun save(file: MultipartFile) {
        try {
            Files.copy(file.inputStream, this.root.resolve(file.originalFilename))
        } catch (e: Exception) {
            throw Exception("Could not Store file. Error: " + e.message)
        }
    }

    override fun load(filename: String): Resource {
        try {
            val file = this.root.resolve(filename)
            val resource = UrlResource(file.toUri())

            if (resource.exists() || resource.isReadable) {
                return resource
            } else {
                throw RuntimeException("Could not read the file!")
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException("Error: " + e.message)
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile())
    }

//    fun loadAll(): Stream<Path>? {
//        return try {
//            Files.walk(this.root, 1).filter { path: Path -> path != this.root }.map { other: Path? ->
//                this.root.relativize(
//                    other
//                )
//            }
//        } catch (e: IOException) {
//            throw RuntimeException("Could not load the files!")
//        }
//    }
}