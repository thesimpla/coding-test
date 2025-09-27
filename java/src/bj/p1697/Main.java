package bj.p1697;

// https://www.acmicpc.net/problem/1697
import java.io.*;
import java.util.*;
import java.lang.*;


public class Main {
    static int cnt=1;
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int [] dim = new int[200003];
        boolean [] visited = new boolean[200003];
        Queue<Integer> q = new LinkedList<>();

        q.offer(n);
        dim[n] = 1;

        //큐가 비어있지 않으면
        //값을 poll 한다.
        //그 값을 bfs에 넣는다.


        while(!q.isEmpty()) {
            int cur = q.poll();

            //System.out.println("===================");
            //System.out.println("queue poll=> " + cur);
            if(cur == k) {
                break;
            }
            //System.out.println("check visited[cur]="+visited[cur]);

            bfs(cur, q, visited, dim);

            cnt+=1;
        }

        System.out.print((dim[k]-1));
    }

    static void bfs(int cur, Queue<Integer> q, boolean[] visited, int[] dim) {

        // start에서 갈 수 있는 값 얻기
        // 방문하지 않았다면 방문한다.
        // 방문한 뒤, 차수 및 visited를 업데이트한다.
        // 차수 값은 cur이 가진 값의 차수 +1을 한다.
        int[] list = {cur-1, cur+1, cur*2};

        //System.out.println("bfs(cur="+cur+") / dim="+dim[cur]);
        for (int i : list) {
            //q.offer(i);

            if (i>=0 && i <=100000)
                if (!visited[i]) {
                    //System.out.println("add("+i+")");
                    q.offer(i);
                    dim[i] = dim[cur] + 1;
                    visited[i] = true;
                }

        }

    }

}