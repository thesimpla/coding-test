package pg.level1.p81301_digit_string_and_words
class Solution2 {
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
            if (s[i].isLetter()) { // in 'a' ~ 'z'
                tmpString.add(s[i])
                if(map.keys.contains(tmpString.joinToString(""))) {
                    val value = tmpString.joinToString("")
                    arr.add(map[value]!!)
                    tmpString.clear()

                }
            } else if (s[i].isDigit()) { // in number
                arr.add(s[i])
            }
        }

        answer = arr.joinToString("").toInt()
        return answer
    }
}
