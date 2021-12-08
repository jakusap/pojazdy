package com.example.pojazdy.controllers

import com.example.pojazdy.model.annotations.HasPartnerRole

import com.example.pojazdy.service.StorageService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


//import pl.codecape.taxi.model.FileImportLog
//import pl.codecape.taxi.model.ImportFilesTask
//import pl.codecape.taxi.model.ImportedFile
//import pl.codecape.taxi.model.ImportedFileType
//import pl.codecape.taxi.model.Provider
//import pl.codecape.taxi.model.annotations.HasPartnerRole
//import pl.codecape.taxi.service.ImportsService
//import pl.codecape.taxi.service.parsers.CsvFileImporter
//import pl.codecape.taxi.utils.InputStreamUtils

/**
 *
 * @author Michał Szturc
 */
@Slf4j
@RestController
@CompileStatic
@RequestMapping("/files")
class FilesController {

    private final StorageService storageService

    @Autowired
    FilesController(StorageService storageService){
        this.storageService = storageService
    }

    @HasPartnerRole
    @PostMapping
    ResponseEntity uploadFile( @RequestParam(name = "file") MultipartFile file){
        String filename = file.getOriginalFilename()
        def okej = storageService.storeFile(file)
        ResponseEntity.ok("File uploaded")
    }
}
