package com.example.adoptie.service

import ai.djl.modality.Classifications
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.transform.Normalize
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
    @Value("\${model.labels.path}")
    private lateinit var labelsPathStr: String
    private lateinit var model: ZooModel<Image, Classifications>
    private lateinit var synsets: List<String>

    @PostConstruct
    fun init() {
        val modelPath = Paths.get(modelPathStr)
        val labelsPath = Paths.get(labelsPathStr)

        if (!Files.exists(modelPath)) {
            throw IllegalStateException("Model not found: ${modelPath.toAbsolutePath()}")
        }

        if (!Files.exists(labelsPath)) {
            throw IllegalStateException("Labels not found: ${labelsPath.toAbsolutePath()}")
        }
        synsets = Files.readAllLines(labelsPath)


        val criteria = Criteria.builder()
            .setTypes(Image::class.java, Classifications::class.java)
            .optEngine("OnnxRuntime")
            .optModelPath(modelPath)
            .optTranslator(
                ImageClassificationTranslator.builder()
                    .addTransform(Resize(224, 224))
                    .addTransform(ToTensor())
                    .addTransform(
                        Normalize(
                            floatArrayOf(0.485f, 0.456f, 0.406f),
                            floatArrayOf(0.229f, 0.224f, 0.225f)
                        )
                    )
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

        return imagini.parallelStream().allMatch { file ->
            model.newPredictor().use { predictor ->
                val img = ImageFactory.getInstance().fromInputStream(file.inputStream)
                val result = predictor.predict(img)

                val top3 = result.topK<Classifications.Classification>(3)

                top3.any { classification ->
                    val label = classification.className.lowercase()
                    val score = classification.probability

                    val index = label.split(",")[0].trim().toIntOrNull() ?: -1

                    val isAnimalIndex = when (index) {
                        in 0..397 -> true
                        in 398..500 -> false
                        else -> false
                    }

                    if (isAnimalIndex && score > 0.35) {
                        println("Animal detectat: $label (${String.format("%.2f", score * 100)}%)")
                        return@any true
                    }
                    false
                }
            }
        }
    }
}