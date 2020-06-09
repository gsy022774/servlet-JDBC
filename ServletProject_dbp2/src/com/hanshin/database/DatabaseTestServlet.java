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
		out.print("<body><h3>Database 결과값<h3><body>");
		
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
		out.print("<body><h3>Database 결과값<h3><body>");
		
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
			
			if(button.equals("DB보기")) {
				System.out.println("db보기버튼이 눌러짐..");
				sql = "SELECT * FROM databasetest.member";
	            rs = st.executeQuery(sql);
	            out.print("<html><head><title>post</title></head>");
				out.print("<body>");
				out.print("<h1>업데이트 성공!</h1>");
	            while(rs.next()) {
					out.print("ID : " + rs.getString("id") );
					out.print(" 이름 : " + rs.getString("name"));
					out.print(" 전화번호 : " + rs.getString("tel"));
					out.print(" email : " + rs.getString("email"));
					out.print(" 학부 : " + rs.getString("dept"));
					out.print(" 성별 : " + rs.getString("gender"));
					out.print(" birth : " + rs.getString("birth"));
					out.print(" 소개 : " + rs.getString("intro"));
					out.print("<br/>");
	            }
	            out.println("</body></html>");
	            
			} else if(button.equals("DB삭제")) {
				System.out.println("db삭제버튼이 눌러짐..");
				sql = "DELETE FROM databasetest.member";
				
				st.executeUpdate(sql);
				out.print("<h1>DB삭제 성공!</h1>");
				
			} else if(button.equals("전송")) {
				//사용자가 입력한 데이터가 이미 db에 id && 이름이 같은 데이터가 존재할때
				sql = "SELECT * FROM databasetest.member";
	            rs = st.executeQuery(sql);
	            int isrewrite = 0;
	            while(rs.next()) {
	            	System.out.println("select_id " + rs.getString("id")+ " id " + id + " " +rs.getString("name") + " " + username);
	            	if(id.equals(rs.getString("id")) && username.equals(rs.getString("name"))) {
	            		System.out.println("id와 username이 같음.");
	            		if(password.equals(rs.getString("pwd")) != true) {	//3.1) 비밀번호가 다르면 화면에 비밀번호가 다르다는 메시지 출력
	            			System.out.println("비밀번호가 다름.");
	            			System.out.println("pw " + password + " " + rs.getString("pwd"));
	            			
	            			out.print("비밀번호가 다릅니다. <br/>");
	            			break;
	            		} else if(password.equals(rs.getString("pwd"))) {
	            			System.out.println("비밀번호가 같음.");
	            			sql = "UPDATE databasetest.member SET tel=?, email=?, dept=?, gender=?, birth=?, intro=? WHERE id = ?";
	            			pst = con.prepareStatement(sql); // 쿼리문 저장
	            			pst.setString(1, tel);
	        				pst.setString(2, email);
	        				pst.setString(3, dept);
	        				pst.setString(4, gender);
	        				pst.setString(5, birth);
	        				pst.setString(6, intro);
	        				pst.setString(7, id);
	        				pst.executeUpdate(); // 쿼리문 실행
	        				isrewrite = 1;
	        				
	        				break;
	            		}
	            	}
	            	
	            	
	            }
	            
				if (isrewrite == 1) { //id와 name이 같고, pwd도 같았을때 출력값
					// member테이블에 입력된 값을 화면에 출력
					out.print("<html><head><title>post</title></head>");
					out.print("<body>");
					out.print("<h1>업데이트 성공!</h1>");

					out.print("ID : " + id + "<br/>");
					out.print("이름 : " + username + "<br/>");
					out.print("전화번호 : " + tel + "<br/>");
					out.print("email : " + email + "<br/>");
					out.print("학부 : ");
					for (int i = 0; i < depts.length; i++) {
						out.print(depts[i] + " ");
					}
					out.print("<br/>");
					out.print("성별 : " + gender + "<br/>");
					out.print("birth : " + birth + "<br/>");
					out.print("소개 : " + intro + "<br/>");
					out.println("</body></html>");
				} else {	//같은 id와 name이 없엇을때 데이터 삽입

					//[데이터 입력할때]
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
		            pst.executeUpdate();//쿼리 실행

					// 입력된 데이터 출력
					sql = "SELECT * FROM databasetest.member";
					rs = st.executeQuery(sql);
					// member테이블의 모든 데이터 콘솔창에 출력 - 테스트용
					while (rs.next()) {
						System.out.println("id: " + rs.getString("id") + " pwd: " + rs.getString("pwd") + " name: "
								+ rs.getString("name") + " tel: " + rs.getString("tel") + " email: " + rs.getString("email")
								+ " dept: " + rs.getString("dept") + " gender: " + rs.getString("gender") + " birth: "
								+ rs.getString("birth") + " intro: " + rs.getString("intro"));
					}
					// member테이블에 입력된 값을 화면에 출력
					out.print("<html><head><title>post</title></head>");
					out.print("<body>");
					out.print("<h1>POST방식으로 저장 성공했습니다!</h1>");

					out.print("ID : " + id + "<br/>");
					out.print("이름 : " + username + "<br/>");
					out.print("전화번호 : " + tel + "<br/>");
					out.print("email : " + email + "<br/>");
					out.print("학부 : ");
					for (int i = 0; i < depts.length; i++) {
						out.print(depts[i] + " ");
					}
					out.print("<br/>");
					out.print("성별 : " + gender + "<br/>");
					out.print("birth : " + birth + "<br/>");
					out.print("소개 : " + intro + "<br/>");
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