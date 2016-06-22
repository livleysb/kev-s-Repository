package com.kosta.zuplay.model.dto.board;

public class BoardDTO {
	int boardNo;			//κ²μκΈ? λ²νΈ
	String playerNickname;	//??€?
	String boardTitle;		//κΈ?? λͺ?
	int boardLike;			//μ’μ? κ°??
	String boardContent;	//λ³Έλ¬Έ?΄?©
	String boardTime;		//???κ°?
	int boardHits;			//μ‘°ν?
	
	public BoardDTO() {}
	public BoardDTO(int boardNo, String playerNickname, String boardTitle, int boardLike, String boardContent,
			String boardTime, int boardHits) {
		super();
		this.boardNo = boardNo;
		this.playerNickname = playerNickname;
		this.boardTitle = boardTitle;
		this.boardLike = boardLike;
		this.boardContent = boardContent;
		this.boardTime = boardTime;
		this.boardHits = boardHits;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getPlayerNickname() {
		return playerNickname;
	}
	public void setPlayerNickname(String playerNickname) {
		this.playerNickname = playerNickname;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public int getBoardLike() {
		return boardLike;
	}
	public void setBoardLike(int boardLike) {
		this.boardLike = boardLike;
	}
	public String getBoardContent() {
		return boardContent;
	}
	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}
	public String getBoardTime() {
		return boardTime;
	}
	public void setBoardTime(String boardTime) {
		this.boardTime = boardTime;
	}
	public int getBoardHits() {
		return boardHits;
	}
	public void setBoardHits(int boardHits) {
		this.boardHits = boardHits;
	}
	@Override
	public String toString() {
		return "BoardDTO [boardNo=" + boardNo + ", playerNickname=" + playerNickname + ", boardTitle=" + boardTitle
				+ ", boardLike=" + boardLike + ", boardContent=" + boardContent + ", boardTime=" + boardTime
				+ ", boardHits=" + boardHits + "]";
	}
	
	
}
