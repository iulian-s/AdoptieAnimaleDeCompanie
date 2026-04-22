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

        //cream predictor
        return imagini.parallelStream().allMatch { file ->
            model.newPredictor().use { predictor ->
                val img = ImageFactory.getInstance().fromInputStream(file.inputStream)
                val result = predictor.predict(img)

                val best = result.best<Classifications.Classification>()

                println(
                    "Imagine ${file.originalFilename}: " +
                            "${best.className} (${String.format("%.2f", best.probability * 100)}%)"
                )

                val isAnimal = isAnimal(result)

                println(" -> Animal detected: $isAnimal")

                isAnimal
            }
        }
    }

    private fun isAnimal(result: Classifications): Boolean {
        val animalKeywords = listOf(
            "Persian_cat", "Siamese_cat", "Egyptian_cat",
            "Chihuahua",
            "Japanese_spaniel",
            "Maltese_dog",
            "Pekinese",
            "Shih-Tzu",
            "Blenheim_spaniel",
            "papillon",
            "toy_terrier",
            "Rhodesian_ridgeback",
            "Afghan_hound",
            "basset",
            "beagle",
            "bloodhound",
            "bluetick",
            "black-and-tan_coonhound",
            "Walker_hound",
            "English_foxhound",
            "redbone",
            "borzoi",
            "Irish_wolfhound",
            "Italian_greyhound",
            "whippet",
            "Ibizan_hound",
            "Norwegian_elkhound",
            "otterhound",
            "Saluki",
            "Scottish_deerhound",
            "Weimaraner",
            "Staffordshire_bullterrier",
            "American_Staffordshire_terrier",
            "Bedlington_terrier",
            "Border_terrier",
            "Kerry_blue_terrier",
            "Irish_terrier",
            "Norfolk_terrier",
            "Norwich_terrier",
            "Yorkshire_terrier",
            "wire-haired_fox_terrier",
            "Lakeland_terrier",
            "Sealyham_terrier",
            "Airedale",
            "cairn",
            "Australian_terrier",
            "Dandie_Dinmont",
            "Boston_bull",
            "miniature_schnauzer",
            "giant_schnauzer",
            "standard_schnauzer",
            "Scotch_terrier",
            "Tibetan_terrier",
            "silky_terrier",
            "soft-coated_wheaten_terrier",
            "West_Highland_white_terrier",
            "Lhasa",
            "flat-coated_retriever",
            "curly-coated_retriever",
            "golden_retriever",
            "Labrador_retriever",
            "Chesapeake_Bay_retriever",
            "German_short-haired_pointer",
            "vizsla",
            "English_setter",
            "Irish_setter",
            "Gordon_setter",
            "Brittany_spaniel",
            "clumber",
            "English_springer",
            "Welsh_springer_spaniel",
            "cocker_spaniel",
            "Sussex_spaniel",
            "Irish_water_spaniel",
            "kuvasz",
            "schipperke",
            "groenendael",
            "malinois",
            "briard",
            "kelpie",
            "komondor",
            "Old_English_sheepdog",
            "Shetland_sheepdog",
            "collie",
            "Border_collie",
            "Bouvier_des_Flandres",
            "Rottweiler",
            "German_shepherd",
            "Doberman",
            "miniature_pinscher",
            "Greater_Swiss_Mountain_dog",
            "Bernese_mountain_dog",
            "Appenzeller",
            "EntleBucher",
            "boxer",
            "bull_mastiff",
            "Tibetan_mastiff",
            "French_bulldog",
            "Great_Dane",
            "Saint_Bernard",
            "Eskimo_dog",
            "malamute",
            "Siberian_husky",
            "dalmatian",
            "affenpinscher",
            "basenji",
            "pug",
            "hamster",

        )
        val threshold = 0.50

        val top = result.topK<Classifications.Classification>(5)

        return top.any { classification ->
            val label = classification.className.lowercase()
            val prob = classification.probability

            val containsKeyword = animalKeywords.any { keyword -> label.contains(keyword) }

            containsKeyword && prob >= threshold
        }
    }
}