package com.kosta.zuplay.model.service.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.zuplay.model.dao.ItemAuctionDAO;
import com.kosta.zuplay.model.dao.ItemStoreDAO;
import com.kosta.zuplay.model.dto.item.ItemDTO;
import com.kosta.zuplay.model.dto.item.ItemMarketDTO;
import com.kosta.zuplay.model.dto.player.PlayerItemDTO;
import com.kosta.zuplay.model.service.UtilService;

@Service
public class ItemAuctionServiceImpl implements ItemAuctionService {

	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private UtilService utilServiceImpl;

	/**
	 * search item in item auction
	 * 
	 * @param keyword
	 *            : What you want to search
	 * @param itemClass
	 *            : If you want to search for some parts
	 * @param page
	 *            : eight units in one page
	 */
	@Override
	public List<ItemMarketDTO> auctionSearch(String keyword, String itemClass, int page) {
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("keyword", keyword);
		map.put("itemClass", itemClass);
		map.put("startNo", 1 + ((page - 1) * 10) + "");
		map.put("endNo", page * 10 + "");
		return itemAuctionDAO.auctionSearch(map);
	}

	/**
	 * buy item in item auction return int(1=success/2=not enough
	 * inventory/3=not enough Ruby/4=sold out item)
	 */
	@Override
	@Transactional
	public int auctionBuy(String playerNickname, String imSq) {
		ItemStoreDAO itemStoreDAO = sqlSession.getMapper(ItemStoreDAO.class);
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		Map<String, String> payRubyMap = new HashMap<String, String>();
		int ruby = itemStoreDAO.getRuby(playerNickname); // 보유루비 확인
		System.out.println("[ LOG ] : " + playerNickname + " 님의 루비 = " + ruby);
		int price = itemAuctionDAO.auctionHowPrice(imSq);
		if (ruby > price) {
			int piIndex = utilServiceImpl.indexSearch(playerNickname);
			System.out.println("[ LOG ] : " + playerNickname + " 님의 인벤토리 빈 인덱스 = " + piIndex);
			if (piIndex != 0) {
				payRubyMap.put("playerNickname", playerNickname);
				payRubyMap.put("price", price + "");
				itemStoreDAO.payRuby(payRubyMap);
				int payRubyResult = itemStoreDAO.payRuby(payRubyMap);
				if (payRubyResult != 0) {
					ItemDTO itemDTO = itemAuctionDAO.bringItemInfoByImSq(imSq);
					int insertResult = itemAuctionDAO.auctionInsertPlayerItem(
							new PlayerItemDTO(0, playerNickname, null, null, piIndex, itemDTO));
					if (insertResult != 0) {
						itemAuctionDAO.auctionBuyFinish(imSq);
					} else {
						System.out.println("[ LOG ] : 이미 판매된 물품");
						return 4;
					}
				} else {
					System.out.println("[ LOG ] : 루비 빼기 실패");
					return 4;
				}
			} else {
				System.out.println("[ LOG ] : 인벤토리 부족");
				return 2;
			}
		} else {
			System.out.println("[ LOG ] : 루비부족");
			return 3;
		}
		System.out.println("[ LOG ] : 구매 성공");
		return 1;
	}

	/**
	 * sell item in item auction return boolean(true=success/fail=fail)
	 * 
	 */
	@Override
	@Transactional
	public boolean auctionSell(String playerNickname, String piSq, int imPurchasePrice) {
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		ItemDTO itemDTO = itemAuctionDAO.bringItemInfoByPiSq(piSq);
		Map<String, String> map = new HashMap<String,String>();
		map.put("playerNickname", playerNickname);
		map.put("imPurchasePrice", imPurchasePrice+"");
		map.put("itemCode", itemDTO.getItemCode());
		int insertResult = itemAuctionDAO.auctionInsertItemMarket(map);
		if (insertResult != 0) {
			int deleteResult = itemAuctionDAO.auctionDeletePlayerItem(piSq);
			if (deleteResult == 0) {
				System.out.println("[ LOG ] : player_item 레코드 삭제 실패하였습니다.");
			}
		} else {
			System.out.println("[ LOG ] : 경매장에 아이템 등록을 실패하였습니다.");
		}
		return false;
	}

	/**
	 * 경매장 경매 취소
	 */
	@Override
	public boolean auctionCancel(String imSq) {
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		int result = itemAuctionDAO.auctionCancel(imSq);
		if (result == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 경매장 유찰품/골드 수령 IM_AUCTION_END 진행중 여부 (T=진행중/F=종료/X=유찰)
	 */
	@Override
	@Transactional
	public boolean auctionBring(String playerNickname, String imSq) {
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		ItemStoreDAO itemStoreDAO = sqlSession.getMapper(ItemStoreDAO.class);
		String imAuctionEnd = itemAuctionDAO.auctionBring(imSq);
		if (imAuctionEnd.equals("F")) {
			Map<String, String> map = new HashMap<String, String>();
			int price = itemAuctionDAO.auctionHowPrice(imSq);
			map.put("playerNickname", playerNickname);
			map.put("price", -price + "");
			itemStoreDAO.payRuby(map);
		} else if (imAuctionEnd.equals("X")) {
			ItemDTO itemDTO = itemAuctionDAO.bringItemInfoByImSq(imSq);
			int piIndex = utilServiceImpl.indexSearch(playerNickname);
			if (piIndex != 0) {
				itemAuctionDAO.auctionInsertPlayerItem(new PlayerItemDTO(0, playerNickname, null, null, piIndex, itemDTO));
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 내 경매 물품 가져오기
	 */
	@Override
	public List<ItemMarketDTO> auctionMyPage(String playerNickname) {
		ItemAuctionDAO itemAuctionDAO = sqlSession.getMapper(ItemAuctionDAO.class);
		return itemAuctionDAO.auctionMyPage(playerNickname);
	}

}