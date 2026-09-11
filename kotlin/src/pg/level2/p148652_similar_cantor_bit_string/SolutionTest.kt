package pg.level2.p148652_similar_cantor_bit_string

fun main() {
    val solution = Solution()

    // 입출력 예 #1
    val n1 = 2
    val l1 = 4L
    val r1 = 17L
    val expected1 = 8
    val actual1 = solution.solution(n1, l1, r1)
    println("#1 expected=$expected1 actual=$actual1 ${if (actual1 == expected1) "PASS" else "FAIL"}")

    // 추가 케이스 #2
    val n2 = 5
    val l2 = 100L
    val r2 = 500L
    val expected2 = 145
    val actual2 = solution.solution(n2, l2, r2)
    println("#2 expected=$expected2 actual=$actual2 ${if (actual2 == expected2) "PASS" else "FAIL"}")

    // 추가 케이스 #3
    val n3 = 5
    val l3 = 500L
    val r3 = 1500L
    val expected3 = 321
    val actual3 = solution.solution(n3, l3, r3)
    println("#3 expected=$expected3 actual=$actual3 ${if (actual3 == expected3) "PASS" else "FAIL"}")

    // 추가 케이스 #4
    val n4 = 5
    val l4 = 2000L
    val r4 = 3000L
    val expected4 = 385
    val actual4 = solution.solution(n4, l4, r4)
    println("#4 expected=$expected4 actual=$actual4 ${if (actual4 == expected4) "PASS" else "FAIL"}")

    // 추가 케이스 #5
    val n5 = 3
    val l5 = 30L
    val r5 = 90L
    val expected5 = 21
    val actual5 = solution.solution(n5, l5, r5)
    println("#5 expected=$expected5 actual=$actual5 ${if (actual5 == expected5) "PASS" else "FAIL"}")
}
