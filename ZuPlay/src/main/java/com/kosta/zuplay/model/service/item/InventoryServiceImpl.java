package com.kosta.zuplay.model.service.item;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.zuplay.model.dao.PlayerItemDAO;
import com.kosta.zuplay.model.dto.player.PlayerItemDTO;

@Service
public class InventoryServiceImpl implements InventoryService {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<PlayerItemDTO> playerItemSelectAll(String playerNickname) throws Exception {
		PlayerItemDAO dao = sqlSession.getMapper(PlayerItemDAO.class);
		return dao.playerItemSelectAll(playerNickname);
	}

	@Override
	public boolean playerItemInsert(List<PlayerItemDTO> list) throws Exception {
		PlayerItemDAO dao = sqlSession.getMapper(PlayerItemDAO.class);
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			result = dao.playerItemInsert(list.get(i));
		}
		if (result != 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<PlayerItemDTO> playerItemWorn(String playerNickname) throws Exception {
		PlayerItemDAO dao = sqlSession.getMapper(PlayerItemDAO.class);
		return dao.playerItemWorn(playerNickname);
		
	}

}
