package com.biz.score.exec;

import java.util.Scanner;

import com.biz.score.config.Lines;
import com.biz.score.service.ScoreService;
import com.biz.score.service.ScoreServiceImplV1;

public class scoreEx_01 {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		ScoreService scoreService = new ScoreServiceImplV1();

		scoreService.loadScore();

		while (true) {
			System.out.println();
			System.out.println(Lines.dLine);
			System.out.println("♣ 빛고을 대학 학사관리 시스템 V1 ♣");
			System.out.println(Lines.dLine);
			System.out.println("1. 성적 등록");
			System.out.println("2. 성적 일람표 출력");
			System.out.println("3. 성적 일람표 txt파일 저장");
			System.out.println(Lines.sLine);
			System.out.println("QUIT. 업무종료");
			System.out.println(Lines.dLine);
			System.out.print("업무선택>> ");

			String stMenu = scan.nextLine();
			if (stMenu.equalsIgnoreCase("QUIT")) {
				break;
			}

			int intMenu = 0;
			try {
				intMenu = Integer.valueOf(stMenu);
			} catch (Exception e) {
				System.out.println("메뉴는 숫자로만 선택 가능");
				continue;
			}

			if (intMenu == 1) {
				while(scoreService.inputScore()) {
						while (scoreService.inputScore());

						scoreService.calcSum();
						scoreService.calcAvg();
				}
			} else if (intMenu == 2) {
				scoreService.calcSum();
				scoreService.calcAvg();
				scoreService.scoreList();
				
			}else if(intMenu == 3){
				scoreService.saveScoreList();
			}else {
				System.out.println("\n>> 해당하는 업무가 없습니다");
			}
			
		}
		System.out.println("\n>> 업무종료 :)");
	}
}
