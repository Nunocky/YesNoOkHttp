package org.nunocky.yesnookhttp

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import se.ansman.kotshi.KotshiJsonAdapterFactory
import java.io.IOException


@KotshiJsonAdapterFactory
abstract class ApplicationJsonAdapterFactory : JsonAdapter.Factory {
    companion object {
        val INSTANCE: ApplicationJsonAdapterFactory = KotshiApplicationJsonAdapterFactory
    }
}

class YesNoService {

    fun getYesNo(): YesNo {
        val client = OkHttpClient()

        val request = Request.Builder().url("https://yesno.wtf/api").build()
        val call = client.newCall(request)
        val response = call.execute()
        val body = response.body() ?: throw IOException("invalid response")

        // body string -> json
        val jsonData = body.string()

//        val jObject = JSONObject(jsonData)
//        val answer = jObject.getString("answer")
//        val forced = (jObject.getString("forced") == "true")
//        val image = jObject.getString("image")
//        return YesNo(answer, forced, image)

//        val moshi = Moshi.Builder().build()
//        val jsonAdapter: JsonAdapter<YesNo> = moshi.adapter(YesNo::class.java)
//        val yesNo = jsonAdapter.fromJson(jsonData)
//        return yesNo ?: throw IOException("JSON decode failed")

        val moshi = Moshi.Builder()
            .add(ApplicationJsonAdapterFactory.INSTANCE)
            .build()
        val jsonAdapter: JsonAdapter<YesNo> = moshi.adapter(YesNo::class.java)
        val yesNo = jsonAdapter.fromJson(jsonData)
        return yesNo ?: throw IOException("JSON decode failed")
    }

    fun getImageByteArray(url: String): ByteArray {
        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()
        val call = client.newCall(request)
        val response = call.execute()
        val body = response.body() ?: throw Exception("invalid response")

        val stream = body.byteStream()
        return stream.readBytes()
    }
}