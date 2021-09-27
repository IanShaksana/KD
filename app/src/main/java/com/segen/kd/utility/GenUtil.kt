package com.segen.kd.utility

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.segen.kd.modelbody.ModelRetCross
import com.google.gson.Gson
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class GentUtil {

    // cross platform cryptography
    private val key: String = "OhLordYouLoveMe"
    private val CipherName: String = "AES/CBC/PKCS5PADDING"
    private val CipherKeyLen: Int = 16

    fun generateIv(): IvParameterSpec {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    fun encrypt(data: String): String {
        try {
            var key = this.key
            if (key.length < CipherKeyLen) {
                val numPad = CipherKeyLen - key.length
                for (i in 0 until numPad) {
                    key += "0" // 0 pad to len 16 bytes
                }
            } else if (key.length > CipherKeyLen) {
                key = key.substring(0, CipherKeyLen) // truncate to 16 bytes
            }

            val initVector: IvParameterSpec = generateIv()
            val skeySpec = SecretKeySpec(key.toByteArray(charset("ISO-8859-1")), "AES")
            val cipher: Cipher = Cipher.getInstance(CipherName)
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector)
            val encryptedData: ByteArray = cipher.doFinal(data.toByteArray())
            val base64_EncryptedData: String =
                android.util.Base64.encodeToString(encryptedData, android.util.Base64.NO_WRAP)
            val base64_IV: String =
                android.util.Base64.encodeToString(initVector.iv, android.util.Base64.NO_WRAP)

            return "$base64_EncryptedData:$base64_IV"
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }

    fun decrypt(data: String): String {
        try {
            var key = this.key
            if (key.length < CipherKeyLen) {
                val numPad = CipherKeyLen - key.length
                for (i in 0 until numPad) {
                    key += "0" // 0 pad to len 16 bytes
                }
            } else if (key.length > CipherKeyLen) {
                key = key.substring(0, CipherKeyLen) // truncate to 16 bytes
            }
            val parts = data.split(":").toTypedArray()
            val iv =
                IvParameterSpec(android.util.Base64.decode(parts[1], android.util.Base64.NO_WRAP))

            val skeySpec = SecretKeySpec(key.toByteArray(charset("ISO-8859-1")), "AES")
            val cipher: Cipher = Cipher.getInstance(CipherName)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val decodedEncryptedData: ByteArray =
                android.util.Base64.decode(parts[0], android.util.Base64.NO_WRAP);

            val original: ByteArray = cipher.doFinal(decodedEncryptedData)
            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        }
    }

    fun resultEncryptCross(data: Any): JSONObject? {
        return try {
            val encryptedText: String = encrypt(Gson().toJson(data))
            val modelSend = ModelRetCross(encryptedText)
            JSONObject(Gson().toJson(modelSend))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun resultDecryptCross(resp: JSONObject): String? {
        return try {
            val data = resp.getJSONArray("data").get(0)
            Log.i("Jam Result", Date().toString())
            Log.i("Result", resp.toString())
            Log.i("Result", data.toString())
            val decrypted = decrypt(data.toString())
            Log.i("Result", decrypted)
            decrypted
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // cross platform cryptography


    // http req
    fun httpReq(url: String, jObject: JSONObject, context: Context): JSONObject {
        val future = RequestFuture.newFuture<JSONObject>()
        val queue = Volley.newRequestQueue(context)
        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, jObject, future, future
        )
        queue.add(stringRequest)
        Timber.i("Being Send  : " + jObject)

        var resp = JSONObject()
        try {
            resp = future.get(15, TimeUnit.SECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resp
    }

    fun process(url: String, data: Any, context: Context): JSONObject {
        return httpReq(url, resultEncryptCross(data)!!, context)
    }
    // http req

}