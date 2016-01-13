package com.app.demos.ui.test.zhangBen;

/**
 * @desc <pre>
 * 类别数据模型
 * </pre>
 * @author Weiliang Hu
 * @Date 2013-12-11
 */
public class CategoryModle {
	/** 类别资源图片对应的ID */
	private int id;

	/** 类别资源对应的文字描述 */
	private String character;

	public CategoryModle(int resId, String categoryName) {
		id = resId;
		character = categoryName;
	}

	public CategoryModle() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

}
