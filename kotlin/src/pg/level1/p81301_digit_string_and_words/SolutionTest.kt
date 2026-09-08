package pg.level1.p81301_digit_string_and_words

fun main() {
    val solution = Solution2()

    // 입출력 예 #1
    val s1 = "one4seveneight"
    val expected1 = 1478
    val actual1 = solution.solution(s1)
    println("#1 expected=$expected1 actual=$actual1 ${if (actual1 == expected1) "PASS" else "FAIL"}")

    // 입출력 예 #2
    val s2 = "23four5six7"
    val expected2 = 234567
    val actual2 = solution.solution(s2)
    println("#2 expected=$expected2 actual=$actual2 ${if (actual2 == expected2) "PASS" else "FAIL"}")

    // 입출력 예 #3
    val s3 = "2three45sixseven"
    val expected3 = 234567
    val actual3 = solution.solution(s3)
    println("#3 expected=$expected3 actual=$actual3 ${if (actual3 == expected3) "PASS" else "FAIL"}")

    // 입출력 예 #4
    val s4 = "123"
    val expected4 = 123
    val actual4 = solution.solution(s4)
    println("#4 expected=$expected4 actual=$actual4 ${if (actual4 == expected4) "PASS" else "FAIL"}")
}
