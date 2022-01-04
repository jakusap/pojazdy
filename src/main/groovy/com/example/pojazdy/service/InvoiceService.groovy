package com.example.pojazdy.service

import com.example.pojazdy.model.Invoice
import com.example.pojazdy.model.documents.Document
import com.example.pojazdy.repository.DriversRepository
import com.example.pojazdy.repository.InvoiceRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
@Service
@CompileStatic
class InvoiceService {

    private final InvoiceRepository invoiceRepository
    private final DriversRepository driversRepository
    private final LoginService loginService
    private final StorageService storageService

    @Autowired
    InvoiceService(InvoiceRepository invoiceRepository, DriversRepository driversRepository, LoginService loginService, StorageService storageService) {
        this.invoiceRepository = invoiceRepository
        this.storageService = storageService
        this.loginService = loginService
        this.driversRepository = driversRepository
    }

    List<Invoice> getPartnerInvoices() {
        def partnerUUID = loginService.loginPartnerUUID()
        invoiceRepository.findPartnerInvoices(partnerUUID)
    }

    void uploadInvoice(MultipartFile file, Invoice invoice) {
        def partnerUUID = loginService.loginPartnerUUID()
        def driver = driversRepository.findDriverByDriverUUID(partnerUUID, invoice.driverUUID)

        invoice.partnerUUID = partnerUUID
        invoice.driverFirstName = driver.firstName
        invoice.driverLastName = driver.lastName

        def invoiceUUID = invoiceRepository.insert(invoice)
        storageService.storeInvoice(file, invoiceUUID)
    }

    Invoice findInvoice(String invoiceUUID) {
        def partnerUUID = loginService.loginPartnerUUID()
        def invoice = invoiceRepository.findInvoiceByUUID(partnerUUID, invoiceUUID)
        invoice.content = storageService.getDocumentFilePath(invoice.getPath())
        invoice
    }
}
