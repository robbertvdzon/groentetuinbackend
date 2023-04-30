package receptenadmin

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Bucket
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import com.squareup.gifencoder.FloydSteinbergDitherer
import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

fun main(args: Array<String>) {

    updateImagesInFolder()
    createAnimatedGif()

    /*
    rm video.mp4
    ffmpeg -i tuinvideo.gif -movflags faststart -pix_fmt yuv420p -vf "scale=trunc(iw/2)*2:trunc(ih/2)*2" video.mp4
    cp video.mp4 ~/Desktop

     */


//    "rm video.mp4".runCommand()
//    "ffmpeg -i tuinvideo.gif -movflags faststart -pix_fmt yuv420p -vf \"scale=trunc(iw/2)*2:trunc(ih/2)*2\" video.mp4".runCommand()
//    "cp video.mp4 ~/Desktop".runCommand()


}
//
//fun String.runCommand(
//    workingDir: File = File("."),
//    timeoutAmount: Long = 60,
//    timeoutUnit: TimeUnit = TimeUnit.SECONDS
//): String? = runCatching {
//    ProcessBuilder("\\s".toRegex().split(this))
//        .directory(workingDir)
//        .redirectOutput(ProcessBuilder.Redirect.PIPE)
//        .redirectError(ProcessBuilder.Redirect.PIPE)
//        .start().also { it.waitFor(timeoutAmount, timeoutUnit) }
//        .inputStream.bufferedReader().readText()
//}.onFailure { it.printStackTrace() }.getOrNull()

@Throws(Exception::class)
private fun createAnimatedGif() {

    // The GIF image will be created with file name "my_animated_image.gif"
    FileOutputStream("tuinvideo.gif").use { outputStream ->
        val options = ImageOptions()

        // Set 500ms between each frame
        options.setDelay(100, TimeUnit.MILLISECONDS)

        // Use Floyd Steinberg dithering as it yields the best quality
        options.setDitherer(FloydSteinbergDitherer.INSTANCE)

        // Create GIF encoder with same dimension as of the source images

        val startTime = System.currentTimeMillis()
        val encoder = GifEncoder(outputStream, 1920, 1080, 0)
        val imageDir = File("tuinimages")
        imageDir.list().sorted().forEach {
            if (it.endsWith(".jpg") && !it.equals("last.jpg")) {
                println(it)
                val image = File("tuinimages/$it")
                encoder.addImage(convertImageToArray(image), options)
            }
        }
        encoder.finishEncoding()
        val endTime = System.currentTimeMillis()
        val diff = endTime - startTime
        println("Time to create gif: ${diff / 1000} sec")
    }
}

/**
 * Convert BufferedImage into RGB pixel array
 */
@Throws(IOException::class)
fun convertImageToArray(file: File): Array<IntArray> {
    val bufferedImage = ImageIO.read(file)
    val rgbArray = Array(bufferedImage.height) { IntArray(bufferedImage.width) }
    for (i in 0 until bufferedImage.height) {
        for (j in 0 until bufferedImage.width) {
            rgbArray[i][j] = bufferedImage.getRGB(j, i)
        }
    }
    return rgbArray
}


private fun updateImagesInFolder() {
    val classLoader = Thread.currentThread().contextClassLoader
    val serviceAccount = classLoader.getResourceAsStream("auth.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://groentetuin-f9990.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)

    val bucket: Bucket = StorageClient.getInstance().bucket("groentetuin-f9990.appspot.com")

    val imageDir = File("tuinimages")
    if (!imageDir.exists()) imageDir.mkdirs()

    bucket.list().iterateAll().forEach {
        println(it.name)
        val fileName = "tuinimages/${it.name}"
        if (!File(fileName).exists()) {
            println("Downloading $fileName")
            val of = Path.of(fileName)
            it.downloadTo(of)
        } else {
            println("File $fileName already exists")
        }
    }
}