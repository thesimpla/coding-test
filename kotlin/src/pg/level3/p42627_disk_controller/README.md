# 디스크 컨트롤러
# 링크 
https://school.programmers.co.kr/learn/courses/30/lessons/42627

## 접근
- 우선순위 큐로 (소요시간 짧은 순 → 요청 시각 빠른 순 → 번호 작은 순) 비교해서 대기 작업 중 우선순위가 가장 높은 걸 선택
- 큐가 비어있는 유휴 구간에서는 `currentTime`을 다음 처리 가능 시각까지 흘려보내야 함

## 실수
- 유휴 구간 처리를 빼먹어서 무한루프에 빠짐 — 큐가 비어있을 때 `currentTime`을 그대로 두면 다음 작업이 영원히 안 들어옴
- 작업 처리 여부를 입력 배열 자체에 `-1`로 표시했다가 배열이 훼손됨 → `visitedJobs: BooleanArray`처럼 입력을 건드리지 않는 별도 상태로 관리해야 함
- 디버깅용 `println`을 루프 안에 남겨뒀다가 큰 입력에서 시간초과 유발

## 새로 안 것
- `PriorityQueue`의 다중 키 비교 문법: `compareBy<T> { }.thenBy { }.thenBy { }`

## 복잡도
- 시간: O(n² log n) — 매 처리마다 남은 작업을 다시 스캔(O(n))하고 우선순위 큐에 넣음(O(log n))
- 공간: O(n)

## 클린코드 리뷰
- for 문 kotlin 답게 쓰려면 for (i in jobs.indices) 또는 for(i in 0 until sizeOfJobs)
- Array<Int>로 [인덱스, 요청시각, 소요시간] 3개를 묶는거보다 Pair/Triple이나 data class가 더 명확함. 
(Array<Int>는 equals/toString이 구조 비교를 안해줌)
- taTime, rqTime 축약이 애매함. duration, requestTime 정도로 명확하게