package com.zhuanjingkj.zjcbe.utility.web.arron;

/**
 * @description:
 * @author: wangliying
 * @create: 2018-06-14
 **/
public class HttpProcessException extends Exception {

    private static final long serialVersionUID = 3250476277591172656L;

    /**
	 *
	 */
	//private static final long serialVersionUID = -2749168865492921426L;
	public HttpProcessException(Exception e) {
		super(e);
	}

	/**
	 * @param msg
	 */
	public HttpProcessException(String msg) {
		super(msg);
	}

	/**
	 * @param message
	 * @param e
	 */
	public HttpProcessException(String message, Exception e) {
		super(message, e);
	}

}