package com.kosta.zuplay.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.kosta.zuplay.model.dto.player.FriendDTO;
import com.kosta.zuplay.model.dto.player.FriendVO;
import com.kosta.zuplay.model.service.item.ItemAuctionService;
import com.kosta.zuplay.model.service.player.FriendService;
import com.kosta.zuplay.util.vo.PlayerVO;

@Controller
public class FriendController {

	@Autowired
	private FriendService friendServiceImpl;

	@Autowired
	private ServletContext application;

	@Autowired
	private ItemAuctionService itemAuctionServiceImpl;

	public void friendLogin(String playerNickname, WebSocketSession webSession) {
		List<String> list = new ArrayList<>();
		WebSocketSession webSocketSession = null;
		boolean isAuctionFinish = true;
		try {
			isAuctionFinish = itemAuctionServiceImpl.itemAuctionEndSearch(playerNickname);
			list = friendServiceImpl.friendSelectOnlyNickname(playerNickname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonAuction = "{\"type\":\"notiIsAuctionFinish\",\"data\":\"" + isAuctionFinish + "\"}";
		String json = "{\"type\":\"notiFriendLogin\",\"data\":\"" + playerNickname + "\"}";
		TextMessage txAuction = new TextMessage(jsonAuction);
		TextMessage tx = new TextMessage(json);
		try {
			webSession.sendMessage(txAuction);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PlayerVO pv = null;
		for (int i = 0; i < list.size(); i++) {
			String playerNickname1 = list.get(i);
			pv = (PlayerVO) application.getAttribute("#" + playerNickname1);
			if (pv != null) {
				webSocketSession = pv.getSession();
				try {
					webSocketSession.sendMessage(tx);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void friendSelect(String playerNickname) {
		PlayerVO pv = (PlayerVO) application.getAttribute("#" + playerNickname);
		if (pv != null) {
			WebSocketSession webSession = pv.getSession();
			List<FriendVO> list = null;
			try {
				list = friendServiceImpl.friendSelect(playerNickname);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Gson gson = new Gson();
			String json = gson.toJson(list);
			json = "{\"type\":\"friendSelect\",\"data\": " + json + "}";
			TextMessage tx = new TextMessage(json);
			try {
				webSession.sendMessage(tx);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//
	// public void friendSelectOnline(String playerNickname) {
	// PlayerVO pv = (PlayerVO) application.getAttribute("#" + playerNickname);
	// WebSocketSession webSession = pv.getSession();
	// Gson gson = new Gson();
	// List<FriendDTO> list = null;
	// try {
	// list = friendServiceImpl.friendSelectOnline(playerNickname);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// String json = "{\"type\":\"friendSelectOnline\",\"data\":" +
	// gson.toJson(list) + "}";
	// TextMessage tx = new TextMessage(json);
	// try {
	// webSession.sendMessage(tx);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public void friendAdd(String playerNickname, String playerNickname2) {
		PlayerVO pv = (PlayerVO) application.getAttribute("#" + playerNickname2);
		WebSocketSession webSession = null;
		if (pv != null) {
			webSession = pv.getSession();
		}
		FriendDTO dto = null;
		dto = friendServiceImpl.friendAdd(playerNickname, playerNickname2);
		if (dto != null) {
			Gson gson = new Gson();
			String json = gson.toJson(dto);
			json = "{\"type\":\"notiFriendAdd\",\"data\":" + json + "}";
			TextMessage tx = new TextMessage(json);
			if (application.getAttribute("#" + playerNickname2) != null) {
				try {
					webSession.sendMessage(tx);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void friendDel(String playerNickname, int friendSq) {
		PlayerVO pv = (PlayerVO) application.getAttribute("#" + playerNickname);
		if (pv != null) {
			WebSocketSession webSession = pv.getSession();
			boolean result = false;
			result = friendServiceImpl.friendDel(friendSq);
			Gson gson = new Gson();
			String json = "{\"type\":\"friendDel\",\"data\":" + gson.toJson(result) + "}";
			TextMessage tx = new TextMessage(json);
			try {
				webSession.sendMessage(tx);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void friendAccept(String playerNickname, String playerNickname2, int friendSq) {
		PlayerVO pv = (PlayerVO) application.getAttribute("#" + playerNickname);
		PlayerVO pv2 = (PlayerVO) application.getAttribute("#" + playerNickname2);
		WebSocketSession webSession2 = null;
		if (pv2 != null) {
			webSession2 = pv2.getSession();
		}
		WebSocketSession webSession = null;
		if (pv != null) {
			webSession = pv.getSession();
		}

		boolean result = true;
		try {
			result = friendServiceImpl.friendAccept(friendSq);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result) {
			String json = "{\"type\":\"notiFriendAcceptMe\",\"data\":\"" + result + "\"}";
			String json2 = "{\"type\":\"notiFriendAcceptYou\",\"data\":\"" + playerNickname + "\"}";

			try {
				if (pv != null) {
					webSession.sendMessage(new TextMessage(json));
				}
				if (pv2 != null) {
					webSession2.sendMessage(new TextMessage(json2));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
