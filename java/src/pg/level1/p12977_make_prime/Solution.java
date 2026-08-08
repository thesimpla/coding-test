package pg.level1.p12977_make_prime;

class Solution {
    public int solution(int[] nums) {
        int answer = 0;

        int nums_len = nums.length;

        // 3개를 고르기
        for (int i = 0; i < nums_len - 2; i++) {
            for (int j = i + 1; j < nums_len - 1; j++) {
                for (int k = j + 1; k < nums_len; k++) {
                    int sum = nums[i] + nums[j] + nums[k];
                    boolean prime = true;
                    // System.out.println("sum("+sum+") is i="+i+" j="+j+" k="+k);
                    for (int a = 2; a * a <= sum; a++) {

                        if (sum % a == 0) {
                            // System.out.println("sum("+sum+") is not prime number. i="+i+" j="+j+"
                            // k="+k);
                            prime = false;
                            break;
                        }
                    }
                    if (prime) answer++;
                }
            }
        }

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        // System.out.println("answer is "+ answer);

        return answer;
    }
}
