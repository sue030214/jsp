package practice;

import java.util.Date;

public class Item {

	// ID
	private String id;

	// 名前
	private String name;

	// ユーザー
	private User user;

	// 期限
	private Date expireDate;

	// 終了日時
	private Date finishedDate;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id セットする id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user セットする user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate セットする expireDate
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return finishedDate
	 */
	public Date getFinishedDate() {
		return finishedDate;
	}

	/**
	 * @param finishedDate セットする finishedDate
	 */
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}



}
