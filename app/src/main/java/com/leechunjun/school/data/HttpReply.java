package com.leechunjun.school.data;

public interface HttpReply {
	/**
	 * ���ݷ�������ִ�о����߼�
	 * @param response
	 */
	void onFinished(String response);
	/**
	 * ���쳣����Ĵ���
	 * @param e
	 */
	void onError(Exception e);

}
