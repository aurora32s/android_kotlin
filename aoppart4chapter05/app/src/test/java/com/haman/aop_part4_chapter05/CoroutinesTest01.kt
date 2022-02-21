package com.haman.aop_part4_chapter05

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis
class CoroutinesTest01 {

    @Test
    fun test01() = runBlocking {
        val time = measureTimeMillis {
            val name = getName()
            val lastName = getLastName()
            println("Hello, $name $lastName")
        }
        println("measure time: $time")
    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val name = async { getName() }
            val lastName = async { getLastName() }
            println("Hello, ${name.await()} ${lastName.await()}")
        }
        println("measure time: $time")
    }

    suspend fun getName(): String {
        delay(1000)
        return "홍"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "길동"
    }
}