package cn.e3mall.domian;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGridResult implements Serializable{

	
	private Integer total;
	
	//通配符
	private List<?> rows;

	public EasyUIDataGridResult(Integer total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
	
	
	public EasyUIDataGridResult(Long total, List<?> rows) {
		super();
		this.total = total.intValue();
		this.rows = rows;
	}



	public Integer getTotal() {
		return total;
	}



	public void setTotal(Integer total) {
		this.total = total;
	}



	public List<?> getRows() {
		return rows;
	}



	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
