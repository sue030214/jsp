package practice;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Test extends HttpServlet{

	/***
	 * シリアルバージョンUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * JSPのベースディレクトリ
	 */
	private static final String JSP_BASE = "/WEB-INF/jsp";

	/**
	 * データベースのコネクションを保持します。
	 * */
	private Connection pooledConnection;


	@Override
	public void destroy(){

		if(pooledConnection != null){

			try{
				pooledConnection.close();
			}catch(SQLException e){
				;
			}

			pooledConnection = null;
		}

		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 要求からactionパラメータを取得
		String action = req.getParameter("action");

		String forward = null;

		if("login".equals(action)){

			// ログイン画面の処理
			// login.jspへフォワードする
			forward = JSP_BASE + "/login.jsp";
		}else {

			// 不正なアクションの場合
			throw new ServletException("不正なリクエストです");
		}

		// JSPへのフォワード
		RequestDispatcher dispatcher = req.getRequestDispatcher(forward);
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 要求からactionパラメータを取得
		String action = req.getParameter("action");

		String forward = null;
		if("login_action".equals(action)){

			// ログイン画面からの入力受付
			forward = doLoginAction(req, resp);
		}else{
			// 不正なアクションの場合
			forward = doError(req, resp, "不正なリクエストです。");
		}

		// JSPへのフォワード
		RequestDispatcher dispatcher = req.getRequestDispatcher(forward);
		dispatcher.forward(req, resp);
	}

	/**
	 *
	 * @param req 要求
	 * @param resp 応答
	 * @return	ログイン情報？
	 * @throws ServletException
	 * @throws IOException
	 */
	private String doLoginAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		String userID = req.getParameter("user_id");

		String password = req.getParameter("password");
		if(userID == null || password == null){

			throw new ServletException("不正なパラメータです。");
		}

		try{

			// ユーザー情報を取得します。
			User user = getUser(userID, password);
			if(user == null){

				return doError(req, resp, "不正なユーザーIDもしくはパラメータです。");
			}

			// 名前をセッションに格納する
			req.getSession().setAttribute("currentUser", user);

			// アイテムを取得する
			Item[] items = getItems();

			// アイテムを要求オブジェクトに格納する
			req.setAttribute("items", items);

			// 一覧を表示する
			return JSP_BASE + "list.jsp";
		}catch(SQLException e){

			return doError(req, resp, e.getMessage());

		}

	}

	/**
	 *
	 * @param userID ユーザーID
	 * @param password パスワード
	 * @return
	 * @throws SQLException
	 */
	private User getUser(String userID, String password) throws SQLException{

		Statement statement = null;

		try{
			// SQL文を発行
			statement = getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT" +
																"ID," +
																"NAME" +
															"FROM" +
																"TODO_USER" +
															"WHERE" +
																"ID='" + userID +
															"' AND" +
																"PASSWORD='" + password + "'");

			boolean br = resultSet.first();
			if(!br){

				return null;
			}

			User user = new User();
			user.setId(resultSet.getString("ID"));
			user.setName(resultSet.getString("NAME"));

			return user;
		}catch(SQLException e){

			throw e;

		}finally{

			if(statement != null){

				statement.close();
				statement = null;
			}
		}
	}


	/**
	 * 接続オブジェクトを生成します。
	 * @return
	 * @throws SQLException
	 */
	private Connection getConnection() throws SQLException{

		// Connectionの準備
		if(pooledConnection != null){

			return pooledConnection;
		}
		try{

			// 下準備
			Class.forName("org.h2.Driver");
			pooledConnection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~", "sa", "");
			return pooledConnection;

		}catch(ClassNotFoundException e){

			pooledConnection = null;
			throw new SQLException(e);

		}catch (SQLException e) {

			pooledConnection = null;
			throw e;
		}
	}

	/**
	 *
	 * @param req 要求
	 * @param resp 応答
	 * @param message メッセージ
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private String doError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException,IOException {

		req.setAttribute("message", message);

		// エラーを表示する
		return JSP_BASE + "error.jsp";
	}

	/**
	 * アイテム取得用のSQL文を生成します。
	 * @param where
	 * @return
	 */
	private String createSQL(String where){

		StringBuffer buf = new StringBuffer();

		buf.append("SELECT " +
						"TODO_ITEM.ID," +
						"TODO_ITEM.NAME," +
						"TODO_USER.ID," +
						"TODO_USER.NAME," +
						"EXPIRE_DATE,"+
						"FINISHED_DATE" +
					"FROM" +
						"TODO_USER," + "TODO_ITEM" +
					"WHERE" +
						"TODO_USER.ID = TODO_ITEM.USER");

		if(where != null){
			buf.append(" AND " + where);

		}
		return buf.toString();
	}

	/**
	 * アイテムを取得します。
	 * @return
	 * @throws SQLException
	 */
	private Item[] getItems() throws SQLException {

		Statement statement = null;
		try{
			// SQL文を発行
			statement = getConnection().createStatement();

			ResultSet resultSet = statement.executeQuery(createSQL(null));
			boolean br = resultSet.first();

			if(!br){

				return new Item[0];
			}

			ArrayList<Item> items = new ArrayList<Item>();
			do {

				Item item = new Item();
				item.setId(resultSet.getString("TODO_ITEM.ID"));
				item.setName(resultSet.getString("TODO_ITEM.NAME"));
				User user = new User();
				user.setId(resultSet.getString("TODO_USER.ID"));
				user.setName(resultSet.getString("TODO_USER.NAME"));
				item.setUser(user);
				item.setExpireDate(resultSet.getDate("EXPIRE_DATE"));
				item.setFinishedDate(resultSet.getDate("FINISHED_DATE"));

				items.add(item);
			}while(resultSet.next());

			return items.toArray(new Item[0]);
		}catch (SQLException e) {

			pooledConnection = null;
			throw e;

		}finally{

			if(statement != null){

				statement.close();
				statement = null;
			}
		}

	}
}
