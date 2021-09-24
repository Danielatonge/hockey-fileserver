package com.example.hockeyfileserver.service

import com.example.hockeyfileserver.message.ResponseMessage
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.collections.ArrayList


interface FileUploadService {
    fun init()
    fun save(file: MultipartFile): ResponseMessage
    fun save(files: Array<MultipartFile>): List<ResponseMessage>
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

    override fun save(file: MultipartFile): ResponseMessage {
        try {
            Files.copy(file.inputStream, this.root.resolve(file.originalFilename),StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            throw Exception("Could not Store file. Error: " + e.message)
        }
        val fileURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/").path(file.originalFilename!!).toUriString()
        return ResponseMessage(fileURL)
    }

    override fun save(files: Array<MultipartFile>): List<ResponseMessage> {
        val fileDownloadUrls: MutableList<ResponseMessage> = ArrayList()
        try {
            files.forEach { file -> fileDownloadUrls.add(save(file)) }
        } catch (e: Exception) {
            throw Exception("Could not Store file. Error: " + e.message)
        }
        return fileDownloadUrls
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