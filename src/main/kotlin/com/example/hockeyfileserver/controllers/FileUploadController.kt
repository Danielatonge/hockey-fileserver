package com.example.hockeyfileserver.controllers


import com.example.hockeyfileserver.service.FileUploadServiceImpl
import com.example.hockeyfileserver.model.FileUploadInfo
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import java.util.stream.Collectors

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
class FileUploadController(val fileUploadService: FileUploadServiceImpl) {

    @PostMapping("/file/upload")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
    ) {
        fileUploadService.save(file)
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
