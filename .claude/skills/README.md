# coding-test 프로젝트 스킬

프로그래머스 문제 풀이 자동화용 프로젝트 스킬 모음.
작업 디렉토리가 `coding-test` 안일 때만 보인다 (다른 프로젝트 세션에서는 안 뜸).

## 스킬 목록

| 스킬 | 언제 쓰나 | 호출 |
|---|---|---|
| [pg-create](pg-create/SKILL.md) | 문제 풀기 **시작할 때** | `/pg-create <url> <level> [java\|kotlin]` |
| [pg-test](pg-test/SKILL.md) | 제출했는데 **일부 실패/시간초과** 났을 때 | `/pg-test [폴더경로] [증상]` |
| [pg-check](pg-check/SKILL.md) | 다 풀고 **마무리**할 때 | `/pg-check [폴더경로]` |

### 전형적인 흐름

```
/pg-create <링크> <레벨>     # 폴더 + README + Solution 스켈레톤 + 공식 예제 SolutionTest 생성
      ↓
        직접 풀기
      ↓
/pg-test  <폴더> 시간초과     # (선택) 경계값·함정·성능 케이스 추가해서 원인 좁히기
      ↓
/pg-check <폴더>             # Spotless 포맷 + Checkstyle/detekt 린트 + AI 클린코드 리뷰
      ↓
        README 접근/복잡도 회고 작성 후 커밋
```

## 권장 모델 / effort

| 스킬 | 권장 | 이유 |
|---|---|---|
| `pg-create` | Sonnet, 기본 effort | 링크 읽고 템플릿 채우는 기계적 작업. 판단이 필요한 부분은 슬러그 작명 정도 |
| `pg-check` | Sonnet 기본 (리뷰 깊게 원하면 Opus) | 포맷·린트는 Gradle이 하고, AI가 추론하는 건 마지막 리뷰 단계뿐 |
| `pg-test` | **Opus + high effort** | 제약조건 해석 + 현재 풀이의 약점 분석 + 기대값 직접 계산까지, 추론 부담이 가장 큼. 기대값을 틀리게 만들면 없는 버그를 쫓게 되므로 손해가 큰 지점 |

모델 변경은 `/model`로 한다 (예: `/model claude-opus-5`).

## 언어별 파이프라인

`pg-check`가 언어를 경로로 판별해서 알아서 고른다.

| | java | kotlin |
|---|---|---|
| 포맷 | Spotless + google-java-format (AOSP, 4-space) | Spotless + ktlint |
| 린트 | Checkstyle (`java/config/checkstyle/checkstyle.xml`) | detekt (`kotlin/config/detekt/detekt.yml`) |
| 실행 | `cd java && ./gradlew spotlessApply checkstyleMain` | `cd kotlin && ./gradlew spotlessApply detekt` |

두 프로젝트 모두 `src/pg`만 대상으로 스코프를 잡았다 (`bj/`는 패키지 없는 `Main` 클래스들이라 한 sourceSet에 넣으면 충돌).

## 알아둘 것

- **`main()` 터미널 실행**: `./gradlew runMain -PmainClass=pg.level1.p42627_disk_controller.SolutionTestKt`
  (kotlin은 파일명 뒤에 `Kt`가 붙는다. IntelliJ에서는 `main` 옆 초록 ▶로 그냥 실행하면 됨)
- **`SolutionTest`는 린트 제외 대상**이다. 입출력 예를 그대로 박아둔 파일이라 기대값 리터럴이 전부 MagicNumber로 잡히기 때문. 포맷팅은 그대로 적용된다.
- **프로그래머스에서 코드 붙여넣을 때 `package` 선언이 날아가기 쉽다.** 그러면 같은 폴더의 `SolutionTest`가 `Solution`을 못 찾아서 컴파일이 깨진다. 첫 줄 확인할 것.
- **레벨은 자동으로 못 가져온다.** 문제 페이지가 SPA라 레벨 뱃지가 정적 HTML에 없어서, `/pg-create` 호출할 때 직접 넘겨야 한다. (제목·제한사항·함수 시그니처·입출력 예는 자동으로 가져옴)
