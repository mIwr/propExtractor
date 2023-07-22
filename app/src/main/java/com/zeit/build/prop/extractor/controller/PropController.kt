package com.zeit.build.prop.extractor.controller

import com.zeit.build.prop.extractor.model.PropKey
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class PropController constructor(private val _props: HashMap<PropKey, String>) {

    val props: HashMap<PropKey, String>
        get() {
            return HashMap(_props)
        }

    companion object {
        fun extractFromDevice(): PropController {
            val props = HashMap<PropKey, String>()
            try {
                val map = extractFromSDK()
                for (item in map) {
                    props[item.key] = item.value
                }
            } catch (e: Exception) {
                print(e)
            }
            val map = extractFromGetProp()
            for (item in map) {
                props[item.key] = item.value
            }
            return PropController(map)
        }

        private fun extractFromGetProp(): HashMap<PropKey, String> {
            val p: Process
            val result = HashMap<PropKey, String>()
            try {
                p = ProcessBuilder("/system/bin/getprop").redirectErrorStream(true).start()
                val readStream = BufferedReader(InputStreamReader(p.inputStream))
                var line = ""
                while (readStream.readLine().also { if (it != null) line = it } != null) {
                    val rawPair: List<String> = line.split(':')
                    if (rawPair.size < 2) {
                        continue
                    }
                    var buff = rawPair[0]
                    val key = buff.substring(1, buff.length - 1)
                    val propKey = PropKey.fromRawKey(key) ?: continue
                    buff = rawPair[1]
                    val pairVal = buff.substring(2, buff.length - 1)
                    result[propKey] = pairVal
                }
                p.destroy()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }

        private fun extractFromSDK(): HashMap<PropKey, String> {
            val properties = System.getProperties()
            val result = HashMap<PropKey, String>()
            if (properties.isEmpty) {
                return result
            }
            var propVal: String?
            for (prop in properties.stringPropertyNames()) {
                if (prop.isNullOrEmpty()) {
                    continue
                }
                val propKey = PropKey.fromRawKey(prop) ?: continue
                propVal = properties.getProperty(prop)
                if (propVal.isNullOrEmpty()) {
                    continue
                }
                result[propKey] = propVal
            }
            return result
        }
    }
}