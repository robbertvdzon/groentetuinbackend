package receptenadmin

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val classLoader = Thread.currentThread().contextClassLoader
    val serviceAccount = classLoader.getResourceAsStream("auth.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://groentetuin-f9990.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)

    val bucket: Bucket = StorageClient.getInstance().bucket("groentetuin-f9990.appspot.com")

    bucket.list().iterateAll().forEach {
        println(it.name)
        val of = Path.of("/tmp/${it.name}")
        it.downloadTo(of)

    }

    val res = bucket.get("last.jpg")
    val of = Path.of("/tmp/last.jpg")
    res.downloadTo(of)
    println(res)

}