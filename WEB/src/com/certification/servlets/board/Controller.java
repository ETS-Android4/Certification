package com.certification.servlets.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.brd")
// /myhome/~~~~~~~~.brd �� ������ ��� ��û

/*
 * < �Խ��� >
 * 1. �۾��� 
 *  /myhome/board/write.brd
 * 
 * 2. �۸�Ϻ��� 
 *  /myhome/board/list.brd
 * 
 * 3. �ۻ���
 *  /myhome/board/delete.brd?num=�����ұ۹�ȣ
 * 
 * 4. �ۼ���
 *  /myhome/board/modify.brd?num=�����ұ۹�ȣ
 *  
 *  
 * 5. ��������
 *  /myhome/board/read.brd?num=�����۹�ȣ
 * 
 */




public class Controller extends HttpServlet{
	
	// service : post, get ���� ��� ��û ������� ����
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("��Ʈ�ѷ��� service() ����!!");
		
		String uri = ((HttpServletRequest)request).getRequestURI();
		System.out.println("��û�� �ּ� : " + uri);
		
		
		// ��û Ű���� ���� (write, list, read, delete, modify)
		String keyword = uri.replaceAll("/certification/board/|.brd", "");
		System.out.println(keyword);
		
		
		// (�ؿ� ������ Ű����(list, write, delete..)�� �ٸ� ��û�� ���� ��� 
		// "/myhome" �ε��� �������� �����̷�Ʈ�� �� �ֵ��� �⺻��� ����
		NextAction next = new NextAction("/", true);
		
		if("list".equals(keyword)) {
			new ListAction().execute(request, response); // <--- DB ��ȸ
			next.setNextPath("/board/listView.jsp"); // <--- �ٷ� ������ �� ���
			next.setRedirect(false); // ������
		}
		else if("write".equals(keyword)) {
			next.setNextPath("/board/writeView.jsp");
			next.setRedirect(true);
		}
		else if("writeAction".equals(keyword)) {
			request.setCharacterEncoding("utf-8");
			new WriteAction().execute(request, response);
			next.setNextPath("/board/resultView.jsp");
			next.setRedirect(false);
		}
		else if("delete".equals(keyword)) {
			new DeleteAction().execute(request, response);
			next.setNextPath("/board/resultView.jsp");
			next.setRedirect(false);
		}
		
		////////////// ���� ��� ////////////// 
		else if("modify".equals(keyword)) {
			new ReadAction().execute(request, response); // �������� �������� ����
			next.setNextPath("/board/modifyView.jsp");  // ���� form����
			next.setRedirect(false);
		}
		else if("modifyAction".equals(keyword)) {
			new ModifyAction().execute(request, response); // ���� action ����
			next.setNextPath("/board/resultView.jsp");  // ��� ��������
			next.setRedirect(false);
		}
		//////////////���� ��� �� ////////////// 
		
		
		else if("read".equals(keyword)) {
			new ReadAction().execute(request, response);
			next.setNextPath("/board/readView.jsp");
			next.setRedirect(false);
		}
		else if("writeReply".equals(keyword)) {
			new WriteReplyAction().execute(request, response);
			next.setNextPath("/board/read.brd?num=" + request.getParameter("boardNum"));
			next.setRedirect(false);
		}
		
		if(next.isRedirect()) { // �����̷�Ʈ?
			response.sendRedirect( "/certification" + next.getNextPath() );
		}
		else { // ������?
			request.getRequestDispatcher( next.getNextPath() )
					.forward(request, response);
		}
	}
}








