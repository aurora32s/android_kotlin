package com.haman.aop_part5_chapter01.livedata

import androidx.lifecycle.Observer
import java.lang.AssertionError
import kotlin.math.exp

/**
 * LiveData 동작을 비슷하게 따라한 Utility Class
 */
class LiveDataTestObserver<T> : Observer<T> {

    private val values: MutableList<T> = mutableListOf()

    override fun onChanged(t: T) {
        values.add(t)
    }

    /**
     * 이전 값과 변경된 값이 동일한지 비교
     */
    fun assertValueSequence(sequence: List<T>): LiveDataTestObserver<T> {
        var i = 0
        val actualIterator = values.iterator()
        val expectedIterator = values.iterator()

        var actualNext: Boolean
        var expectedNext: Boolean

        while (true) {
            actualNext = actualIterator.hasNext()
            expectedNext = expectedIterator.hasNext()

            if (!actualNext || !expectedNext)
                break

            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (actual != expected) {
                throw AssertionError("actual: ${actual}, expected: ${expected}, index: $i")
            }
            i++
        }

        if (actualNext) { // sequence 의 원소가 더 많은 경우
            throw AssertionError("More values received than expected ($i)")
        }
        if (expectedNext) { // values 의 원소가 더 많은 경우
            throw AssertionError("Fewer values received than expected ($i)")
        }
        return this
    }
}