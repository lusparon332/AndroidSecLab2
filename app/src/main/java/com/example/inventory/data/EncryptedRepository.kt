package com.example.inventory.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.inventory.applicationContext
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.charset.StandardCharsets

class EncryptedRepository(val selectedUri: Uri) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    fun WriteFile(item: Item){
        val file: File = File(applicationContext!!.getExternalFilesDir(null), "t")
        val encryptedFile = EncryptedFile.Builder(
            file,
            applicationContext!!,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        val jsonString = Json.encodeToString(Item.serializer(), item)
        encryptedFile.openFileOutput().use {
            it.write(jsonString.toByteArray(StandardCharsets.UTF_8))
            it.flush()
        }
        Log.d("txt content", file.readText())
        file.inputStream().use{input ->
            selectedUri.let { applicationContext!!.contentResolver.openOutputStream(it) }.use{output ->
                val t = input?.copyTo(output!!)
                Log.d("long", "$t")
            }
        }
        file.delete()
    }
    fun ReadFile(): Item?{
        try{
            val file: File = File(applicationContext!!.getExternalFilesDir(null), "t")
            val encryptedFile = EncryptedFile.Builder(
                file,
                applicationContext!!,
                masterKeyAlias,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            file.outputStream().use{output ->
                selectedUri.let { applicationContext!!.contentResolver.openInputStream(it) }.use{input ->
                    val t = input?.copyTo(output)
                    Log.d("long", "$t")
                }
            }
            val fileContent = ByteArray(32000)
            val numBytesRead: Int
            encryptedFile.openFileInput().use {
                numBytesRead = it.read(fileContent)
            }
            file.delete()
            val jsonString = String(fileContent, 0, numBytesRead)
            val itemObject = Json.decodeFromString(Item.serializer(), jsonString)
            itemObject.creation = "file"
            itemObject.id = 0
            return itemObject
        } catch (exception: Exception){
            Toast.makeText(
                applicationContext,
                "Ошибка чтения файла",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
    }
}