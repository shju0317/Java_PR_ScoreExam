package com.biz.score.service;

import com.biz.score.domain.ScoreVO;

public interface ScoreService {
	public void loadScore();
	public boolean inputScore();
	public Integer scoreChk(String sc_score);
	public void calcSum();
	public void calcAvg();
	public void scoreList();
	public void saveScoreVO(ScoreVO scoreVO);
	public void saveScoreList();
}
