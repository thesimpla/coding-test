package bj.p2606;
// https://www.acmicpc.net/problem/2606

import java.io.*;
import java.util.*;


public class Main {

    public static void main(String[] args) throws Exception {

        int result = 0;
        // num_com 받기
        // num_pair 받기
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int num_com = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int num_pair = Integer.parseInt(st.nextToken());

        //인접리스트 선언
        List<List<Integer>> graph = new ArrayList<>();


        for(int i = 0; i <= num_com; i++) {
            graph.add(new ArrayList<>());
        }

        //입력받은 pair로 인접리스트 값 채우기
        for (int i = 0; i < num_pair; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        // 인접리스트를 기반으로 탐색
        boolean[] checked = new boolean[num_com+1];
        Queue<Integer> q = new LinkedList<>();
        q.offer(1);
        checked[1] = true;

        // queue에서는 인접 리스트를 queue에 넣기 만약 checked가 false라면

        while(!q.isEmpty()) {
            int cur = q.poll();

            for(int next : graph.get(cur)) {
                if(!checked[next]) {
                    q.offer(next);
                    checked[next] = true;
                }
            }
        }

        for(boolean ch : checked) {
            //System.out.println("check="+ch);
            if(ch) {result+=1;}
        }

        System.out.print(result-1);
    }


}
