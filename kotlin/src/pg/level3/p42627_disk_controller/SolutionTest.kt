package pg.level3.p42627_disk_controller

import kotlin.random.Random
import kotlin.system.exitProcess

// ---------------------------------------------------------------------------
// 러너
// ---------------------------------------------------------------------------

private var passed = 0
private var failed = 0

/**
 * 타임아웃된 케이스는 스레드를 안전하게 강제종료할 방법이 없어서 백그라운드에
 * 좀비 스레드로 남는다(무한루프면 계속 할당하며 메모리를 먹는다).
 * 이미 하나라도 타임아웃이 났으면 20초짜리 자동 반례 탐색은 건너뛴다 —
 * 안 그러면 좀비 스레드가 더 쌓여서 Gradle 데몬 힙을 압박할 수 있다.
 */
private var anyTimedOut = false

/** jobs 배열을 매번 새로 만든다. Solution이 입력을 수정해도 다른 테스트에 영향이 없도록. */
private fun jobsOf(vararg pairs: Pair<Int, Int>): Array<IntArray> =
    Array(pairs.size) { intArrayOf(pairs[it].first, pairs[it].second) }

private fun Array<IntArray>.deepCopy(): Array<IntArray> = Array(size) { this[it].copyOf() }

private fun Array<IntArray>.show(): String = joinToString(", ", "[", "]") { it.contentToString() }

/**
 * 현재 풀이가 무한 루프에 빠질 수 있어서 테스트마다 타임아웃을 건다.
 * 타임아웃된 스레드는 강제 종료할 수 없으므로 daemon으로 띄운다(JVM 종료는 가능).
 */
private fun <T> callWithTimeout(timeoutMs: Long, block: () -> T): Result<T>? {
    var outcome: Result<T>? = null
    val worker = Thread {
        outcome = runCatching(block)
    }
    worker.isDaemon = true
    worker.start()
    worker.join(timeoutMs)
    return if (worker.isAlive) null else outcome
}

private fun <T> check(
    name: String,
    priority: String,
    input: String,
    expected: T,
    timeoutMs: Long = 3_000,
    eq: (T, T) -> Boolean = { a, b -> a == b },
    run: () -> T,
) {
    val startedAt = System.nanoTime()
    val outcome = callWithTimeout(timeoutMs, run)
    val elapsedMs = (System.nanoTime() - startedAt) / 1_000_000

    if (outcome == null) {
        failed++
        anyTimedOut = true
        println("[FAIL] $priority $name — ${timeoutMs}ms 안에 끝나지 않음 (무한 루프 의심)")
        println("    input   : $input")
        return
    }
    outcome.fold(
        onSuccess = { actual ->
            val ok = eq(expected, actual)
            if (ok) passed++ else failed++
            println("[${if (ok) "PASS" else "FAIL"}] $priority $name (${elapsedMs}ms)")
            if (!ok) {
                println("    input   : $input")
                println("    expected: $expected")
                println("    actual  : $actual")
            }
        },
        onFailure = { e ->
            failed++
            println("[FAIL] $priority $name — 예외 발생: $e")
            println("    input   : $input")
        },
    )
}

// ---------------------------------------------------------------------------
// oracle: 문제에 서술된 컨트롤러 동작을 그대로 O(n^2)로 시뮬레이션한다.
//
// 주의: "평균을 최소화하는 최적 스케줄"을 순열 완전탐색으로 구하면 안 된다.
// 문제는 "유휴 상태이고 큐가 비어있지 않으면 즉시 가장 우선순위 높은 작업을 시작"하는
// 컨트롤러의 동작을 정의하고 있고, 이 규칙은 최적해와 다르다.
// 반례: [[0,5],[1,1]] → 규칙대로면 5, 일부러 기다리는 최적 스케줄이면 4.
// ---------------------------------------------------------------------------

private fun oracle(jobs: Array<IntArray>): Int {
    val n = jobs.size
    val done = BooleanArray(n)
    var now = 0
    var totalTurnaround = 0L

    repeat(n) {
        // 지금 시점에 요청된 작업 중 (소요시간, 요청시각, 번호) 순으로 최소인 것
        var pick = -1
        for (i in 0 until n) {
            if (done[i] || jobs[i][0] > now) continue
            if (pick == -1) {
                pick = i
                continue
            }
            val better = compareValuesBy(jobs[i], jobs[pick], { it[1] }, { it[0] })
            if (better < 0) pick = i
        }
        if (pick == -1) {
            // 대기 중인 작업이 없으면 다음 요청 시각까지 유휴 상태로 대기
            var next = Int.MAX_VALUE
            for (i in 0 until n) {
                if (!done[i] && jobs[i][0] < next) next = jobs[i][0]
            }
            now = next
            for (i in 0 until n) {
                if (done[i] || jobs[i][0] > now) continue
                if (pick == -1) {
                    pick = i
                    continue
                }
                val better = compareValuesBy(jobs[i], jobs[pick], { it[1] }, { it[0] })
                if (better < 0) pick = i
            }
        }
        now += jobs[pick][1]
        totalTurnaround += (now - jobs[pick][0]).toLong()
        done[pick] = true
    }
    return (totalTurnaround / n).toInt()
}

// ---------------------------------------------------------------------------
// 자동 반례 탐색
// ---------------------------------------------------------------------------

/** 입력 공간이 작은 구간(n<=3, s<=3, l<=3)은 랜덤보다 완전열거가 강하다. */
private fun exhaustiveDiff(maxN: Int = 3, maxS: Int = 3, maxL: Int = 3): String? {
    val options = mutableListOf<Pair<Int, Int>>()
    for (s in 0..maxS) for (l in 1..maxL) options += s to l

    for (n in 1..maxN) {
        val idx = IntArray(n)
        while (true) {
            val jobs = Array(n) { intArrayOf(options[idx[it]].first, options[idx[it]].second) }
            val expected = oracle(jobs.deepCopy())
            val actual = Solution().solution(jobs.deepCopy())
            if (expected != actual) {
                return "완전열거 반례: jobs=${jobs.show()} expected=$expected actual=$actual"
            }
            var carry = n - 1
            while (carry >= 0) {
                idx[carry]++
                if (idx[carry] < options.size) break
                idx[carry] = 0
                carry--
            }
            if (carry < 0) break
        }
    }
    return null
}

/** 제약(1<=n, 0<=s<=1000, 1<=l<=1000)을 지키되 작은 범위로 랜덤 생성해 oracle과 비교. */
private fun randomDiff(trials: Int = 2_000, seed: Int = 42): String? {
    val rnd = Random(seed)
    repeat(trials) {
        val n = rnd.nextInt(1, 9)
        val jobs = Array(n) { intArrayOf(rnd.nextInt(0, 13), rnd.nextInt(1, 9)) }
        val expected = oracle(jobs.deepCopy())
        val actual = Solution().solution(jobs.deepCopy())
        if (expected != actual) {
            return "랜덤 반례(seed=$seed): jobs=${jobs.show()} expected=$expected actual=$actual"
        }
    }
    return null
}

// ---------------------------------------------------------------------------

fun main() {
    // --- 공식 예제 ---
    check("공식 예제", "HIGH", "[[0,3],[1,9],[3,5]]", expected = 8) {
        Solution().solution(jobsOf(0 to 3, 1 to 9, 3 to 5))
    }

    // --- 최소 경계 ---
    check("작업 1개, 0초에 요청", "HIGH", "[[0,1]]", expected = 1) {
        Solution().solution(jobsOf(0 to 1))
    }
    check("작업 1개, 0초가 아닌 시각에 요청", "CRITICAL", "[[5,3]]", expected = 3) {
        Solution().solution(jobsOf(5 to 3))
    }

    // --- 유휴 구간 ---
    check("중간에 유휴 구간", "CRITICAL", "[[0,1],[5,1]]", expected = 1) {
        Solution().solution(jobsOf(0 to 1, 5 to 1))
    }
    check("유휴 후 대기열에 2개, 실행 중 1개 추가 도착", "CRITICAL", "[[0,1],[5,3],[5,1],[6,1]]", expected = 2) {
        Solution().solution(jobsOf(0 to 1, 5 to 3, 5 to 1, 6 to 1))
    }

    // --- 우선순위 규칙 ---
    check("동시 요청, 소요시간 짧은 순서로 처리", "HIGH", "[[0,3],[0,1],[0,2]]", expected = 3) {
        Solution().solution(jobsOf(0 to 3, 0 to 1, 0 to 2))
    }
    check("소요시간 역순으로 들어온 동시 요청", "HIGH", "[[0,4],[0,3],[0,2],[0,1]]", expected = 5) {
        Solution().solution(jobsOf(0 to 4, 0 to 3, 0 to 2, 0 to 1))
    }
    check("실행 중 도착한 짧은 작업 (선점 없음)", "HIGH", "[[0,10],[1,1],[2,1]]", expected = 10) {
        Solution().solution(jobsOf(0 to 10, 1 to 1, 2 to 1))
    }
    check("기다리는 게 유리해도 즉시 시작해야 함", "HIGH", "[[0,5],[1,1]]", expected = 5) {
        Solution().solution(jobsOf(0 to 5, 1 to 1))
    }

    // --- 경계/나눗셈 ---
    check("완료 시각과 요청 시각이 정확히 일치", "HIGH", "[[0,2],[2,1]]", expected = 1) {
        Solution().solution(jobsOf(0 to 2, 2 to 1))
    }
    check("동일 소요시간 다수 + 평균 소수점 버림", "MEDIUM", "[[0,2],[0,2],[1,2]]", expected = 3) {
        Solution().solution(jobsOf(0 to 2, 0 to 2, 1 to 2))
    }
    check("값 최대 경계 (s=1000, l=1000)", "MEDIUM", "[[1000,1000]]", expected = 1000) {
        Solution().solution(jobsOf(1000 to 1000))
    }

    // --- 입력 mutation / 재호출 ---
    run {
        val shared = jobsOf(0 to 3, 1 to 9, 3 to 5)
        val snapshot = shared.deepCopy()
        check("같은 배열로 두 번 호출해도 같은 결과", "CRITICAL", "[[0,3],[1,9],[3,5]] 재사용", expected = 8) {
            Solution().solution(shared)
            Solution().solution(shared)
        }
        val mutated = shared.indices.any { !shared[it].contentEquals(snapshot[it]) }
        if (mutated) {
            failed++
            println("[FAIL] CRITICAL 입력 배열 원본 보존")
            println("    before  : ${snapshot.show()}")
            println("    after   : ${shared.show()}")
        } else {
            passed++
            println("[PASS] CRITICAL 입력 배열 원본 보존")
        }
    }

    // --- 성능 (최대 입력) ---
    // 500개 전부 s=0, l=1000 → k번째 완료 시각 = 1000k, 합 = 1000*(1+..+500) = 125,250,000
    // 평균 = 125,250,000 / 500 = 250,500
    check("성능: 500개 동시 요청, 소요시간 최대", "MEDIUM", "n=500, s=0, l=1000", expected = 250_500, timeoutMs = 10_000) {
        Solution().solution(Array(500) { intArrayOf(0, 1000) })
    }

    println("---")
    println("total=${passed + failed} passed=$passed failed=$failed")

    // --- 자동 반례 탐색 ---
    // 이미 타임아웃(무한루프 의심)이 하나라도 있었으면 20초짜리 탐색을 또 돌리지 않는다.
    // 좀비 스레드가 더 쌓여서 메모리를 압박하는 것보다, 먼저 그 버그를 고치는 게 안전하다.
    println("---")
    if (anyTimedOut) {
        println("자동 반례 탐색 생략: 이미 타임아웃된 케이스가 있음 (무한루프로 의심됨, 먼저 그것부터 고칠 것)")
    } else {
        val exhaustive = callWithTimeout(20_000) { exhaustiveDiff() }
        when {
            exhaustive == null -> println("완전열거: 20초 안에 끝나지 않음 (무한 루프 의심)")
            exhaustive.getOrNull() == null -> println("완전열거(n<=3, s<=3, l<=3): 반례 없음")
            else -> println(exhaustive.getOrNull())
        }

        val random = callWithTimeout(20_000) { randomDiff() }
        when {
            random == null -> println("랜덤 differential: 20초 안에 끝나지 않음 (무한 루프 의심)")
            random.getOrNull() == null -> println("랜덤 differential 2,000회: 반례 없음")
            else -> println(random.getOrNull())
        }
    }

    // 타임아웃된 좀비 스레드(무한루프)가 있다면 프로세스를 즉시 끝내서 더 이상
    // 메모리를 먹거나 콘솔을 스팸하지 못하게 한다. daemon 스레드라 안전하게 죽는다.
    exitProcess(0)
}
