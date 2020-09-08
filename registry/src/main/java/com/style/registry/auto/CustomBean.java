package com.style.registry.auto;

/**
 * @author leon
 * @date 2020-07-13 16:51:25
 */
public class CustomBean {
	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomBean(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public CustomBean() {
	}
}
