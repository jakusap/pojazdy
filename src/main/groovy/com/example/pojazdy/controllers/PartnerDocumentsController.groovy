package com.example.pojazdy.controllers


import groovy.transform.CompileStatic
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
//import pl.codecape.taxi.exceptions.NotFoundException
//import pl.codecape.taxi.model.annotations.HasDriverRole
//import pl.codecape.taxi.model.annotations.HasPartnerOrDriverRole
//import pl.codecape.taxi.model.documents.FillDocumentRequest
//import pl.codecape.taxi.model.documents.PartnerDocument
//import pl.codecape.taxi.model.documents.SignedContract
//import pl.codecape.taxi.service.SignedContractsService

import javax.servlet.http.HttpServletResponse
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 *
 * @author Jakub Sapiński
 */
@CompileStatic
@RestController
@RequestMapping("/partner-documents")
class PartnerDocumentsController {

}

