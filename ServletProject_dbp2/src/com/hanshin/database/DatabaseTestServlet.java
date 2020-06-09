package com.hanshin.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/databaseTest")
public class DatabaseTestServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>Test</title></head>");
		out.print("<body><h3>Database �����<h3><body>");
		
		String userName = "";
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://localhost:3306/databasetest?serverTimezone=UTC";
		try {
			Class.forName(jdbc_driver).newInstance();
			Connection con = DriverManager.getConnection(jdbc_url, "root", "gsy022774");
			Statement st = con.createStatement();
			
			String sql = "SELECT * FROM databasetest.member";
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				userName = rs.getString("username");
				System.out.printf("title: %s\n", userName);
				out.print("<br>" + userName);
			} 
			
			rs.close();
			st.close();
			con.close();			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		out.print("</html>");
		out.close();
	}	
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = resp.getWriter();
		out.print("<html><head><title>Test</title></head>");
		out.print("<body><h3>Database �����<h3><body>");
		
		String id = req.getParameter("id");
		String password = req.getParameter("pwd");
		String username = req.getParameter("name");
		String tel = req.getParameter("tel");
		String email = req.getParameter("email");
		String[] depts = req.getParameterValues("dept");
		String dept = Arrays.toString(depts);
		String gender = req.getParameter("gender");
		String birth = req.getParameter("birth");
		String intro = req.getParameter("introduction");
		
		String button = req.getParameter("btn");
		
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://localhost:3306/databasetest?serverTimezone=UTC";
		try {
			Class.forName(jdbc_driver).newInstance();
			Connection con = DriverManager.getConnection(jdbc_url, "root", "gsy022774");
			Statement st = con.createStatement(); 
			PreparedStatement pst = null;
			ResultSet rs = null;
			String sql = null;
			
			if(button.equals("DB����")) {
				System.out.println("db�����ư�� ������..");
				sql = "SELECT * FROM databasetest.member";
	            rs = st.executeQuery(sql);
	            out.print("<html><head><title>post</title></head>");
				out.print("<body>");
				out.print("<h1>������Ʈ ����!</h1>");
	            while(rs.next()) {
					out.print("ID : " + rs.getString("id") );
					out.print(" �̸� : " + rs.getString("name"));
					out.print(" ��ȭ��ȣ : " + rs.getString("tel"));
					out.print(" email : " + rs.getString("email"));
					out.print(" �к� : " + rs.getString("dept"));
					out.print(" ���� : " + rs.getString("gender"));
					out.print(" birth : " + rs.getString("birth"));
					out.print(" �Ұ� : " + rs.getString("intro"));
					out.print("<br/>");
	            }
	            out.println("</body></html>");
	            
			} else if(button.equals("DB����")) {
				System.out.println("db������ư�� ������..");
				sql = "DELETE FROM databasetest.member";
				
				st.executeUpdate(sql);
				out.print("<h1>DB���� ����!</h1>");
				
			} else if(button.equals("����")) {
				//����ڰ� �Է��� �����Ͱ� �̹� db�� id && �̸��� ���� �����Ͱ� �����Ҷ�
				sql = "SELECT * FROM databasetest.member";
	            rs = st.executeQuery(sql);
	            int isrewrite = 0;
	            while(rs.next()) {
	            	System.out.println("select_id " + rs.getString("id")+ " id " + id + " " +rs.getString("name") + " " + username);
	            	if(id.equals(rs.getString("id")) && username.equals(rs.getString("name"))) {
	            		System.out.println("id�� username�� ����.");
	            		if(password.equals(rs.getString("pwd")) != true) {	//3.1) ��й�ȣ�� �ٸ��� ȭ�鿡 ��й�ȣ�� �ٸ��ٴ� �޽��� ���
	            			System.out.println("��й�ȣ�� �ٸ�.");
	            			System.out.println("pw " + password + " " + rs.getString("pwd"));
	            			
	            			out.print("��й�ȣ�� �ٸ��ϴ�. <br/>");
	            			break;
	            		} else if(password.equals(rs.getString("pwd"))) {
	            			System.out.println("��й�ȣ�� ����.");
	            			sql = "UPDATE databasetest.member SET tel=?, email=?, dept=?, gender=?, birth=?, intro=? WHERE id = ?";
	            			pst = con.prepareStatement(sql); // ������ ����
	            			pst.setString(1, tel);
	        				pst.setString(2, email);
	        				pst.setString(3, dept);
	        				pst.setString(4, gender);
	        				pst.setString(5, birth);
	        				pst.setString(6, intro);
	        				pst.setString(7, id);
	        				pst.executeUpdate(); // ������ ����
	        				isrewrite = 1;
	        				
	        				break;
	            		}
	            	}
	            	
	            	
	            }
	            
				if (isrewrite == 1) { //id�� name�� ����, pwd�� �������� ��°�
					// member���̺� �Էµ� ���� ȭ�鿡 ���
					out.print("<html><head><title>post</title></head>");
					out.print("<body>");
					out.print("<h1>������Ʈ ����!</h1>");

					out.print("ID : " + id + "<br/>");
					out.print("�̸� : " + username + "<br/>");
					out.print("��ȭ��ȣ : " + tel + "<br/>");
					out.print("email : " + email + "<br/>");
					out.print("�к� : ");
					for (int i = 0; i < depts.length; i++) {
						out.print(depts[i] + " ");
					}
					out.print("<br/>");
					out.print("���� : " + gender + "<br/>");
					out.print("birth : " + birth + "<br/>");
					out.print("�Ұ� : " + intro + "<br/>");
					out.println("</body></html>");
				} else {	//���� id�� name�� �������� ������ ����

					//[������ �Է��Ҷ�]
					//id, password, username, tel, email, dept, genger, birth, intro
					sql = "INSERT INTO databasetest.member VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
					pst = con.prepareStatement(sql);
					
					pst.setString(1, id);
					pst.setString(2, password);
		            pst.setString(3, username);
		            pst.setString(4, tel);
		            pst.setString(5, email);
		            pst.setString(6, dept);
		            pst.setString(7, gender);
		            pst.setString(8, birth);
		            pst.setString(9, intro);
		            pst.executeUpdate();//���� ����

					// �Էµ� ������ ���
					sql = "SELECT * FROM databasetest.member";
					rs = st.executeQuery(sql);
					// member���̺��� ��� ������ �ܼ�â�� ��� - �׽�Ʈ��
					while (rs.next()) {
						System.out.println("id: " + rs.getString("id") + " pwd: " + rs.getString("pwd") + " name: "
								+ rs.getString("name") + " tel: " + rs.getString("tel") + " email: " + rs.getString("email")
								+ " dept: " + rs.getString("dept") + " gender: " + rs.getString("gender") + " birth: "
								+ rs.getString("birth") + " intro: " + rs.getString("intro"));
					}
					// member���̺� �Էµ� ���� ȭ�鿡 ���
					out.print("<html><head><title>post</title></head>");
					out.print("<body>");
					out.print("<h1>POST������� ���� �����߽��ϴ�!</h1>");

					out.print("ID : " + id + "<br/>");
					out.print("�̸� : " + username + "<br/>");
					out.print("��ȭ��ȣ : " + tel + "<br/>");
					out.print("email : " + email + "<br/>");
					out.print("�к� : ");
					for (int i = 0; i < depts.length; i++) {
						out.print(depts[i] + " ");
					}
					out.print("<br/>");
					out.print("���� : " + gender + "<br/>");
					out.print("birth : " + birth + "<br/>");
					out.print("�Ұ� : " + intro + "<br/>");
					out.println("</body></html>");
					
				}
			}
			

			rs.close();
			st.close();
			con.close();			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		
		out.print("</html>");
		out.close();
	}
}