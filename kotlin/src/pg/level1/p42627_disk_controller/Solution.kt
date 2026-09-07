package pg.level1.p42627_disk_controller
import java.util.PriorityQueue

class Solution {
    fun solution(jobs: Array<IntArray>): Int {
        var answer = 0

        val sizeOfJobs = jobs.size
        val turnaroundTime: IntArray = IntArray(sizeOfJobs)
        val visitedJobs: BooleanArray = BooleanArray(sizeOfJobs)
        var remainingJobs = sizeOfJobs
        var currentTime = 0

        while (remainingJobs > 0) {
            val currentJobs = PriorityQueue<Array<Int>>(
                compareBy<Array<Int>> { it[2] } // 소요시간 비교해서 더 작은 값 먼저
                    .thenBy { it[1] } // 작업 요청 시간이 빠른 것
                    .thenBy { it[0] }, // 작업번호 비교해서 더 작은 값 먼저
            )

            for (i in jobs.indices) {
                val s = jobs[i][0]
                val l = jobs[i][1]
                if (s <= currentTime && !visitedJobs[i]) {
                    val job: Array<Int> = arrayOf(i, s, l)
                    currentJobs.add(job)
                }
            }

            if (currentJobs.isNotEmpty()) {
                val currentJob: Array<Int> = currentJobs.poll()
                val duration = currentJob[2] // 작업 소요시간
                val requestTime = currentJob[1] // 작업 요청 시간
                val endTime = currentTime + duration // 작업이 실제 끝난 시간
                val index = currentJob[0] // 작업 요청 번호
                turnaroundTime[index] = endTime - requestTime

                currentTime = endTime
                remainingJobs = remainingJobs - 1

                visitedJobs[index] = true

                currentJobs.clear()
            } else {
                // 유휴시간 처리
                currentTime += 1
            }
        }
        answer = (turnaroundTime.sum() / sizeOfJobs).toInt()

        return answer
    }
}
