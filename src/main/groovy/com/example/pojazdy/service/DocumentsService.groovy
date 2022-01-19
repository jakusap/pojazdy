package com.example.pojazdy.service


import com.example.pojazdy.model.documents.Document
import com.example.pojazdy.repository.DocumentsRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import com.example.pojazdy.model.eventsFiles.EventFile

/**
 *
 * @author Jakub Sapiński
 */
@Slf4j
@Service
@CompileStatic
class DocumentsService {

    private final DocumentsRepository documentsRepository
    private final LoginService loginService
    private final StorageService storageService

    @Autowired
    DocumentsService(DocumentsRepository documentsRepository, LoginService loginService, StorageService storageService) {
        this.documentsRepository = documentsRepository
        this.loginService = loginService
        this.storageService = storageService
    }

    List<Document> getPartnerDocuments(){
        def partnerUUID = loginService.loginPartnerUUID()
        documentsRepository.findPartnerDocuments(partnerUUID)
    }

    void uploadDocument(MultipartFile file, Document document, int eventId) {
        def partnerUUID = loginService.loginPartnerUUID()
        document.partnerUUID = partnerUUID
        documentsRepository.insertDocumentFileType(document.partnerUUID, document.typeCode)
        documentsRepository.insert(document)
        def documentId = documentsRepository.getDocumentId(document)
        storageService.storeDocument(file, documentId)
        def eventFile = new EventFile()
        eventFile.eventId = eventId
        eventFile.fileId = documentId as int
        documentsRepository.registerDocumentForEvent(eventFile)
    }

    Document findDocument(int documentId) {
        def partnerUUID = loginService.loginPartnerUUID()
        def document = documentsRepository.findDocumentById(partnerUUID, documentId)
        document.content = storageService.getDocumentFilePath(document.getPath())
        document
    }
}
