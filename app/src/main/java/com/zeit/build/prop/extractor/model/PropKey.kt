package com.zeit.build.prop.extractor.model

class PropKey constructor(private val prefix: String? = null, private val group: String, private val subgroup: String? = null, private val name: String, private val type: String? = null) {

    val key: String
        get() {
            var key = group
            if (!prefix.isNullOrEmpty()) {
                key = "$prefix.$key"
            }
            if (!subgroup.isNullOrEmpty()) {
                key += ".$subgroup"
            }
            key += ".$name"
            if (!type.isNullOrEmpty()) {
                key += ".$type"
            }
            return key
        }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PropKey) {
            return false
        }
        val otherProp: PropKey = other
        return otherProp.key == key
    }

    override fun toString(): String {
        return key
    }

    companion object {
        fun fromRawKey(key: String): PropKey? {
            val split = key.split('.')
            if (split.size < 2) {
                return null
            }
            when (split.size) {
                2 -> return PropKey(group = split[0], name = split[1])
                3 -> return PropKey(prefix = split[0], group = split[1], name = split[2])
                4 -> return PropKey(prefix = split[0], group = split[1], subgroup = split[2], name = split[3])
            }
            return PropKey(prefix = split[0], group = split[1], subgroup = split[2], name = split[3], type = split[4])
        }
    }
}