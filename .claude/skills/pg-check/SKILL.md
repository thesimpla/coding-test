---
name: pg-check
description: java/src/pg 아래 프로그래머스 풀이 하나를 Spotless로 포맷하고 Checkstyle로 린트한 뒤, 클린 코드 관점으로 AI 리뷰까지 수행한다. "/pg-check [폴더경로]" 형태로 호출.
---

# pg-check

`/pg-new`로 만든 패키지에 실제 풀이 코드를 다 작성한 뒤, 포맷 정리 → 정적 린트 → AI 클린 코드 리뷰까지 한 번에 돌린다.

## 입력

`/pg-check [폴더 경로]`

- 폴더 경로를 생략하면, `java/src/pg` 아래에서 가장 최근에 수정된 `Solution.java`가 속한 폴더를 대상으로 한다 (`find java/src/pg -name Solution.java -newer ... ` 또는 `ls -t`로 판단).
- 경로를 준 경우 해당 폴더 안의 `Solution.java`를 대상으로 한다.

## 절차

1. **포맷 + 린트 실행**:
   ```bash
   cd java && ./gradlew spotlessApply checkstyleMain
   ```
   - `spotlessApply`는 대상 파일들을 실제로 재포맷한다 (google-java-format). 이건 의미 변화가 없는 스타일 정리이므로 승인 없이 바로 반영해도 된다.
   - `checkstyleMain`이 위반사항을 리포트만 하고 빌드를 실패시킬 수 있다 (`maxWarnings = 0`). 실패해도 정상 — 다음 단계로 진행한다.

2. **Spotless 결과 확인**: `git diff -- java/src/pg/<대상경로>` 로 실제로 뭐가 바뀌었는지 확인해서 사용자에게 간단히 요약 (예: "들여쓰기/공백 정리, N줄 변경").

3. **Checkstyle 리포트 파싱**: `java/build/reports/checkstyle/main.xml`을 읽어서, 대상 파일에 해당하는 `<error>` 항목들을 라인번호/규칙/메시지로 요약. (다른 pg 폴더의 기존 위반사항은 이번 리뷰 범위가 아니므로 대상 파일 것만 추린다.)

4. **AI 클린 코드 리뷰**: Checkstyle은 네이밍/매직넘버/메서드길이/미사용import/빈블록 정도만 잡아준다. 여기서 추가로 Claude가 대상 `Solution.java`를 직접 읽고 아래 관점으로 리뷰한다 (Checkstyle이 못 잡는 것 위주):
   - 죽은 코드 / 주석 처리된 코드 (예: `// System.out.println(...)` 같은 디버그 잔재)
   - 변수명이 문법적으로는 통과해도 의미가 불분명한 경우 (`a`, `tmp`, `flag2` 등)
   - 중첩이 과도하게 깊은 반복문/조건문, 조기 반환(early return)으로 단순화할 수 있는 부분
   - 중복 로직
   - 알고리즘/자료구조 선택이 문제 제약조건에 비해 비효율적인 경우 (간단히 언급만, 정답이 맞다면 필수 지적사항은 아님)

5. **결과 제시**: Spotless가 이미 반영한 포맷 변경은 "적용 완료"로 보고하고, Checkstyle 위반 + AI 리뷰 코멘트는 파일:라인 형태로 목록화해서 보여준다. **로직/네이밍 변경은 사용자 승인 없이 자동으로 고치지 않는다** — 사용자가 반영을 원하면 그때 Edit으로 수정한다.

## 하지 않는 것

- Checkstyle 위반이나 AI 리뷰 코멘트를 사용자 확인 없이 자동 수정하지 않는다 (포맷팅 제외).
- 다른 pg 폴더나 bj 폴더까지 확장해서 검사하지 않는다 — 대상은 항상 지정된(또는 최근 수정된) 폴더 하나.
- 문제를 다시 풀거나 정답 여부를 판단하지 않는다 (코드 스타일/가독성 리뷰만).
