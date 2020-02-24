package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;

@WebServlet("/aa")
public class BoardGen extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardDao dao = BoardDao.getInstance();
		for(int i = 0; i < 100; ++i) {
			BoardVo vo = new BoardVo();
			vo.setTitle( i + "��° �� ����!");
			vo.setContent( i + "��° �� ����!");
			vo.setWriterNum(1);
			dao.insert(vo);
		}
	}
}











