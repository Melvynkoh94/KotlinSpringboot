package com.assessment.kotlinspringboot.service

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface FileStorage {
    fun store(file: MultipartFile)
    fun loadFile(): Resource
    fun deleteAll()
    fun init()
    fun loadFiles(): Stream<Path>
}