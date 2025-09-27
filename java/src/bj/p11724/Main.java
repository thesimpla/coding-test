package bj.p11724;

//https://www.acmicpc.net/problem/11724

import java.io.*;
import java.util.*;

// #11724
public class Main {
    public static void main(String[] args) throws IOException {
        // 정점, 간선 입력 받기
        // 입력받으면서 인접리스트 만들기
        // 간선의 개수 기준으로 visited 체크하기 - 모든 정점이 큐에 들어가야함.
        // 인접리스트를 큐에 넣어서 연결 요소 찾기

        // 입력을 위해 StringTokenizer, BufferedReader reader 사용하기

        int result =0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n+1; i++){
            graph.add(new ArrayList<>());
        }

        boolean[] visited = new boolean[n+1];

        for(int i = 0; i < m; i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        for(int i = 1; i < graph.size(); i++) {
            if(!visited[i]) {
                //bfs(i, graph, visited);
                dfs(i, graph, visited);

                result+=1;
            }
        }
        System.out.print(result);
    }

    public static void dfs(int i, List<List<Integer>> graph, boolean[] visited ) {
        visited[i] = true;
        for (int next : graph.get(i)) {
            if (!visited[next]) {
                dfs(next,graph, visited);
            }
        }
    }

    public static void bfs(int i, List<List<Integer>> graph, boolean[] visited ) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(i);
        visited[i] = true;
        while(!q.isEmpty()) {
            int cur = q.poll();

            for (int next : graph.get(cur)) {
                if (!visited[next]) {
                    q.offer(next);
                    visited[next] = true;
                }
            }
        }
    }
}
