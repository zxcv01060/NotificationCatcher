package tw.idv.louisli.notificationcatcher

import org.junit.Assert
import org.junit.Test
import tw.idv.louisli.notificationcatcher.extension.MutableListExtension.move

class MutableListExtensionTest {
    @Test
    fun testMove4To7() {
        val list = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.move(4, 7)
        Assert.assertEquals(list[4], 5)
        Assert.assertEquals(list[6], 7)
        Assert.assertEquals(list[7], 4)
    }

    @Test
    fun testMove7To4() {
        val list = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.move(7, 4)
        Assert.assertEquals(list[4], 7)
        Assert.assertEquals(list[5], 4)
        Assert.assertEquals(list[7], 6)
    }
}