package com.example.hockeyfileserver.controllers


import com.example.hockeyfileserver.message.ResponseMessage
import com.example.hockeyfileserver.service.FileUploadServiceImpl
import com.example.hockeyfileserver.model.FileUploadInfo
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletRequest
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

//    @GetMapping("/download/files", produces = ["application/zip"])
//    fun getFile(@RequestParam name: List<String>, response: HttpServletResponse) {
//        val zipOut = ZipOutputStream(response.outputStream)
//        for (fileName in name) {
//            FileS
//        }
//        val file = fileUploadService.load(filename)
//        return ResponseEntity.ok().header(
//            HttpHeaders.CONTENT_DISPOSITION,
//            "attachment; filename=\"" + file.filename + "\""
//        ).body(file)
//        zipOut.finish()
//        zipOut.close()
//        response.status = HttpServletResponse.SC_OK
//        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zip)
//    }

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
