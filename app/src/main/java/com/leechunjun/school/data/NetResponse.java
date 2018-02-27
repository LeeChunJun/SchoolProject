package com.leechunjun.school.data;

public interface NetResponse {
	/**
	 * 根据返回内容执行具体逻辑
	 * @param response 返回信息
	 * @param code 返回代码
	 */
	public void onFinished(String response, int code);

}
