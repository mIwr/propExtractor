package com.zeit.build.prop.extractor.model

import org.junit.Assert
import org.junit.Test

class PropKeyUnitTest {

    @Test
    fun keyFormTest() {
        var propKey = PropKey(group = "group", name = "name")
        Assert.assertEquals(propKey.key, "group.name")
        propKey = PropKey(group = "group", name = "name", type = "type")
        Assert.assertEquals(propKey.key, "group.name.type")
        propKey = PropKey(group = "group", subgroup = "subgroup", name = "name")
        Assert.assertEquals(propKey.key, "group.subgroup.name")
        propKey = PropKey(prefix = "prefix", group = "group", name = "name")
        Assert.assertEquals(propKey.key, "prefix.group.name")
        propKey = PropKey(prefix = "prefix", group = "group", name = "name", type = "type")
        Assert.assertEquals(propKey.key, "prefix.group.name.type")
        propKey = PropKey(prefix = "prefix", group = "group", subgroup = "subgroup", name = "name")
        Assert.assertEquals(propKey.key, "prefix.group.subgroup.name")
        propKey = PropKey(prefix = "prefix", group = "group", subgroup = "subgroup", name = "name", type = "type")
        Assert.assertEquals(propKey.key, "prefix.group.subgroup.name.type")
    }

    @Test
    fun rawKeyParseTest() {
        var rawKey = "group.name"
        var propKey = PropKey.fromRawKey(rawKey)
        Assert.assertEquals(propKey, PropKey(group = "group", name = "name"))
        rawKey = "prefix.group.name"
        propKey = PropKey.fromRawKey(rawKey)
        Assert.assertEquals(propKey, PropKey(prefix = "prefix", group = "group", name = "name"))
        rawKey = "prefix.group.subgroup.name"
        propKey = PropKey.fromRawKey(rawKey)
        Assert.assertEquals(propKey, PropKey(prefix = "prefix", group = "group", subgroup = "subgroup", name = "name"))
        rawKey = "prefix.group.subgroup.name.type"
        propKey = PropKey.fromRawKey(rawKey)
        Assert.assertEquals(propKey, PropKey(prefix = "prefix", group = "group", subgroup = "subgroup", name = "name", type = "type"))
    }
}