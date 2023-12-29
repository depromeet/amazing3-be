package io.raemian.image.repository

import io.raemian.image.enums.FileExtensionType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File
import java.io.InputStream
import java.nio.file.Files

@Repository
class ImageRepository {

    @Value("\${server.image.url}")
    private lateinit var url: String

    @Value("\${server.image.file-path}")
    private lateinit var path: String

    fun upload (
        fileName: String,
        inputStream: InputStream
    ): String {
        if(isExist(fileName)) {
            throw IllegalStateException("이미 동일한 이름의 이미지가 존재합니다.")
        }

        Files.copy(inputStream, File(createPath(fileName)).toPath())

        return createUrl(fileName)
    }

    fun update (
        newFileName: String,
        oldFileName: String,
        inputStream: InputStream
    ): String {
        // 파일이 존재하면 삭제
        if(isExist(oldFileName)) {
            File(createPath(oldFileName)).delete()
        }

        Files.copy(inputStream, File(createPath(newFileName)).toPath())

        return createUrl(newFileName)
    }

    fun delete (
        fileName: String
    ) {
        // 파일이 존재하지 않는다면 생략
        if(!isExist(fileName)) {
            return
        }

        val file: File = File(createPath(fileName))

        file.delete()
    }

    fun isExist (
        fileName: String
    ): Boolean {
        val file: File = File(createPath(fileName))

        if(file.exists()) {
            return true
        }

        return false;
    }

    private fun createPath(fileName: String): String =
        "%s/${fileName}".format(path)

    private fun createUrl(fileName: String): String =
        "%s/${fileName}".format(url)
}