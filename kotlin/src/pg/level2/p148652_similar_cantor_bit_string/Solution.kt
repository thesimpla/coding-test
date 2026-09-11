package pg.level2.p148652_similar_cantor_bit_string
import kotlin.math.*

private const val SECTION_COUNT = 5
private const val ZERO_SECTION = 3
private const val DEBUG = false

class Solution {
    fun solution(n: Int, l: Long, r: Long): Int {
        if(DEBUG) println("[$n, $l, $r]")
        val answer = (count(n, r + 1) - count(n, l)).toInt()
        return answer
    }

    private fun count(n: Int, k: Long): Long {
        if(DEBUG) println("count[$n,$k]")
        var cnt: Long = 0
        var x = k
        val sizeOfN = 5.0.pow(n).toLong()
        val sectionInfo = IntArray(n)
        for (i in n downTo 1) {
            if(DEBUG) println("===== $i 번째 분해 =====")
            // x가 n의 몇번째인지 확인한다. => getSectionRange등으로 리팩토링
            for (j in 1..SECTION_COUNT) {
                val section: Long = 5.0.pow(i - 1).toLong() * j
                val lastSection: Long = (5.0.pow(i - 1).toLong() * (j - 1))
                if(DEBUG) print("section($lastSection ~ $section) ")
                if (x <= section) {
                    sectionInfo[i - 1] = j
                    if(DEBUG) println("x=$x, lastSection=$lastSection, next x = ${x-lastSection}")
                    x -= lastSection
                    break
                } else if (x <= sizeOfN) {
                    if(DEBUG) println("not in ")
                    if (j != ZERO_SECTION) {
                        cnt += 4.0.pow(i - 1).toLong()
                    }
                } else {
                    return 4.0.pow(n).toLong()
                }
            }
            if (sectionInfo[i - 1] == ZERO_SECTION) return cnt

            if(DEBUG) {
                println("sectionInfo[${n-1}] = ${sectionInfo[n-1]}")
                println("n=$i 일때 ${sectionInfo[i-1]}번째 지금까지 1의 값은 $cnt")
            }
        }
        return cnt
    }
}
