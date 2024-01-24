package io.raemian.image.repository

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

    fun upload(
        finalPath: String,
        fileName: String,
        inputStream: InputStream,
    ): String {
        if (isExist(finalPath, fileName)) {
            throw IllegalStateException("이미 동일한 이름의 이미지가 존재합니다.")
        }

        Files.copy(inputStream, File(createPath(finalPath, fileName)).toPath())

        return createUrl(finalPath, fileName)
    }

    fun update(
        finalPath: String,
        newFileName: String,
        oldFileName: String,
        inputStream: InputStream,
    ): String {
        // 파일이 존재하면 삭제
        if (isExist(finalPath, oldFileName)) {
            File(createPath(finalPath, oldFileName)).delete()
        }

        Files.copy(inputStream, File(createPath(finalPath, newFileName)).toPath())

        return createUrl(finalPath, newFileName)
    }

    fun delete(
        finalPath: String,
        fileName: String,
    ) {
        // 파일이 존재하지 않는다면 생략
        if (!isExist(finalPath, fileName)) {
            return
        }

        val file: File = File(createPath(finalPath, fileName))

        file.delete()
    }

    fun isExist(
        finalPath: String,
        fileName: String,
    ): Boolean {
        val file: File = File(createPath(finalPath, fileName))

        if (file.exists()) {
            return true
        }

        return false
    }

    private fun createPath(
        finalPath: String,
        fileName: String,
    ): String =
        "%s$finalPath/$fileName".format(path)

    private fun createUrl(
        finalPath: String,
        fileName: String,
    ): String =
        "%s$finalPath/$fileName".format(url)
}
