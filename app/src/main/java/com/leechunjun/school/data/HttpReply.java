package com.leechunjun.school.data;

public interface HttpReply {
	/**
	 * 根据返回内容执行具体逻辑
	 * @param response
	 */
	void onFinished(String response);
	/**
	 * 对异常情况的处理
	 * @param e
	 */
	void onError(Exception e);

}
