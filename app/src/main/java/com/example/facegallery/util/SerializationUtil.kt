//package com.example.facegallery.util
//
//import android.renderscript.Type
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.google.gson.JsonElement
//import com.google.gson.JsonPrimitive
//import com.google.gson.JsonSerializationContext
//import com.google.gson.JsonSerializer
//import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
//
//
//object SerializationUtil {
//
//    private val gson: Gson = GsonBuilder()
//        .registerTypeAdapter(FaceDetectorResult::class.java, FaceDetectorResultSerializer())
//        .create()
//
//    fun serializeFaceDetectorResult(faceDetectorResult: FaceDetectorResult): String {
//        return gson.toJson(faceDetectorResult)
//    }
//
//    fun deserializeFaceDetectorResult(jsonString: String): FaceDetectorResult? {
//        return gson.fromJson(jsonString, FaceDetectorResult::class.java)
//    }
//
//    private class FaceDetectorResultSerializer : JsonSerializer<FaceDetectorResult> {
//        override fun serialize(
//            src: FaceDetectorResult?,
//            typeOfSrc: Type?,
//            context: JsonSerializationContext?
//        ): JsonElement {
//            if (src == null) {
//                return JsonPrimitive("") // Or handle null as needed
//            }
//
//            // Assuming 'toByteString()'  gives you the underlying proto data
//            val protoByteString = src.pr
//            val jsonString = protoByteString.toStringUtf8() // Or Base64 encoding if needed
//            return JsonPrimitive(jsonString)
//        }
//    }
//}