package pg.level1.p81301_digit_string_and_words

class Solution {
    fun solution(s: String): Int {
        var answer = 0

        val map: Map<String, Char> = mapOf(
            "zero" to '0', "one" to '1', "two" to '2',
            "three" to '3', "four" to '4', "five" to '5', "six" to '6',
            "seven" to '7', "eight" to '8', "nine" to '9',
        )

        val arr = mutableListOf<Char>()
        val tmpString = mutableListOf<Char>()
        for (i in s.indices) {
            if (s[i].code in 97..122) { // in 'a' ~ 'z'
                tmpString.add(s[i])
                for (j in map.keys) {
                    if (tmpString.joinToString("") == j) {
                        arr.add(map[j]!!)
                        tmpString.clear()
                    }
                }
            } else if (s[i].code in 48..57) { // in number
                arr.add(s[i])
            }
        }

        answer = arr.joinToString("").toInt()
        return answer
    }
}
