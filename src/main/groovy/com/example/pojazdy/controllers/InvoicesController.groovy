package com.example.pojazdy.controllers

import com.example.pojazdy.model.Invoice
import com.example.pojazdy.model.annotations.HasPartnerRole
import com.example.pojazdy.service.InvoiceService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
@RequestMapping("/invoices")
class InvoicesController {

    private final InvoiceService invoiceService

    @Autowired
    InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService
    }

    @HasPartnerRole
    @PostMapping
    void uploadDocument(@RequestParam(name = "file") MultipartFile file,
                        @RequestParam(name = "description") String description,
                        @RequestParam(name = "driverUUID") String driverUUID) {

        Invoice invoice = new Invoice()
        invoice.driverUUID = driverUUID
        invoice.filename = file.getOriginalFilename()
        invoice.description = description
        invoice.systemEntryDate = new Timestamp(System.currentTimeMillis())

        invoiceService.uploadInvoice(file, invoice)
    }

    @HasPartnerRole
    @GetMapping("/{uuid}/download")
    void getInvoice(@PathVariable("uuid") String uuid, HttpServletResponse response) {
        def invoice = invoiceService.findInvoice(uuid)
        log.info("File: {} File2: {} invoiceUUID: {}", invoice.content, invoice.getPath(), uuid)
        def contentType = URLConnection.guessContentTypeFromName(invoice.filename)
        response.setContentType(contentType)
        response.addHeader("filename", invoice.filename)
        IOUtils.copy(invoice.content.toFile().newInputStream(), response.outputStream)
    }
}
