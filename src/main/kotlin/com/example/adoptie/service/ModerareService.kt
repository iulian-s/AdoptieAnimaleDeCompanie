package com.example.adoptie.service

import ai.djl.modality.Classifications
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.transform.Resize
import ai.djl.modality.cv.transform.ToTensor
import ai.djl.modality.cv.translator.ImageClassificationTranslator
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ZooModel
import ai.djl.training.util.ProgressBar
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ModerareService {
    @Value("\${model.ai.path}")
    private lateinit var modelPathStr: String
    private lateinit var model: ZooModel<Image, Classifications>

    @PostConstruct
    fun init() {
        val synsets = listOf("normal", "nsfw")
        val path = Paths.get(modelPathStr)

        if (!Files.exists(path)) {
            throw IllegalStateException("Modelul AI nu a fost găsit la calea: ${path.toAbsolutePath()}")
        }

        val criteria = Criteria.builder()
            .setTypes(Image::class.java, Classifications::class.java)
            .optEngine("OnnxRuntime")
            .optModelPath(path)
            .optTranslator(
                ImageClassificationTranslator.builder()
                    .addTransform(Resize(224, 224)) // Modelul Falconsai folosește 224x224
                    .addTransform(ToTensor())
                    .optApplySoftmax(true)
                    .optSynset(synsets)
                    .build()
            )
            .optProgress(ProgressBar())
            .build()
        model = criteria.loadModel()
    }

    fun suntToateImaginileSafe(imagini: List<MultipartFile>?): Boolean {
        if (imagini.isNullOrEmpty()) return true

        //cream predictor
        return imagini.parallelStream().allMatch{ file ->
            model.newPredictor().use { predictor ->
                val img = ImageFactory.getInstance().fromInputStream(file.inputStream)
                val result = predictor.predict(img)
                val classification =
                    result.get("normal") as? Classifications.Classification
                val normalScore = classification?.probability ?: 0.0
                println("Imagine ${file.originalFilename}: Safe score = ${"%.2f".format(normalScore * 100)}%")
                normalScore > 0.8
            }
         }
    }
}