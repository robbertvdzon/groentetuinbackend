package receptenadmin

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val file = File("/tmp/last.jpg")
    val inputStream = FileInputStream(file)
    val outputStream = ByteArrayOutputStream()

    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } != -1) {
        outputStream.write(buffer, 0, length)
    }
    val imageBytes = outputStream.toByteArray()

    inputStream.close()
    outputStream.close()

    val currentDate = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
    val formattedDate = currentDate.format(formatter)
    val filename = "tuin-$formattedDate.jpg"

    val classLoader = Thread.currentThread().contextClassLoader
    val serviceAccount = classLoader.getResourceAsStream("auth.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://groentetuin-f9990.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)

    val bucket: Bucket = StorageClient.getInstance().bucket("groentetuin-f9990.appspot.com")
    val res = bucket.create(filename, imageBytes)
    bucket.create("last.jpg", imageBytes)
    println(res)

}