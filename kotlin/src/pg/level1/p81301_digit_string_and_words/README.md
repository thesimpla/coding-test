# 숫자 문자열과 영단어
# 링크 
https://school.programmers.co.kr/learn/courses/30/lessons/81301

## 접근
- (TODO: 풀이 후 작성)

## 결과
Solution1
테스트 1 〉	통과 (9.14ms, 62.3MB)
테스트 2 〉	통과 (10.20ms, 61.6MB)
테스트 3 〉	통과 (11.00ms, 61.1MB)
테스트 4 〉	통과 (12.32ms, 61.9MB)
테스트 5 〉	통과 (10.33ms, 62.2MB)
테스트 6 〉	통과 (16.42ms, 61.5MB)
테스트 7 〉	통과 (9.62ms, 60.9MB)
테스트 8 〉	통과 (10.05ms, 61.9MB)
테스트 9 〉	통과 (10.76ms, 62.6MB)
테스트 10 〉	통과 (11.54ms, 61.4MB)
Solution2
테스트 1 〉	통과 (9.07ms, 62.4MB)
테스트 2 〉	통과 (8.96ms, 62MB)
테스트 3 〉	통과 (11.54ms, 62.5MB)
테스트 4 〉	통과 (11.89ms, 62.1MB)
테스트 5 〉	통과 (9.70ms, 62.2MB)
테스트 6 〉	통과 (8.99ms, 62.5MB)
테스트 7 〉	통과 (9.34ms, 61.9MB)
테스트 8 〉	통과 (9.30ms, 61.9MB)
테스트 9 〉	통과 (8.91ms, 62.4MB)
테스트 10 〉	통과 (8.88ms, 62.4MB)

## 배운 점
- char가 숫자인지 문자인지 궁금할때 ascii 코드로 확인하지 않아도됨. kotlin은 isDigit(), isLetter()를 제공
- 중첩 개선 (kotlin의 api 사용) 
  - 중첩 깊이 감소 : 순수 가독성, 유지보수성 지표임. 
  - for-if문을 없앤 것. 알파벳 하나마다 map.keys를 순회하면 O(10), map은 해시 기반이라 O(1).
  - => 곧, "선형 탐색"->"해시 조회"라는 정석적인 성능 개선 패턴. 현재는 입력이 50이라 체감이 되지 않는 것.
  - => 반복문으로 값 찾기 대신 Set/Map의 contains/get 사용은 입력 문제에서 
    - 기존
      for (j in map.keys) {
          if (tmpString.joinToString("") == j) {
              arr.add(map[j]!!)
              tmpString.clear()
          }
      }
    - 변경
      if(map.keys.contains(tmpString.joinToString(""))) {
          val value = tmpString.joinToString("")
          arr.add(map[value]!!)
          tmpString.clear()
      }
