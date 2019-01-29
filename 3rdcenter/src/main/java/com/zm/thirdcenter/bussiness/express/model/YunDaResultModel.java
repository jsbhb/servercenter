package com.zm.thirdcenter.bussiness.express.model;

public class YunDaResultModel {

	private String hawbno;
	private String mail_no;
	private String pdf_info;
	private String code;
	private String msg;
	public String getHawbno() {
		return hawbno;
	}
	public void setHawbno(String hawbno) {
		this.hawbno = hawbno;
	}
	public String getMail_no() {
		return mail_no;
	}
	public void setMail_no(String mail_no) {
		this.mail_no = mail_no;
	}
	public String getPdf_info() {
		return pdf_info;
	}
	public void setPdf_info(String pdf_info) {
		this.pdf_info = pdf_info;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hawbno == null) ? 0 : hawbno.hashCode());
		result = prime * result + ((mail_no == null) ? 0 : mail_no.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YunDaResultModel other = (YunDaResultModel) obj;
		if (hawbno == null) {
			if (other.hawbno != null)
				return false;
		} else if (!hawbno.equals(other.hawbno))
			return false;
		if (mail_no == null) {
			if (other.mail_no != null)
				return false;
		} else if (!mail_no.equals(other.mail_no))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "YunDaResultModel [hawbno=" + hawbno + ", mail_no=" + mail_no + ", pdf_info=" + pdf_info + ", code="
				+ code + ", msg=" + msg + "]";
	}
}
