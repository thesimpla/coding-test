---
name: pg-check
description: java/src/pg 또는 kotlin/src/pg 아래 프로그래머스 풀이 하나를 포맷팅(Spotless)하고 정적 린트(Checkstyle/detekt)한 뒤, 클린 코드 관점으로 AI 리뷰까지 수행한다. "/pg-check [폴더경로]" 형태로 호출.
---

# pg-check

`/pg-create`로 만든 패키지에 실제 풀이 코드를 다 작성한 뒤, 포맷 정리 → 정적 린트 → AI 클린 코드 리뷰까지 한 번에 돌린다. java, kotlin 둘 다 지원한다.

## 입력

`/pg-check [폴더 경로]`

- 폴더 경로를 생략하면, `java/src/pg`와 `kotlin/src/pg` 전체를 통틀어 가장 최근에 수정된 `Solution.java`/`Solution.kt`가 속한 폴더를 대상으로 한다.
- 경로를 준 경우 해당 폴더 안의 `Solution.java` 또는 `Solution.kt`를 대상으로 한다.
- 경로가 `java/...` 아래면 java 파이프라인을, `kotlin/...` 아래면 kotlin 파이프라인을 쓴다.

## 절차

1. **언어 판별**: 대상 경로가 `java/`로 시작하면 java, `kotlin/`이면 kotlin.

2. **포맷 + 린트 실행**:

   **java**:
   ```bash
   cd java && ./gradlew spotlessApply checkstyleMain
   ```
   - 리포트: `java/build/reports/checkstyle/main.xml`

   **kotlin**:
   ```bash
   cd kotlin && ./gradlew spotlessApply detekt
   ```
   - 리포트: `kotlin/build/reports/detekt/detekt.txt` (한 줄에 한 이슈, 파싱하기 가장 쉬움)

   - `spotlessApply`는 대상 파일들을 실제로 재포맷한다 (java는 google-java-format AOSP 스타일/4-space, kotlin은 ktlint). 의미 변화가 없는 스타일 정리이므로 승인 없이 바로 반영해도 된다.
   - Checkstyle/detekt 태스크가 위반사항을 리포트만 하고 빌드를 실패시킬 수 있다 (`maxWarnings = 0` / `buildUponDefaultConfig` 기준 위반 시 실패). 실패해도 정상 — 다음 단계로 진행한다.

3. **Spotless 결과 확인**: `git diff -- <대상경로>`로 실제로 뭐가 바뀌었는지 확인해서 사용자에게 간단히 요약 (예: "들여쓰기/공백 정리, N줄 변경").

4. **정적 린트 리포트 파싱**: 위 리포트 파일에서 대상 파일에 해당하는 위반 항목들만 추려서 라인번호/규칙/메시지로 요약. (다른 pg 폴더의 기존 위반사항은 이번 리뷰 범위가 아니므로 대상 파일 것만 추린다.)

5. **AI 클린 코드 리뷰**: Checkstyle/detekt는 네이밍/매직넘버/메서드길이/미사용import/빈블록 정도만 잡아준다. 여기서 추가로 Claude가 대상 Solution 파일을 직접 읽고 아래 관점으로 리뷰한다 (정적 린트가 못 잡는 것 위주):
   - 죽은 코드 / 주석 처리된 코드 (예: 디버그용 `println`/`System.out.println` 잔재)
   - 변수명이 문법적으로는 통과해도 의미가 불분명한 경우 (`a`, `tmp`, `flag2` 등)
   - 중첩이 과도하게 깊은 반복문/조건문, 조기 반환(early return)으로 단순화할 수 있는 부분
   - 중복 로직
   - kotlin의 경우 관용적이지 않은 Java 스타일 코드(불필요한 `var`/가변 상태, `for` 인덱스 루프 대신 컬렉션 함수로 표현 가능한 부분 등)도 짚어준다
   - 알고리즘/자료구조 선택이 문제 제약조건에 비해 비효율적인 경우 (간단히 언급만, 정답이 맞다면 필수 지적사항은 아님)

6. **결과 제시**: Spotless가 이미 반영한 포맷 변경은 "적용 완료"로 보고하고, 정적 린트 위반 + AI 리뷰 코멘트는 파일:라인 형태로 목록화해서 보여준다. **로직/네이밍 변경은 사용자 승인 없이 자동으로 고치지 않는다** — 사용자가 반영을 원하면 그때 Edit으로 수정한다.

## 하지 않는 것

- 정적 린트 위반이나 AI 리뷰 코멘트를 사용자 확인 없이 자동 수정하지 않는다 (포맷팅 제외).
- 다른 pg 폴더나 bj 폴더까지 확장해서 검사하지 않는다 — 대상은 항상 지정된(또는 최근 수정된) 폴더 하나.
- 문제를 다시 풀거나 정답 여부를 판단하지 않는다 (코드 스타일/가독성 리뷰만).
