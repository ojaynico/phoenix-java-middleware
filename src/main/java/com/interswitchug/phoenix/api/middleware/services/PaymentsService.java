package com.interswitchug.phoenix.api.middleware.services;

import com.interswitchug.phoenix.api.middleware.dto.*;
import com.interswitchug.phoenix.api.middleware.utils.AuthUtils;
import com.interswitchug.phoenix.api.middleware.utils.Constants;
import com.interswitchug.phoenix.api.middleware.utils.CryptoUtils;
import com.interswitchug.phoenix.api.middleware.utils.HttpUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentsService {

	public String validateCustomer(PaymentRequest request, String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.ROOT_LINK + "sente/customerValidation";
		request.setTerminalId(Constants.TERMINAL_ID);

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.POST_REQUEST, endpointUrl, "",
				authToken, terminalKey);

		String jsonString = JSONDataTransform.marshall(request);
		return HttpUtil.postHTTPRequest(endpointUrl, headers, jsonString);
	}

	public String makePayment(PaymentRequest request, String authToken, String terminalKey) throws Exception {

		String endpointUrl = Constants.ROOT_LINK + "sente/xpayment";

        request.setTerminalId(Constants.TERMINAL_ID);
		String additionalData = request.getAmount()+"&"
		+request.getTerminalId()+"&"
				+request.getRequestReference()+"&"
		+ request.getCustomerId()+"&" +request.getPaymentCode();

		if(request.getOtp() != null)
			request.setOtp(CryptoUtils.encrypt(request.getOtp(), terminalKey));

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.POST_REQUEST, endpointUrl, additionalData,
				authToken, terminalKey);

		return  HttpUtil.postHTTPRequest(endpointUrl, headers, JSONDataTransform.marshall(request));
	}

	public String fetchBalance(String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.ROOT_LINK +  "sente/accountBalance";
		String request = endpointUrl +"?terminalId="+ Constants.TERMINAL_ID + "&requestReference="+java.util.UUID.randomUUID();

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.GET_REQUEST, request, "", authToken, terminalKey);
		return HttpUtil.getHTTPRequest(request, headers);
	}

	public String checkStatus(String requestReference, String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.ROOT_LINK +  "sente/status";
		String request = endpointUrl +"?terminalId="+ Constants.TERMINAL_ID + "&requestReference="+requestReference;

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.GET_REQUEST, request, "", authToken, terminalKey);
		return HttpUtil.getHTTPRequest(request, headers);
	}

	public String getCategories(String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.BILLERS_ROOT +  "categories-by-client/"+Constants.TERMINAL_ID+"/"+ Constants.TERMINAL_ID;

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.GET_REQUEST, endpointUrl, "", authToken, terminalKey);
		return HttpUtil.getHTTPRequest(endpointUrl, headers);
	}

	public String getCategoryBillers(String categoryId, String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.BILLERS_ROOT +  "biller-by-category/"+categoryId;

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.GET_REQUEST, endpointUrl, "", authToken, terminalKey);
		return HttpUtil.getHTTPRequest(endpointUrl, headers);
	}

	public String getBillerItems(String billerId, String authToken, String terminalKey) throws Exception {

		String endpointUrl =  Constants.BILLERS_ROOT +  "items/biller-id/"+billerId;

		Map<String,String> headers = AuthUtils.generateInterswitchAuth(Constants.GET_REQUEST, endpointUrl, "", authToken, terminalKey);
		return HttpUtil.getHTTPRequest(endpointUrl, headers);
	}
}
