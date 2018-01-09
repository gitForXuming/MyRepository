package com.designPattern.chainOfResponsibility;

public class ObjectHandler extends Handler {

	@Override
	public void handlerRequest(String user, double fee) {
		if("test".equals(user)&&fee<=new Double("1000.00").doubleValue()){
			System.out.println(String.format("[%s]是我项目组的,申请了[%s]元的项目经费，我已经批准", user,String.valueOf(fee)));
		}else{
			System.out.println(String.format("[%s]是我项目组的,申请了[%s]元的项目经费，超过了我批准范围 我无法批准 ,提交到部门经理批准",
					user,String.valueOf(fee)));
			if(null!= this.getNextHandler()){
				this.getNextHandler().handlerRequest(user, fee);
			}else{
				System.out.println("该笔项目经费申请失败！");
			}
		}
	}
}
