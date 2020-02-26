package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.certification.beans.AccountDao;
import com.certification.beans.AccountVo;
import com.certification.beans.BoardDao;
import com.certification.beans.BoardVo;
import com.certification.beans.ReplyDao;

public class ReadAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1. num �Ķ���� �ޱ� 
		String s = request.getParameter("num");
		int num = Integer.parseInt(s);
		
		// 2. Dao �� ���Ͽ� DB���� num�� �Խñ� ������ �ޱ� ( vo�� ���Ϲ��� ) 
		BoardDao dao = BoardDao.getInstance();
		
		
		Cookie[] cookies = request.getCookies();
		boolean check = false;
		for(Cookie c : cookies) {
			if(c.getName().equals( String.valueOf(num) ) ){
				check = true;
				break;
			}
		}
		
		// �� ������ �� ���� ó���ô�?
		if(!check) {
			Cookie cookie = new Cookie(String.valueOf(num), "");
			response.addCookie(cookie);
			dao.updateHit(num);
		}
		
		BoardVo vo = dao.select(num);
		
		// 3. writerNum ---> �ۼ��� �г��� �ޱ�
		AccountDao dao2 = AccountDao.getInstance();
		AccountVo vo2 = dao2.select(vo.getWriterNum());
		vo.setWriter( vo2.getNickname() );
		
		// 4. vo �� request �ٱ��Ͽ� ��� 
		request.setAttribute("vo", vo);
		request.setAttribute("replyList", ReplyDao.getInstance().selectList(num));
	}
}
