---
name: pg-tc-java
description: 프로그래머스 문제 URL을 직접 읽고 분석해서, 그 문제에만 의미가 있는 테스트케이스·반례·경계값과 IntelliJ에서 바로 실행되는 Java 테스트 코드를 생성한다. 풀이 코드를 같이 주면 구현 특화 반례와 brute-force 기반 랜덤 반례 탐색까지 만든다. "/pg-tc-java <url> [풀이경로] [옵션]" 형태로 호출.
---

# pg-tc-java

프로그래머스 문제를 **매번 새로 읽고 분석해서** 그 문제에 실제로 의미가 있는 테스트만 설계한다.
대상 언어는 **Java**다 (Kotlin은 `pg-tc-kotlin`).

## 입력

`/pg-tc-java <programmers-url> [풀이 코드 경로] [옵션]`

- **필수**: 프로그래머스 문제 URL
- **선택**:
  - 풀이 코드 경로 (생략 시, URL의 문제번호로 `java/src/pg/**/p{id}_*/Solution.java`를 찾아 있으면 자동으로 사용)
  - `랜덤테스트` / `no-random` — 랜덤 differential test 포함 여부 (기본: 가능하면 포함)
  - `성능테스트` / `no-perf` — 성능 테스트 포함 여부 (기본: 최대 입력이 의미 있으면 포함)

## 절대 원칙

1. **테스트 종류를 고정하지 않는다.** 아래 후보군은 후보일 뿐이다. 매번 문제를 읽고 **이 문제에 의미가 있는 것만** 고른다.
2. **문제의 입력 조건을 위반하는 테스트를 만들지 않는다.** 중복이 금지면 중복 테스트를 만들지 않고, 빈 입력이 불가능하면 빈 입력 테스트를 만들지 않는다. 음수가 없으면 음수를 넣지 않는다.
3. **expected를 추측하지 않는다.** 문제 규칙에 따라 직접 계산하고, 계산 근거를 주석/설명에 남긴다. 확신이 없으면 **그 테스트를 넣지 않는다.**
4. **사용자 풀이의 실행 결과를 expected로 쓰지 않는다.** 그 코드가 틀렸을 수 있다.
5. **비슷한 입력에서 숫자만 바꾼 테스트로 개수를 채우지 않는다.** 양보다 다양성.
6. **프로그래머스의 실제 제한시간을 모른다.** 로컬 측정치로 "통과한다"고 단정하지 않는다.

## 절차

### 1단계 — 문제 읽기

WebFetch로 URL을 읽어 아래를 추출한다. 한 번에 안 나오면 프롬프트를 바꿔 다시 호출한다.

- 문제 제목 / 문제 설명
- 입력 인자: 이름, 자료형, 크기, 값 범위
- 출력 자료형과 반환 규칙
- **제한사항 섹션 전문** (여기가 가장 중요하다)
- 입출력 예 테이블 전체 (값을 바꾸거나 요약하지 말 것)

### 2단계 — 제약 표 만들기

추출한 제한사항을 아래 항목으로 정리한다. 명시되지 않은 항목은 "명시 없음"으로 남기고, **명시 없음을 마음대로 가정으로 바꾸지 않는다.**

| 항목 | 확인 내용 |
|---|---|
| 크기 상한/하한 | 배열 길이, 문자열 길이, 정점 수, 간선 수 |
| 값 범위 | 최소/최대, 0 포함 여부, 음수 허용 여부 |
| 중복 | 중복 허용/금지 |
| 정렬 | 정렬 보장 여부 |
| 빈 값 | 빈 배열/빈 문자열 허용 여부 |
| 관계 조건 | 값 사이 제약 (예: completion 길이 = participant 길이 - 1) |
| 보장 조건 | 항상 해가 존재하는지, 연결 그래프인지, 트리인지 |
| 문자 범위 | 소문자만/숫자만/공백 포함 등 |

프로그래머스는 **시간·메모리 제한을 대개 명시하지 않는다.** 그러니 크기 상한으로 허용 복잡도를 역산한다.

| n 상한 | 허용 복잡도 |
|---|---|
| ~10 | O(n!), O(2ⁿ) |
| ~100 | O(n³) |
| ~1,000 | O(n²) |
| ~100,000 | O(n log n) |
| ~1,000,000+ | O(n), O(log n) |

### 3단계 — 유형·오류 가능성 판단

문제 유형을 판단한다 (복수 가능): 배열/문자열/구현/정렬/해시/스택/큐/우선순위큐/투포인터/슬라이딩윈도우/이분탐색/누적합/그리디/완전탐색/백트래킹/DFS/BFS/그래프/트리/최단경로/Union-Find/위상정렬/DP/수학/조합/순열/비트마스킹/시뮬레이션.

**분류 자체보다 "이 문제에서 어떤 오류가 나기 쉬운가"가 중요하다.**

### 4단계 — 자기 점검 16문

테스트를 만들기 전에 아래에 답한다. 답이 곧 테스트 전략이 된다.

1. 가능한 가장 작은 입력은? 2. 알고리즘 상태가 처음 변하는 최소 입력은? 3. 정답이 경계에 있으면? 4. 같은 값이 반복될 수 있나? 5. 정답이 여러 개인가? 6. 정답이 없는 경우가 허용되나? 7. 입력 정렬이 보장되나? 8. 구현자가 잘못된 정렬 가정을 할 수 있나? 9. int 오버플로 가능성은? 10. 입력을 수정하면 문제가 생기나? 11. 전역/static 상태 초기화 문제가 생길 수 있나? 12. 재귀 깊이가 위험한가? 13. 최대 입력에서 가장 커지는 자료구조는? 14. 이 알고리즘 아이디어를 깨는 작은 반례가 있나? 15. brute-force로 작은 입력을 검증할 수 있나? 16. 랜덤보다 완전열거가 더 적합한가?

### 5단계 — 전략 후보군에서 선택

**해당하는 것만** 고른다. 해당 없는 항목을 억지로 나열하지 않는다.

- **공통 경계**: 최소/최대 크기, 최소/최대 값, 크기 1, 크기 2, 점화식이 처음 적용되는 크기, 답이 경계에 위치, 첫/마지막 인덱스
- **값 분포**: 전부 동일, 전부 상이, 중복 과다, 하나만 다름, 최소·최대 혼합, 극단값 편중
- **정렬**: 이미 오름차순, 완전 역순, 거의 정렬, 동일 값 다수, 정렬 후 순서가 크게 바뀌는 입력
- **답의 위치**: 처음/중간/마지막에 결정, 답 하나/여럿, 규칙에 따른 tie-break, 전체가 답, 원소 하나가 답
- **수치**: 0, 1, 최대/최소, `Integer.MAX_VALUE` 근처, 덧셈·곱셈·누적합 오버플로, 정수 나눗셈 내림, 나머지, 부동소수점 오차
- **자료구조/알고리즘별**: 아래는 해당 유형일 때만
  - 배열: 길이 1, 첫/마지막 인덱스, 입력 수정 여부
  - 문자열: 길이 1, 동일 문자 반복, 교차 패턴, palindrome, prefix/suffix 경계, 끝에서 매칭, 대소문자
  - Map/Set: 동일 키 재등장, 한 키가 매우 빈번, 존재 여부와 빈도 혼동
  - Stack: 완전 중첩 / 전혀 중첩 안 됨, empty 경계
  - Queue·BFS: 시작=목적지, 한 단계 이동, 최단경로 복수, 여러 경로 합류, 도달 불가(허용될 때만)
  - DFS: 깊이 1, 긴 일자 구조, 분기 과다, 사이클, 재귀 깊이 최대, 여러 컴포넌트
  - 그래프: 정점 1, 간선 0, 일직선, 별 모양, 트리, 사이클, 여러 컴포넌트, 밀집/희소
  - 트리: 한쪽으로 치우침, 균형, leaf 1개/다수, 최대 깊이, root 특수 처리
  - 투포인터: 즉시 만남, 한쪽만 이동, 답이 처음/마지막 구간, 전체가 답
  - 슬라이딩 윈도우: 길이 1, 전체 길이, 최적 구간이 처음/끝, 동일 최적값 복수, 연속 축소
  - 이분탐색: 답이 lower/upper bound, 값 없음, 공간 크기 1, low==high, 조건이 처음/마지막 true
  - DP: 초기값만으로 해결, 점화식 첫 적용, 불가능 상태, 여러 경로가 같은 상태 도달, greedy가 틀리는 작은 반례
  - 그리디: **반례 탐색을 특히 적극적으로** — 당장 최선이 나중에 불리한 경우, tie, 선택 순서 의존, 정렬 기준 오선택
  - 완전탐색/백트래킹: 선택지 1개, 전부 실패, 첫/마지막에 성공, pruning 경계, 상태 복구 누락
  - 우선순위큐: 동일 priority, size 1, push/pop 교차, tie 처리
  - Union-Find: 자기 자신, 이미 연결된 union, 긴 chain, 마지막 union으로 전체 연결

### 6단계 — 사용자 풀이 코드 분석 (코드가 있을 때만)

문제 분석과 **별도로** 코드를 읽고 취약점을 찾은 뒤, **그 취약점을 실제로 드러내는 테스트**를 추가한다.

공통: off-by-one, 배열 범위, 루프 시작/종료값, `<` vs `<=`, 초기값(특히 answer 초기값), 빈 컬렉션 접근, null 가능성, 중복 처리, 정렬 가정, 입력 직접 수정, 호출 간 상태 잔존, 잘못된 early return.

**Java 특화**:
- `int` 오버플로와 `long` 변환 시점 — `long v = a * b;`는 a,b가 int면 **int로 먼저 곱한 뒤** 대입된다. `long v = (long) a * b;`가 필요한지 확인하고, 해당하면 지적한다.
- `Integer.MAX_VALUE`를 INF로 쓸 때 덧셈 오버플로
- Comparator에서 `a - b` 뺄셈 오버플로 → `Integer.compare(a, b)` 권장
- `equals`/`hashCode` 미구현 객체를 Map/Set 키로 사용
- `String` 비교에 `==` 사용
- `static` 필드 초기화 / 호출 간 잔존
- `PriorityQueue`의 comparator, 동일 priority 순서 미보장
- 배열 shallow copy (`clone()`은 1차원만 깊게 복사)
- 재귀 `StackOverflowError` 가능성
- `Arrays.asList`의 고정 크기, 오토박싱 비용

**BFS/DFS**: visited를 큐에 넣을 때 찍는지 꺼낼 때 찍는지, 같은 정점 중복 삽입, 재귀 깊이, 부모 처리, 사이클.
**DP**: dp 크기, base case, unreachable 표현, INF 오버플로, 이전 상태 접근 범위.
**그래프**: 0-based/1-based, 양방향 간선 누락, self-loop, 중복 간선, 컴포넌트 처리.

### 7단계 — 테스트케이스 작성

구성: 공식 예제 → 최소 경계 → 최대/극단 → 구조적 특수 → 알고리즘 취약 → 구현 취약 → 직접 만든 반례 → (필요 시) 대규모 성능 입력.

**의미 있는 케이스 10개 이상을 목표로 하되, 그만큼 서로 다른 의미를 만들 수 없는 문제라면 억지로 채우지 않는다.**

각 케이스는 아래를 갖춘다.

```
[테스트 이름] (중요도: CRITICAL|HIGH|MEDIUM|LOW)
입력:
예상 출력:
검증 목적:
위험 요소:   ← 어떤 종류의 버그를 잡는지
```

중요도 기준 — CRITICAL: 실제 오답 가능성이 높은 반례 / HIGH: 경계 조건, 흔한 구현 오류 / MEDIUM: 안정성 검증 / LOW: 추가 구조 검증.

### 8단계 — brute-force oracle과 자동 반례 탐색

작은 입력에서 **확실한 정답**을 구할 수 있는 문제라면 brute-force(완전탐색/모든 조합·순열/단순 DFS/O(n²)~O(n³))를 만든다. 성능보다 정확성 우선.

brute-force 자체의 정답성을 확신할 수 없다면 **억지로 만들지 말고**
"이 문제는 신뢰할 수 있는 독립 oracle을 간단히 만들기 어렵다"고 명시하고 랜덤 differential test를 생략한다.

가능하다면 랜덤 differential test를 만든다.
1. **문제 제약을 만족하는** 작은 랜덤 입력 생성 (순열이면 중복 금지, 트리면 간선 정확히 N-1, 연결 보장이면 연결 유지, 정렬 보장이면 정렬 유지, 문자 범위 준수)
2. solution 실행 → 3. brute-force 실행 → 4. 비교 → 5. 다르면 즉시 입력/expected/actual 출력하고 중단
6. `new Random(seed)`로 **seed 고정** (재현 가능해야 함), 기본 1,000~10,000회
7. **solution과 oracle에 같은 객체를 넘기지 않는다** — 각각 독립 복사본 (2차원 배열은 행마다 `clone()`)

입력 공간이 작으면(불리언 조합, 길이 1~8 배열, 작은 문자 집합, 작은 그래프·순열) **랜덤보다 완전열거가 강력하다.** 그 경우 완전열거를 우선 제안한다.

**중요**: 완전열거/랜덤 탐색 전체를 11단계의 타임아웃 헬퍼로 감싸서 부른다 (개별 호출이 아니라 전체를 한 번에). 그리고 7단계에서 이미 타임아웃(무한루프 의심)이 하나라도 났다면 **이 단계 자체를 건너뛴다** — 무한루프가 있는 채로 수천 회 반복 탐색을 돌리면 좀비 스레드가 쌓여 메모리를 압박한다. 실제로 이 순서를 안 지켜서 Gradle 데몬이 힙 부족으로 죽은 적이 있다.

### 9단계 — 상태 오염 / 입력 mutation 검증

- 각 테스트는 **독립된 입력 객체**를 쓴다 (재사용 금지).
- 사용자 코드에 `static` 필드, 전역 가변 컬렉션, 인스턴스 카운터가 있으면
  `solution(A)` → `solution(B)` → `solution(A)` 순서로 호출해 **첫 번째와 세 번째 결과가 같은지** 확인하는 테스트를 추가한다.
- 입력 배열을 넘긴 뒤 원본이 변형됐는지 비교하는 테스트도 유효하다.

### 10단계 — 성능·복잡도 분석

- 현재 풀이(있으면)의 시간·공간 복잡도와, 최대 입력에서의 위험도
- 성능 테스트는 **최악의 케이스를 유발하는 입력**으로 만든다 (단순히 큰 입력이 아니라)
- 재귀 깊이 최대, 정렬 최대 크기, BFS 큐 최대, DP 배열 최대 등 무엇이 가장 커지는지 짚는다
- **로컬 JVM 측정치 ≠ 프로그래머스 채점 환경.** 비교·이상 탐지용으로만 쓰고 통과 여부를 단정하지 않는다.
- `Runtime.totalMemory() - freeMemory()`는 GC/heap 예약 때문에 부정확하다. 출력하더라도 **참고값**임을 명시하고, 자료구조 크기·객체 수·복사본 생성으로 공간복잡도를 논한다.

### 11단계 — Java 테스트 코드 생성

- 파일: 대상 문제 폴더의 **`SolutionTest.java`** (이 이름이 Checkstyle 린트 제외 대상이라 기대값 리터럴이 MagicNumber로 잡히지 않는다). 이미 있으면 내용을 보여주고 교체 여부를 확인한다.
- 첫 줄 `package pg.level{N}.p{id}_{slug};` 를 반드시 넣는다. (`Solution.java`의 package 선언이 빠져 있으면 컴파일이 깨지므로 같이 확인해준다.)
- `Solution` 클래스 형태는 프로그래머스 제출 형식 그대로 유지한다 (`class Solution { public int solution(...) }`). `Solution`이 package-private이므로 테스트 클래스는 **같은 패키지**에 둔다.
- brute-force와 랜덤 생성기는 같은 파일에 `private static` 메서드로 둔다.

러너 요구사항:
- 테스트마다 **이름 / 입력 / expected / actual / PASS·FAIL** 출력
- 예외가 나도 그 테스트만 FAIL 처리하고 **다음 테스트를 계속 진행**, 예외 내용 출력
- 마지막에 총 개수 / 성공 / 실패 요약, 가능하면 개별·전체 실행시간
- 배열 출력은 `Arrays.toString()` / `Arrays.deepToString()`
- 배열 비교는 `Arrays.equals()` / `Arrays.deepEquals()` — 배열에 `==`를 쓰지 않는다
- **모든 케이스에 타임아웃을 건다** (아래 `ExecutorService` + `Future.get(timeout)` 패턴). 사용자 풀이에 무한루프가 있을 수 있고, 실제로 자주 있었다 — 타임아웃 없이 돌리면 러너 자체가 멈춘다.
- **타임아웃(무한루프 의심) 하나라도 나면 자동 반례 탐색(완전열거/랜덤)을 건너뛴다.** JVM 스레드는 안전하게 강제종료할 수 없어서 타임아웃 난 스레드는 daemon 좀비로 계속 돌며 할당을 반복한다 — 이미 하나 있는 상태에서 수천 회 탐색을 추가로 돌리면 좀비가 더 쌓여 **Gradle 데몬 힙을 압박해 데몬이 죽는 사고**로 이어질 수 있다 (실제로 겪은 사고).
- **`main()` 맨 끝에 `System.exit(0)`을 반드시 넣는다.** 보고가 끝나는 즉시 JVM을 종료시켜 좀비 스레드를 확실히 정리하고, 콘솔이 그 뒤로도 스팸으로 도배되는 것을 막는다.

러너 뼈대:

```java
package pg.level1.p00000_example;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class SolutionTest {
    private static int passed = 0;
    private static int failed = 0;
    private static boolean anyTimedOut = false;
    // 데몬 스레드 풀 — 타임아웃 난 작업은 JVM 종료 시(System.exit) 함께 정리된다.
    private static final ExecutorService POOL = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    private static <T> void check(String name, String priority, String input,
                                  T expected, long timeoutMs, Supplier<T> run, BiPredicate<T, T> eq) {
        long start = System.nanoTime();
        Future<T> future = POOL.submit(run::get);
        try {
            T actual = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            boolean ok = eq.test(expected, actual);
            if (ok) {
                passed++;
            } else {
                failed++;
            }
            System.out.println("[" + (ok ? "PASS" : "FAIL") + "] " + priority + " " + name
                    + " (" + elapsedMs + "ms)");
            if (!ok) {
                System.out.println("    input   : " + input);
                System.out.println("    expected: " + expected);
                System.out.println("    actual  : " + actual);
            }
        } catch (TimeoutException e) {
            failed++;
            anyTimedOut = true;
            System.out.println("[FAIL] " + priority + " " + name
                    + " — " + timeoutMs + "ms 안에 끝나지 않음 (무한 루프 의심)");
            System.out.println("    input   : " + input);
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] " + priority + " " + name + " — 예외 발생: " + e);
            System.out.println("    input   : " + input);
        }
    }

    private static <T> void check(String name, String priority, String input,
                                  T expected, Supplier<T> run) {
        check(name, priority, input, expected, 3_000, run, Object::equals);
    }

    /** 완전열거/랜덤 등 오래 걸릴 수 있는 탐색을 통째로 타임아웃과 함께 실행한다. */
    private static void runSearch(String label, long timeoutMs, Callable<String> search) {
        Future<String> future = POOL.submit(search);
        try {
            System.out.println(future.get(timeoutMs, TimeUnit.MILLISECONDS));
        } catch (TimeoutException e) {
            System.out.println(label + ": " + timeoutMs + "ms 안에 끝나지 않음 (무한 루프 의심)");
        } catch (Exception e) {
            System.out.println(label + " 실행 중 예외: " + e);
        }
    }

    public static void main(String[] args) {
        // 각 테스트는 독립된 입력 객체를 만든다
        check("공식 예제 #1", "HIGH", "...", 8, () -> new Solution().solution(/* ... */));

        System.out.println("---");
        System.out.println("total=" + (passed + failed) + " passed=" + passed + " failed=" + failed);

        System.out.println("---");
        if (anyTimedOut) {
            System.out.println("자동 반례 탐색 생략: 이미 타임아웃된 케이스가 있음 (무한루프로 의심됨, 먼저 그것부터 고칠 것)");
        } else {
            // runSearch("완전열거", 20_000, () -> exhaustiveDiff()); 등 8단계 탐색은 여기서 호출한다
        }

        System.exit(0);
    }
}
```

배열을 반환하는 문제라면 `eq`에 `(a, b) -> Arrays.equals((int[]) a, (int[]) b)` 같은 비교자를 넘기고, 출력 문자열도 `Arrays.toString(...)`으로 만든다.

터미널 실행: `cd java && ./gradlew runMain -PmainClass=pg.level{N}.p{id}_{slug}.SolutionTest`
IntelliJ에서는 `main` 옆 초록 ▶로 바로 실행된다.

## 출력 형식

**1~10단계 분석은 전부 그대로 수행하지만, 응답에는 결과만 짧게 보고한다.** 문제 분석 요약, 전략 선택 이유, 코드 취약점 분석, 반례 TOP 3 같은 서술은 응답에 먼저 풀어놓지 않는다 — 사용자가 나중에 "왜 이 케이스 넣었어?", "이거 왜 깨져?"라고 물으면 그때 해당 부분만 근거를 들어 답한다. 그 답을 위해 1~10단계에서 만든 분석(제약 표, 유형 판단, 코드 취약점, 반례 근거, 복잡도 등)은 매 실행마다 대화 맥락에 유지해두고, 다시 문제를 읽지 않고도 바로 참조할 수 있게 한다.

기본 응답은 아래만 포함한다.

1. **생성/갱신된 파일 경로** (`SolutionTest.java`, 필요 시 `Solution.java` package 선언 등 부수 확인사항)
2. **실행 결과 요약**: 총 개수 / PASS / FAIL, 실패한 테스트는 **이름만** 나열 (원인 설명은 생략)
3. 자동 반례 탐색을 돌렸다면 그 결과 한 줄 (반례 발견 여부만, 발견됐다면 입력값까지는 보여주되 원인 설명은 생략)
4. 마지막에 한 줄: "왜 실패했는지 궁금하면 물어봐."

**하지 말 것**: 문제 분석 요약, 테스트 전략 설명, 사용자 코드 분석, 복잡도 분석, TOP 3 반례 설명을 요청하지 않았는데 먼저 서술하는 것. 이 내용들은 질문받았을 때만 꺼낸다.

## 하지 않는 것

- 모든 문제에 같은 테스트 템플릿을 적용
- 문제 조건을 위반하는 입력 생성
- 공식 예제만 반복하거나, 숫자만 바꿔 개수 늘리기
- 사용자 solution 결과를 expected로 사용
- 정답성을 검증하지 않은 brute-force를 oracle로 사용
- 실제 제한시간을 모르면서 통과 여부 단정
- JVM 메모리 측정값을 프로그래머스 메모리 사용량이라고 주장
- int 오버플로·입력 mutation·상태 오염 가능성 무시
- Java와 Kotlin의 특성을 동일하게 취급
- 랜덤 테스트가 항상 충분하다고 주장
- 사용자의 `Solution` 풀이 코드를 승인 없이 수정 (분석·지적까지만)

## 사용 예시

```
/pg-tc-java https://school.programmers.co.kr/learn/courses/30/lessons/12977
/pg-tc-java https://school.programmers.co.kr/learn/courses/30/lessons/12977 java/src/pg/level1/p12977_make_prime/Solution.java
/pg-tc-java https://school.programmers.co.kr/learn/courses/30/lessons/42576 no-random 성능테스트
```
