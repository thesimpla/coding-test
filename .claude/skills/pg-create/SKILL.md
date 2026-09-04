---
name: pg-create
description: 프로그래머스 문제 링크로 java/src/pg 또는 kotlin/src/pg 아래에 레벨별 패키지, README.md, Solution 스켈레톤, 입출력 예 기반 SolutionTest를 생성한다. "/pg-create <url> <level> [java|kotlin]" 형태로 호출 (기본값 kotlin).
---

# pg-create

프로그래머스(school.programmers.co.kr) 문제 링크와 레벨을 받아서, 이 저장소의 `java/src/pg` 또는 `kotlin/src/pg` 컨벤션에 맞는 패키지를 스캐폴딩한다.

## 입력

`/pg-create <programmers-url> <level> [language]`

- `<programmers-url>`: 예) `https://school.programmers.co.kr/learn/courses/30/lessons/12977`
- `<level>`: 정수 (1, 2, 3, ...). **레벨 뱃지는 문제 페이지가 SPA라 정적으로 긁을 수 없으므로 반드시 사용자가 직접 입력한다.** 둘 중 하나라도 빠지면 실행하지 말고 사용자에게 물어본다.
- `[language]`: `java` 또는 `kotlin`(기본값). 생략하면 kotlin.

## 절차

1. **문제 번호 추출**: URL에서 정규식 `lessons/(\d+)` 로 `id`를 뽑는다. 매치되지 않으면 URL 형식이 이상하다고 사용자에게 알리고 중단.

2. **문제 정보 조회**: WebFetch로 해당 URL을 읽어서 아래를 받아온다.
   - 한글 문제 제목
   - 제한사항 요약
   - 함수 시그니처 (언어는 보통 C/C++로 표시됨, 예: `int solution(int nums[], size_t nums_len)`)
   - **입출력 예 테이블 전체** (파라미터별 입력값과 return 값). 테스트 케이스 생성에 쓰므로 값을 임의로 바꾸거나 요약하지 말고 그대로 가져온다.

   WebFetch는 이 URL에 대해 위 정보를 안정적으로 추출한다는 게 확인되어 있다 (레벨 정보만 못 가져온다).
   입출력 예가 한 번에 안 나오면 "입출력 예 테이블을 그대로 추출해줘"로 프롬프트를 바꿔 한 번 더 호출한다.

3. **슬러그 생성**: 한글 제목을 요약하는 영문 snake_case 슬러그를 2~4단어로 만든다.
   - 참고 예시(기존 컨벤션): "소수 만들기" → `make_prime`
   - 너무 축약하지 말고, 문제를 아는 사람이 폴더명만 보고 알아볼 수 있는 수준으로.
   - java/kotlin 모두 같은 슬러그를 쓴다 (같은 문제라면 두 언어에서 동일한 `p{id}_{slug}` 이름).

4. **경로 결정**:
   - java: `java/src/pg/level{level}/p{id}_{slug}/`
   - kotlin: `kotlin/src/pg/level{level}/p{id}_{slug}/`
   - 이미 존재하면 실행을 멈추고 사용자에게 알린다 (덮어쓰지 않는다).

5. **README.md 생성** (기존 예시 `java/src/pg/level1/p12977_make_prime/README.md` 포맷을 언어와 무관하게 그대로 따른다):
   ```markdown
   # {한글 제목}
   # 링크 
   {url}

   ## 접근
   - (TODO: 풀이 후 작성)
   ```
   - `## 실수`, `## 복잡도` 같은 섹션은 문제를 실제로 풀어야 채울 수 있는 내용이므로 지금 단계에서는 만들지 않는다. 접근 섹션 하나만 TODO로 남긴다.

6. **Solution 파일 생성**:
   - 클래스명은 항상 `Solution`, 메서드명은 항상 `solution` (기존 컨벤션, 언어 공통).
   - WebFetch로 받은 함수 시그니처를 각 언어 타입으로 변환한다. C 스타일 배열+길이 파라미터(`int nums[], size_t nums_len`)는 배열이 길이를 스스로 담고 있으므로 길이 파라미터를 드롭한다.

   **java** — `java/src/pg/level{level}/p{id}_{slug}/Solution.java`:
   ```java
   package pg.level{level}.p{id}_{slug};

   class Solution {
       public int solution(int[] nums) {
           int answer = 0;

           return answer;
       }
   }
   ```
   (타입 매핑: 문자열 `String`, 문자열 배열 `String[]`, 2차원 배열 `int[][]` 등 표준적으로.)

   **kotlin** — `kotlin/src/pg/level{level}/p{id}_{slug}/Solution.kt`:
   ```kotlin
   package pg.level{level}.p{id}_{slug}

   class Solution {
       fun solution(nums: IntArray): Int {
           var answer = 0

           return answer
       }
   }
   ```
   (타입 매핑: `Int`/`IntArray`, `String`/`Array<String>`, `Boolean`, `Long`/`LongArray` 등 Kotlin 표준 타입으로. 패키지명에 숫자/언더스코어가 들어가는 건 이 저장소 컨벤션이라 detekt/ktlint 설정에서 이미 허용해뒀다 — 신경 쓸 필요 없음.)

   - 본문은 반환 타입에 맞는 최소 기본값만 반환하는 빈 스켈레톤으로 둔다 (예: `Int`/`int` → `0`, `Boolean`/`boolean` → `false`, `String` → `""`). 실제 풀이 로직은 채우지 않는다.

7. **테스트 케이스 파일 생성**: 같은 폴더에 `SolutionTest.kt` / `SolutionTest.java`를 만들어, 2번에서 가져온 **입출력 예를 전부** 실행해보고 기대값과 비교해 출력하게 한다.
   - 새 의존성(JUnit 등)을 추가하지 않는다. 그냥 `main()`에서 돌려보고 결과를 출력하는 방식 — IntelliJ에서 `main` 옆 초록색 ▶로 바로 실행할 수 있다.
   - 실제값이 기대값과 같으면 `PASS`, 다르면 `FAIL`로 표시하고 둘 다 찍어준다.
   - 배열 반환 문제는 `contentEquals`(kotlin) / `Arrays.equals`(java)로 비교하고, 출력도 `contentToString`/`Arrays.toString`을 쓴다.

   **kotlin** — `SolutionTest.kt`:
   ```kotlin
   package pg.level{level}.p{id}_{slug}

   fun main() {
       val solution = Solution()

       // 입출력 예 #1
       val jobs1 = arrayOf(intArrayOf(0, 3), intArrayOf(1, 9), intArrayOf(3, 5))
       val expected1 = 8
       val actual1 = solution.solution(jobs1)
       println("#1 expected=$expected1 actual=$actual1 ${if (actual1 == expected1) "PASS" else "FAIL"}")
   }
   ```

   **java** — `SolutionTest.java`:
   ```java
   package pg.level{level}.p{id}_{slug};

   public class SolutionTest {
       public static void main(String[] args) {
           Solution solution = new Solution();

           // 입출력 예 #1
           int[][] jobs1 = {{0, 3}, {1, 9}, {3, 5}};
           int expected1 = 8;
           int actual1 = solution.solution(jobs1);
           System.out.println("#1 expected=" + expected1 + " actual=" + actual1
                   + (actual1 == expected1 ? " PASS" : " FAIL"));
       }
   }
   ```

   - 입출력 예가 여러 개면 번호를 붙여 전부 넣는다 (`#1`, `#2`, ...).
   - 이 파일은 **로컬 확인용**이고 프로그래머스에 제출하는 건 `Solution` 쪽이라는 걸 사용자에게 알려준다.

8. **보고**: 생성한 경로와 파일 목록을 사용자에게 알려주고, 레벨은 사용자가 직접 입력한 값이라 자동 검증되지 않았다는 점을 한 줄로 짚어준다. 이어서 다 풀고 나면 `/pg-check`로 포맷/린트/리뷰를 돌릴 수 있다고 안내한다.

## 하지 않는 것

- 실제 풀이 로직을 대신 작성하지 않는다 (스켈레톤만).
- README의 접근/실수/복잡도 같은 회고성 섹션을 미리 채우지 않는다.
- 레벨을 추측하거나 스크래핑을 시도하지 않는다.
