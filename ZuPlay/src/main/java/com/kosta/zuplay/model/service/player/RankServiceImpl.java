package com.kosta.zuplay.model.service.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.zuplay.model.dao.PlayerInfoDAO;
import com.kosta.zuplay.model.dto.player.PlayerDTO;

@Service
public class RankServiceImpl implements RankService{

	@Autowired
	private PlayerInfoService playerInfoService;
	
	@Autowired
	private EarningRateService earningRateService;
	
	@Autowired
	private SqlSession sqlSession;
	
	@Transactional
	@Override
	public void calRank(String kind) throws Exception {
		List<PlayerDTO> playerList2 = new ArrayList<PlayerDTO>();
		List<String> playerList = playerInfoService.getAllPlayerNickName();
		for(String playerNickname : playerList) {
			PlayerDTO playerDTO = playerInfoService.getPlayer(playerNickname);
			if(kind.equals("PLAYER_DAILY_RANK"))
				playerDTO.setEarningRate(earningRateService.calDailyEarningRate(playerNickname));
			else if(kind.equals("PLAYER_SEASON_RANK"))
				playerDTO.setEarningRate(earningRateService.calEarningRate(playerNickname));
			playerList2.add(playerDTO);
		}
		
		Collections.sort(playerList2, new EarningRateCompare());
		int i = 0;
		for(PlayerDTO playerDTO : playerList2) {
			i++;
			PlayerInfoDAO playerInfoDAO = sqlSession.getMapper(PlayerInfoDAO.class);
			Map<String, String> map = new HashMap<String, String>();
			map.put("playerNickname", playerDTO.getPlayerNickname());
			map.put("kind", kind);
			map.put("rank",Integer.toString(i));
			playerInfoDAO.rankUpdate(map);

		}
		System.out.println(kind + "삽입 완료");
	}

	
	
	class EarningRateCompare implements Comparator<PlayerDTO> {
		@Override
		public int compare(PlayerDTO p2, PlayerDTO p1) {
			return p1.getEarningRate() > p2.getEarningRate() ? 1 : p1.getEarningRate() < p2.getEarningRate() ? -1 : 0;
		}
	}

}
