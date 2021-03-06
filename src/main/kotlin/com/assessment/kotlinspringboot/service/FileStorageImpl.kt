package com.assessment.kotlinspringboot.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Service
@PropertySource("classpath:application.properties")
class FileStorageImpl: FileStorage {

    val logger = LoggerFactory.getLogger(this::class.java)
    val rootLocation: Path = Paths.get("uploadedDatafile")
    @Value("\${retail.dataFilename}") val filename: String = ""

    override fun store(file: MultipartFile){
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()))
    }

    override fun loadFile(): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if(resource.exists() || resource.isReadable()) {
            return resource
        }else{
            throw RuntimeException("FAIL!")
        }
    }

    override fun deleteAll(){
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init(){
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1)
                .filter{path -> !path.equals(this.rootLocation)}
                .map(this.rootLocation::relativize)
    }
}