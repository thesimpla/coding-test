---
name: pg-new
description: 프로그래머스 문제 링크로 java/src/pg 또는 kotlin/src/pg 아래에 레벨별 패키지, README.md, Solution 스켈레톤을 생성한다. "/pg-new <url> <level> [java|kotlin]" 형태로 호출 (기본값 kotlin).
---

# pg-new

프로그래머스(school.programmers.co.kr) 문제 링크와 레벨을 받아서, 이 저장소의 `java/src/pg` 또는 `kotlin/src/pg` 컨벤션에 맞는 패키지를 스캐폴딩한다.

## 입력

`/pg-new <programmers-url> <level> [language]`

- `<programmers-url>`: 예) `https://school.programmers.co.kr/learn/courses/30/lessons/12977`
- `<level>`: 정수 (1, 2, 3, ...). **레벨 뱃지는 문제 페이지가 SPA라 정적으로 긁을 수 없으므로 반드시 사용자가 직접 입력한다.** 둘 중 하나라도 빠지면 실행하지 말고 사용자에게 물어본다.
- `[language]`: `java` 또는 `kotlin`(기본값). 생략하면 kotlin.

## 절차

1. **문제 번호 추출**: URL에서 정규식 `lessons/(\d+)` 로 `id`를 뽑는다. 매치되지 않으면 URL 형식이 이상하다고 사용자에게 알리고 중단.

2. **문제 정보 조회**: WebFetch로 해당 URL을 읽어서 아래를 받아온다.
   - 한글 문제 제목
   - 제한사항 요약
   - 함수 시그니처 (언어는 보통 C/C++로 표시됨, 예: `int solution(int nums[], size_t nums_len)`)

   WebFetch는 이 URL에 대해 위 정보를 안정적으로 추출한다는 게 확인되어 있다 (레벨 정보만 못 가져온다).

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

7. **보고**: 생성한 경로와 파일 목록을 사용자에게 알려주고, 레벨은 사용자가 직접 입력한 값이라 자동 검증되지 않았다는 점을 한 줄로 짚어준다. 이어서 다 풀고 나면 `/pg-check`로 포맷/린트/리뷰를 돌릴 수 있다고 안내한다.

## 하지 않는 것

- 실제 풀이 로직을 대신 작성하지 않는다 (스켈레톤만).
- README의 접근/실수/복잡도 같은 회고성 섹션을 미리 채우지 않는다.
- 레벨을 추측하거나 스크래핑을 시도하지 않는다.
