plugins {
    kotlin("jvm") version "2.0.21"
    id("com.diffplug.spotless") version "6.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

repositories {
    mavenCentral()
}

// bj/ 문제들이 나중에 추가되면 java 쪽과 마찬가지로 패키지 없는 파일들이 충돌할 수 있으므로,
// pg/ 문제 풀이만 포맷/린트 대상으로 삼는다.
kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf("src/pg"))
        }
    }
}

spotless {
    kotlin {
        target("src/pg/**/*.kt")
        // 네이밍 위반은 자동수정이 안 돼서 ktlint가 빌드를 그냥 죽여버린다(스택트레이스만 남음).
        // 네이밍/매직넘버 등은 detekt가 깔끔하게 리포트하므로, ktlint는 순수 포맷팅만 담당하게
        // 네이밍 관련 룰과 p{문제번호}_{슬러그} 컨벤션과 충돌하는 룰만 끈다.
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_package-name" to "disabled",
                "ktlint_standard_property-naming" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_class-naming" to "disabled",
                // 주석 위치 규칙도 자동수정이 안 돼서 빌드를 죽인다.
                // 풀이 코드에 설명 주석을 자유롭게 다는 게 더 중요하므로 끈다.
                "ktlint_standard_value-argument-comment" to "disabled",
                "ktlint_standard_value-parameter-comment" to "disabled",
                "ktlint_standard_discouraged-comment-location" to "disabled",
            )
        )
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(file("config/detekt/detekt.yml"))
    source.setFrom(files("src/pg"))
}

// SolutionTest는 입출력 예를 그대로 박아둔 로컬 확인용 파일이라 매직넘버 등이 잔뜩 잡힌다.
// 린트 대상은 실제 제출하는 Solution 코드로 한정한다.
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/SolutionTest.kt")
}

// 특정 문제의 main()을 터미널에서 실행한다.
// 예) ./gradlew runMain -PmainClass=pg.level1.p42627_disk_controller.SolutionTestKt
tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Runs a main() by fully qualified class name (-PmainClass=...)"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set(providers.gradleProperty("mainClass"))
}
