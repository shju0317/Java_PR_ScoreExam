package com.biz.score.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.biz.score.config.DBContract;
import com.biz.score.config.Lines;
import com.biz.score.domain.ScoreVO;

public class ScoreServiceImplV1 implements ScoreService {
	private List<ScoreVO> scoreList;
	private Scanner scan;
	private String fileName;

	String[] strSubjects;
	Integer[] intScores;

	private int[] totalSum;
	private int[] totalAvg;
	
	FileWriter fileWriter = null;
	PrintWriter pWriter = null;

	public ScoreServiceImplV1() {
		scoreList = new ArrayList<ScoreVO>();
		scan = new Scanner(System.in);

		fileName = "src/com/biz/score/data/score.txt";
		strSubjects = new String[] { "국어", "영어", "수학", "음악" };

		intScores = new Integer[strSubjects.length];

		totalSum = new int[strSubjects.length];
		totalAvg = new int[strSubjects.length];
	}

	@Override
	public void loadScore() {
		FileReader fileReader = null;
		BufferedReader buffer = null;

		try {
			fileReader = new FileReader(fileName);
			buffer = new BufferedReader(fileReader);

			String reader = "";

			while (true) {
				reader = buffer.readLine();
				// System.out.println(reader);

				if (reader == null) {
					break;
				}

				String[] scores = reader.split(":");

				ScoreVO scoreVO = new ScoreVO();
				scoreVO.setStrNum(scores[DBContract.SCORE.SC_NUM]);
				scoreVO.setIntKor(Integer.valueOf(scores[DBContract.SCORE.SC_KOR]));
				scoreVO.setIntEng(Integer.valueOf(scores[DBContract.SCORE.SC_ENG]));
				scoreVO.setIntMath(Integer.valueOf(scores[DBContract.SCORE.SC_MATH]));
				scoreVO.setIntMusic(Integer.valueOf(scores[DBContract.SCORE.SC_MUSIC]));

				scoreList.add(scoreVO);
			}
			buffer.close();
			fileReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("\n※ score.txt 파일 열기 오류");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("\n※ score.txt 파일 읽기 오류");
		}

	}

	@Override
	public boolean inputScore() {
		ScoreVO scoreVO = new ScoreVO();

		System.out.print("학번(END:종료)>> ");
		String st_num = scan.nextLine();

		if (st_num.equalsIgnoreCase("END")) {
			return false;
		}

		int intNum = 0;
		try {
			intNum = Integer.valueOf(st_num);
		} catch (Exception e) {
			System.out.println("학번은 숫자만 가능");
			System.out.println("입력한 문자열: " + st_num);
			return true;
		}

		if (intNum < 1 || intNum > 99999) {
			System.out.println("학번은 1~99999까지만 가능");
			System.out.println("다시 입력해주세요 :)");
			return true;
		}

		st_num = String.format("%05d", intNum); // 00001 형식으로 만들기

		for (ScoreVO sVO : scoreList) {
			if (sVO.getStrNum().equals(st_num)) {
				System.out.println("\n>>" + st_num + " 학생의 성적이 이미 등록되어있습니다 :)");
				return true;
			}
		}

		scoreVO.setStrNum(st_num);

		for (int i = 0; i < strSubjects.length; i++) {
			System.out.printf("%s 점수(END:종료)>> ", strSubjects[i]);
			String sc_score = scan.nextLine();

			Integer intScore = this.scoreChk(sc_score);
			if (intScore == null) { // intScore = '-1, null, 숫자'
				i--;
				continue;
			} else if (intScore < 0) {
				return false;
			}

			// 모든 것이 정상이면 점수배열에 값을 저장하자
			intScores[i] = intScore;
		}

		scoreVO.setIntKor(intScores[0]);
		scoreVO.setIntEng(intScores[1]);
		scoreVO.setIntMath(intScores[2]);
		scoreVO.setIntMusic(intScores[3]);

		scoreList.add(scoreVO);
		this.saveScoreVO(scoreVO); // 1명의 데이터를 추가 저장하기
		return true;

	}

	@Override
	public Integer scoreChk(String sc_score) {

		if (sc_score.equalsIgnoreCase("END")) {
			return -1;
		}

		Integer intScore = null;
		try {
			intScore = Integer.valueOf(sc_score);
		} catch (Exception e) {
			System.out.println("점수는 숫자만 가능");
			System.out.println("입력한 문자열: " + sc_score);
			return null;
		}

		if (intScore < 0 || intScore > 100) {
			System.out.println("점수는 0~100까지만 가능");
			System.out.println("다시 입력해주세요 :)");
			return null;
		}

		return intScore;
	}

	@Override
	public void scoreList() {
		Arrays.fill(totalSum, 0);
		Arrays.fill(totalAvg, 0);

		System.out.println();
		System.out.println(Lines.dLine);
		System.out.println("성적 일람표");
		System.out.println(Lines.dLine);
		System.out.println("학번\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|");
		System.out.println(Lines.sLine);

		for (ScoreVO sVO : scoreList) {
			System.out.printf("%s\t|", sVO.getStrNum());
			System.out.printf("%d\t|", sVO.getIntKor());
			System.out.printf("%d\t|", sVO.getIntEng());
			System.out.printf("%d\t|", sVO.getIntMath());
			System.out.printf("%d\t|", sVO.getIntMusic());
			System.out.printf("%d\t|", sVO.getIntSum());
			System.out.printf("%5.2f\t|\n", sVO.getfAvg());

			totalSum[0] += sVO.getIntKor();
			totalSum[1] += sVO.getIntEng();
			totalSum[2] += sVO.getIntMath();
			totalSum[3] += sVO.getIntMusic();
		}
		System.out.println(Lines.sLine);

		System.out.print("총점:\t"); // 과목총점
		int sumAndSum = 0;
		for (int sum : totalSum) {
			System.out.printf("%5d\t|", sum);
			sumAndSum += sum;
		}
		System.out.printf("%s\t|\t|\n", sumAndSum);

		System.out.print("평균:\t"); // 과목평균
		float avgAndAvg = 0f;
		for (int sum : totalSum) {
			float avg = (float) sum / scoreList.size();
			System.out.printf("%5.2f\t|", avg);
			avgAndAvg += avg;
		}
		System.out.printf("\t|%5.2f\t|\n", avgAndAvg / totalSum.length);
		System.out.println(Lines.dLine);
	}

	@Override
	public void calcSum() {
		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getIntKor();
			sum += scoreVO.getIntEng();
			sum += scoreVO.getIntMath();
			sum += scoreVO.getIntMusic();

			scoreVO.setIntSum(sum);
		}
	}

	@Override
	public void calcAvg() {
		for (ScoreVO scoreVO : scoreList) {
			int sum = scoreVO.getIntSum();
			float avg = (float) sum / 4;

			scoreVO.setfAvg(avg);
		}
	}

	@Override
	public void saveScoreVO(ScoreVO scoreVO) {
		fileWriter = null;
		pWriter = null;

		try {
			fileWriter = new FileWriter(this.fileName, true);
			pWriter = new PrintWriter(fileWriter);

			pWriter.printf("%s:", scoreVO.getStrNum());
			pWriter.printf("%d:", scoreVO.getIntKor());
			pWriter.printf("%d:", scoreVO.getIntEng());
			pWriter.printf("%d:", scoreVO.getIntMath());
			pWriter.printf("%d\n", scoreVO.getIntMusic());

			pWriter.flush();
			pWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("\n※ 점수 저장 오류");
		}

	}

	@Override
	public void saveScoreList() {
		fileWriter = null;
		pWriter = null;
		
		calcSum();
		calcAvg();


		try {
			fileWriter = new FileWriter("src/com/biz/score/data/scoreList.txt");
			pWriter = new PrintWriter(fileWriter);

			Arrays.fill(totalSum, 0);
			Arrays.fill(totalAvg, 0);

			pWriter.println(Lines.dLine);
			pWriter.println("성적 일람표");
			pWriter.println(Lines.dLine);
			pWriter.println("학번\t|국어\t|영어\t|수학\t|음악\t|총점\t|평균\t|");
			pWriter.println(Lines.sLine);

			for (ScoreVO sVO : scoreList) {
				pWriter.printf("%s\t|", sVO.getStrNum());
				pWriter.printf("%d\t\t|", sVO.getIntKor());
				pWriter.printf("%d\t\t|", sVO.getIntEng());
				pWriter.printf("%d\t\t|", sVO.getIntMath());
				pWriter.printf("%d\t\t|", sVO.getIntMusic());
				pWriter.printf("%d\t|", sVO.getIntSum());
				pWriter.printf("%5.2f\t|\n", sVO.getfAvg());

				totalSum[0] += sVO.getIntKor();
				totalSum[1] += sVO.getIntEng();
				totalSum[2] += sVO.getIntMath();
				totalSum[3] += sVO.getIntMusic();
			}
			pWriter.println(Lines.sLine);

			pWriter.print("총점:\t"); // 과목총점
			int sumAndSum = 0;
			for (int sum : totalSum) {
				pWriter.printf("%5d\t|", sum);
				sumAndSum += sum;
			}
			pWriter.printf("%s\t|\t\t|\n", sumAndSum);

			pWriter.print("평균:\t"); // 과목평균
			float avgAndAvg = 0f;
			for (int sum : totalSum) {
				float avg = (float) sum / scoreList.size();
				pWriter.printf("%5.2f\t|", avg);
				avgAndAvg += avg;
			}
			pWriter.printf("\t\t|%5.2f\t|\n", avgAndAvg / totalSum.length);
			pWriter.println(Lines.dLine);
	
			pWriter.flush();
			pWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("\n※ scoreList.txt 저장 오류");
		}
		
		System.out.println("\n>> 성적일람표가 scoreList.txt 에 저장되었습니다 :)");
	}

}
