package com.example.hockeyfileserver.controllers


import com.example.hockeyfileserver.message.ResponseMessage
import com.example.hockeyfileserver.service.FileUploadServiceImpl
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletResponse

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
class FileUploadController(val fileUploadService: FileUploadServiceImpl) {

    @PostMapping("/file/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        val fileURL = fileUploadService.save(file)
        return ResponseEntity.ok(fileURL)
    }

    @PostMapping("/file/multi-upload")
    fun uploadFiles(@RequestParam("files") files: Array<MultipartFile>): ResponseEntity<List<ResponseMessage>> {
        val fileURLs = fileUploadService.save(files)
        return ResponseEntity.ok(fileURLs)
    }

    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    fun getFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileUploadService.load(filename)
        return ResponseEntity.ok().header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.filename + "\""
        ).body(file)
    }

    @Throws(IOException::class)
    @GetMapping("/download/files", produces = ["application/zip"])
    fun getCompressedFile(response: HttpServletResponse) {
        response.contentType = "application/octet-stream";
        response.setHeader("Content-Disposition", "attachment;filename=download.zip");
        response.status = HttpServletResponse.SC_OK;

        val fileNames: List<String> = fileUploadService.loadAll("uploads")


        val zipOut = ZipOutputStream(response.outputStream)
        for (file in fileNames) {
            val resource = FileSystemResource(file)
            val zipEntry = ZipEntry(resource.filename)

            zipEntry.size = resource.contentLength()
            zipEntry.time = System.currentTimeMillis()

            zipOut.putNextEntry(zipEntry)

            StreamUtils.copy(resource.inputStream, zipOut)
            zipOut.closeEntry()
        }
        zipOut.finish()
        zipOut.close()
    }

//    @GetMapping("/files")
//    fun getListFiles(): ResponseEntity<List<FileUploadInfo>> {
//        val FileUploadInfos: List<FileUploadInfo> = fileUploadService.loadAll().map { path ->
//            val filename: String = path.getFileName().toString()
//            val url = MvcUriComponentsBuilder.fromMethodName(
//                FileUploadController::class, "getFile",
//                path.getFileName().toString()
//            ).build().toString()
//            FileUploadInfo(filename, url)
//        }.collect(Collectors.toList())
//        return ResponseEntity.status(HttpStatus.OK).body<List<FileUploadInfo>>(FileUploadInfos)
//    }
}
