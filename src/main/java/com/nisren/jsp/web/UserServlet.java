package com.nisren.jsp.web;

import com.nisren.jsp.bean.User;
import com.nisren.jsp.dao.UserDao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/" })
//@WebServlet(asyncSupported = true, urlPatterns = { "/UserServlet" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDao;
       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public UserServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
//	public void init(ServletConfig config) throws ServletException {
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		userDao = new UserDao();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		//Handle all actions
		//Create Switch statement
		String action = request.getServletPath();
		switch(action)
		{
			case "/new":
				showNewForm(request, response);
				break;

			case "/insert":
				try {
					insertUser(request, response);
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
				break;

			case "/delete":
				try {
					deleteUser(request, response);
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
				break;

			case "/edit":
				showEditForm(request, response);
				break;

			case "/update":
				try {
					updateUser(request, response);
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
				break;


			default:
				listUser(request, response);
				break;



		}


	}
	private void showNewForm( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User user = new User(name, email, country);
		userDao.insertUser(user);
		response.sendRedirect("list");
	}

	private void deleteUser( HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		try {
			userDao.deleteUser(id);
		} catch ( Exception e){
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}

	//edit
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser;
		existingUser = userDao.selectUserById(id);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		requestDispatcher.forward(request, response);
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User user = new User(id, name, email, country);
		userDao.updateUser(user);
		response.sendRedirect("list");
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<User> listUser  = userDao.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-list.jsp");
		requestDispatcher.forward(request, response);
	}







}
