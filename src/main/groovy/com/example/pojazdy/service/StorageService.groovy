package com.example.pojazdy.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Slf4j
@CompileStatic
@Service
class StorageService {

    private final LoginService loginService

    private final Path fileStoragePath

    private final String fileStorageLocation

    @Autowired
    StorageService(@Value("\${file.storage.location}") String fileStorageLocation, LoginService loginService)
    {
        this.loginService = loginService
        this.fileStorageLocation = fileStorageLocation
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize()

        try {
            Files.createDirectories(fileStoragePath)
        }
        catch (Exception e) {
            throw new IOException()
        }

    }

    String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename())

        def partnerUUID = loginService.loginPartnerUUID()

        Path partnerPath = Paths.get(fileStoragePath as String, partnerUUID)
        Path filePath = Paths.get(partnerPath as String, fileName)
        try {
            Files.createDirectories(partnerPath)
        }
        catch (Exception e) {
            throw new IOException()
        }

        log.info("{} {}", filePath, fileStorageLocation)
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING)
        }
        catch (IOException e) {
            throw new RuntimeException()
        }

        fileName
    }

}
