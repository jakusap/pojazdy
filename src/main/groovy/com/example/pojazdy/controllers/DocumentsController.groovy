package com.example.pojazdy.controllers

import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.model.documents.Document
import com.example.pojazdy.service.DocumentsService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletResponse
import java.sql.Timestamp

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
@CompileStatic
@RestController
@RequestMapping("/documents")
class DocumentsController {

    private final DocumentsService documentsService

    @Autowired
    DocumentsController(DocumentsService documentsService) {
        this.documentsService = documentsService
    }

    @HasPartnerRole
    @PostMapping
    void uploadDocument(@RequestParam(name = "file") MultipartFile file,
                        @RequestParam(name = "typeCode") String typeCode,
                        @RequestParam(name = "description") String description,
                        @RequestParam(name = "driverUUID") String driverUUID) {

        Document document = new Document()
        document.typeCode = typeCode
        document.driverUUID = driverUUID
        document.filename = file.getOriginalFilename()
        document.description = description
        document.systemEntryDate = new Timestamp(System.currentTimeMillis())
        documentsService.uploadDocument(file, document)
    }

    @HasPartnerRole
    @GetMapping("/{documentId}/download")
    void getDocument(@PathVariable("documentId") int documentId, HttpServletResponse response) {
        def document = documentsService.findDocument(documentId)
        log.info("File: {} File2: {} documentId: {}", document.content, document.getPath(), documentId)
        def contentType = URLConnection.guessContentTypeFromName(document.filename)
        response.setContentType(contentType)
        response.addHeader("filename", document.filename)
        IOUtils.copy(document.content.toFile().newInputStream(), response.outputStream)
    }

}

