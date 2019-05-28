package org.nunocky.yesnookhttp

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class YesNoService {

    fun getYesNo(): YesNo {
        val client = OkHttpClient()

        val request = Request.Builder().url("https://yesno.wtf/api").build()
        val call = client.newCall(request)
        val response = call.execute()
        val body = response.body() ?: throw Exception("invalid response")

        // body string -> json
        val jsonData = body.string()
        val jObject = JSONObject(jsonData)

        val answer = jObject.getString("answer")
        val forced = (jObject.getString("forced") == "true")
        val image = jObject.getString("image")

        return YesNo(answer, forced, image)
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