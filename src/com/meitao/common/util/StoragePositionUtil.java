package com.meitao.common.util;

import com.meitao.common.constants.Constant;
import com.meitao.model.Order;
import com.meitao.model.Storage;
import com.meitao.model.TranshipmentBill;

public class StoragePositionUtil {
	//在库的拆包前状态主要是存放储藏间type=beforeOpen的使用，进行在库判断，入库操作，遇到此状态的order，将判定为出库，不在库
	public final static String[] inStorageState4TranshipmentBeforeOpen = {
		Constant.TRANSHIPMENT_STATE_7
	};
	public final static String[] outStorageState4TranshipmentBeforeOpen = {//出库
		Constant.TRANSHIPMENT_STATE_6
	};
	public final static String[] outStorageState4TranshipmentBeforeWaitCheck = {//待检入库出库
		Constant.TRANSHIPMENT_STATE_5
	};
	//储藏间 type=afterOpen
	public final static String[] inStorageState4TranshipmentAfterOpen = {
		Constant.TRANSHIPMENT_STATE1, Constant.TRANSHIPMENT_STATE2, Constant.TRANSHIPMENT_STATE3 
	};
	public final static String[] outStorageState4TranshipmentAfterOpen = {//出库
		Constant.TRANSHIPMENT_STATE5
	};
	//储藏间type=order
	public final static String[] inStorageState4Orders = {
		 Constant.ORDER_STATE2, Constant.ORDER_STATE3, Constant.ORDER_STATE__10
	};
	public final static String[] outStorageState4Orders = {//出库
		Constant.ORDER_STATE1, Constant.ORDER_STATE4, Constant.ORDER_STATE5,Constant.ORDER_STATE6, Constant.ORDER_STATE7, Constant.ORDER_STATE8, Constant.ORDER_STATE9, Constant.ORDER_STATE10
	};
	
	/**
	 * 是否一个合法的出入库操作
	 * @param state 可能是order或transhipment的state
	 * @param storagePosition 要前往的储藏间
	 * @param operation 操作 0 ：入库，1出库
	 * @return
	 */
	public static boolean isCorrectOperation(String state, Storage storage, int operation){
		return checkState(getStateArray(storage, operation), state);
	}
	private static boolean checkState(String[] stateArray, String actualState){
		for(String state : stateArray){
			if(state.equals(actualState)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据参数自身的state判断应该进行的操作
	 * 现在只能用于transhipment， order
	 * @param object 请传递transhipment或order对象
	 * @return 0入库， 1出库， -1则失败
	 */
	public static int getOperation(Object object){
		String actualState = null;
		int result = -1;
		if(object instanceof TranshipmentBill){
			actualState = ((TranshipmentBill) object).getState();
			if(checkState(inStorageState4TranshipmentAfterOpen, actualState) || checkState(inStorageState4TranshipmentBeforeOpen, actualState)){
				result = 0;
			}else if(checkState(outStorageState4TranshipmentAfterOpen, actualState) || checkState(outStorageState4TranshipmentBeforeOpen, actualState) || checkState(outStorageState4TranshipmentBeforeWaitCheck, actualState)){
				result = 1;
			}
		}else if(object instanceof Order){
			actualState = ((Order) object).getState();
			if(checkState(inStorageState4Orders, actualState)){
				result = 0;
			}else if(checkState(outStorageState4Orders, actualState)){
				result = 1;
			}
		}
		return result;
	}
	
	/**
	 * 获取仓库应有的type
	 * @param object
	 * @return 获取失败，返回null
	 */
	public static String getType(Object object){
		String type = null;
		if(object instanceof TranshipmentBill){
			String actualState = ((TranshipmentBill) object).getState();
			if(checkState(inStorageState4TranshipmentBeforeOpen, actualState) || checkState(outStorageState4TranshipmentBeforeOpen, actualState)){
				type = Constant.STORAGE_TYPE_BEFORE_OPEN;
			}else if(checkState(inStorageState4TranshipmentAfterOpen, actualState) || checkState(outStorageState4TranshipmentAfterOpen, actualState)){
				type = Constant.STORAGE_TYPE_AFTER_OPEN;
			}
		}else if(object instanceof Order){
			type = Constant.STORAGE_TYPE_ORDER;
		}
		return type;
	}
	/**
	 * 
	 * @param type 储藏间类型
	 * @param operation 0:在库，入库 ； 1：不在库，出库
	 * @return
	 */
	public static String[] getStateArray(Storage storage, int operation) {
		String stateArray[] = null;
		/*switch(storage.getType()){
			case Constant.STORAGE_TYPE_BEFORE_OPEN:
				stateArray = operation==0 ? inStorageState4TranshipmentBeforeOpen : outStorageState4TranshipmentBeforeOpen;
				break;
			case Constant.STORAGE_TYPE_AFTER_OPEN:
				stateArray = operation==0 ? inStorageState4TranshipmentAfterOpen : outStorageState4TranshipmentAfterOpen;
				break;
			case Constant.STORAGE_TYPE_ORDER:
				stateArray = operation==0 ? inStorageState4Orders : outStorageState4Orders;
				break;
		}*/
		return stateArray;
	}
}
