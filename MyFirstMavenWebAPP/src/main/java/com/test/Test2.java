package com.test;

public class Test2 {
	public static void changeStr(String str){
		str="welcome";
	}
	
	public static void changeObject(Model model){
		model.setTest("welcome");
	}
	
	public static void main(String[] args) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t1.tfl_batchno  , t1.tfl_payacc  ,t1.tfl_payname ,t1.tfl_paynode  ,t4.brh_name,").append(" ");
		sql.append("substr(t1.tfl_batchno,0,8) ,").append(" ");
		sql.append("count(*) ,sum(case when t1.tfl_stt='20' then 1 else 0 end) ,sum(case when t1.tfl_stt！='20' then 1 else 0 end) ,").append(" ");
		sql.append("sum(t1.tfl_tranamt) ,sum(case when t1.tfl_stt='20' then t1.tfl_tranamt else 0 end) ,").append(" ");
		sql.append("sum(case when t1.tfl_stt!='20' then t1.tfl_tranamt else 0 end) ").append(" ");
		sql.append("from cb_tranflow t1 left join im_branch t4 on t1.tfl_paynode = t4.brh_id where t1.tfl_batchno in(");
		sql.append("select t2.bfl_batchno from cb_batch_flow t2 left join cust_info t3 on t2.bfl_cstno= t3.cust_no ").append(" ");
		sql.append("where t3.bankflag='hmsh' ) ").append(" ");
		sql.append("group by t1.tfl_batchno,t1.tfl_payacc ,t1.tfl_payname ,t1.tfl_paynode ,substr(t1.tfl_batchno,0,8),t4.brh_name ").append(" ");
		sql.append("order by substr(t1.tfl_batchno,0,8);");
		System.out.println(sql.toString());
		final String a="a";
		final String bc ="bc";
		String s1 ="a"+"bc";
		String s2=a + bc;
		System.out.println(s1==s2);
		 
		String str ="1234";
		Model model = new Model();
		model.setTest("1234");
		changeStr(str);
		changeObject(model);
		System.out.println(str);
		System.out.println(model.getTest());
		
		System.out.println(calculateCapacity(63));
		System.out.println(calculateCapacity1(63));
	}
	
	  private static final int calculateCapacity(int x) {
	        if (x >= 1 << 30) {
	            return 1 << 30;
	        }
	        if (x == 0) {
	            return 16;
	        }
	        if (x == 1) {
	            return 2;
	        }
	        x = x - 1;
	        x |= x >> 1;
	        x |= x >> 2;
	        x |= x >> 4;
	        x |= x >> 8;
	        x |= x >> 16;
	        return x + 1;
	    }
	  
	  private static final int calculateCapacity1(int x) {
	        if (x >= 1 << 30) {
	            return 1 << 30;
	        }
	        if (x == 0) {
	            return 16;
	        }
	        if (x == 1) {
	            return 2;
	        }
	       if(x%2==0){
	    	   return x<<1;
	       }else{
	    	   int len = ((x/2)+1);
	    	   return 1<<((x/2)+1);
	       }
	     
	    }
	static class Model{
		private String test="";

		public String getTest() {
			return test;
		}

		public void setTest(String test) {
			this.test = test;
		}
		
	}
}	
