package com.kosta.zuplay.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.zuplay.model.dto.board.BoardCommentDTO;
import com.kosta.zuplay.model.dto.board.BoardDTO;
import com.kosta.zuplay.model.service.community.BoardService;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardServiceImpl;
	
	/**
	 * 게시글 작성 페이지 이동
	 */
	@RequestMapping("write")
	public String write(){
		return "writeBoard";
	}
	
	/**
	 * 게시판 글 작성
	 * @param dto
	 * @return
	 */
	@RequestMapping("insertBoard")
	public String insertBoard(HttpSession session,BoardDTO dto){
		dto.setPlayerNickname((String) session.getAttribute("playerNickname"));
		boardServiceImpl.insertBoard(dto);
		return "selectAll";
	}
	/**
	 * 게시판 글 수정
	 * @param dto
	 * @return
	 */
	@RequestMapping("updateBoard")
	@ResponseBody
	public boolean updateBoard(BoardDTO dto){
		return boardServiceImpl.updateBoard(dto);
	}
	/**
	 * 게시판 디테일뷰
	 * @param boardNo
	 * @return
	 */
	@RequestMapping("selectDetail")
	@ResponseBody
	public BoardDTO selectDetail(int boardNo){
		return boardServiceImpl.selectDetail(boardNo);
	}
	/**
	 * 게시판 리스트 뷰
	 * @return
	 */
	@RequestMapping("selectAll")
	public ModelAndView selectAll(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("selectAll");
		mv.addObject("list", boardServiceImpl.selectAll());
		return mv;
	}
	/**
	 * 게시판 글 삭제
	 * @param playerNickname
	 * @param boardNo
	 * @return
	 */
	@RequestMapping("deleteBoard")
	@ResponseBody
	public boolean deleteBoard(String playerNickname, int boardNo){
		return boardServiceImpl.deleteBoard(playerNickname, boardNo);
	}
	
	/**
	 * 코멘트 쓰기
	 * @param boardCommentDTO
	 * @return
	 */
	@RequestMapping("insertComment")
	@ResponseBody
	public boolean insertComment(BoardCommentDTO boardCommentDTO){
		return boardServiceImpl.insertComment(boardCommentDTO);
	}
	/**
	 * 코멘트 수정
	 * @param dto
	 * @return
	 */
	@RequestMapping("updateComment")
	@ResponseBody
	public boolean updateComment(BoardCommentDTO dto){
		return boardServiceImpl.updateComment(dto);
	}
	/**
	 * 코멘트 읽기
	 * @param boardNo
	 * @return
	 */
	@RequestMapping("selectComment")
	@ResponseBody
	public List<BoardCommentDTO> selectComment(int boardNo){
		return boardServiceImpl.selectComment(boardNo);
	}
	/**
	 * 코멘트 삭제
	 * @param playerNickname
	 * @param bcSq
	 * @return
	 */
	@RequestMapping("deleteComment")
	@ResponseBody
	public boolean deleteComment(String playerNickname,int bcSq){
		return boardServiceImpl.deleteComment(playerNickname, bcSq);
	}
}
